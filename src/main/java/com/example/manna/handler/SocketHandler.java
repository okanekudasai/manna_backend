package com.example.manna.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;

@Component
@Getter
@Slf4j
public class SocketHandler extends TextWebSocketHandler {
    HashSet<WebSocketSession> session_set = new HashSet<>();
    HashMap<WebSocketSession, SessionInfo> session_info = new HashMap<>();

    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> dto;
    HashMap<String, Object> value;

    HashSet<ChatRoom> chatRoomSet;

    int room_number;

    @PostConstruct
    public void init() {

        room_number = 1;

        // 빈 초기화 작업 수행
        dto = new HashMap<>();
        dto.put("code", "");
        dto.put("value", value);

        // 룸 초기화 작업
        chatRoomSet = new HashSet<>();
//        chatRoomSet.add(ChatRoom.builder().idx(room_number++).title("dummy").people_set(new HashSet<>()).host(null).build());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // 일단 누가 연결됐다는걸 로그로 기록할게요
        super.afterConnectionEstablished(session);
        log.info("소켓에 연결되었어요! " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        JsonElement element = JsonParser.parseString(message.getPayload());
        String event = element.getAsJsonObject().get("event").getAsString();
        JsonElement data = element.getAsJsonObject().get("data").getAsJsonObject();
        if (event.equals("ping")) {
            log.info("pong! " + session.getId());
            dto.put("code", "pong");
            dto.put("value", "pong");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
        } else if (event.equals("enter_people")) {

            String nickname = data.getAsJsonObject().get("nickname").getAsString();
            int level = data.getAsJsonObject().get("level").getAsInt();
            int exp = data.getAsJsonObject().get("exp").getAsInt();
            SessionInfo new_person_info = SessionInfo.builder().session_id(session.getId()).exp(exp).level(level).nickname(nickname).room_number(0).build();

            // 기존에 있던 사람들에게 새로운 사람이 왔다는걸 알려요
            value = new HashMap<>();
            dto.put("code", "new_person_come");
            dto.put("value", value);
            value.put("person", new_person_info);
            HashSet<SessionInfo> existing_people = new HashSet<>();
            for (WebSocketSession s : session_set) {
                s.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
                existing_people.add(session_info.get(s));
            }

            // 새로온 사람에게 기존의 사람이 누가 있는지를 알려요
            dto.put("code", "existing_people_list");
            value = new HashMap<>();
            value.put("people_list", existing_people);
            dto.put("value", value);
            session.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));

            // 새로온 사람을 접속한 사람 셋에 추가해요
            session_set.add(session);
            session_info.put(session, new_person_info);

            // 현재 몇명이 접속한지를 출력해요
            System.out.println("현재접속인원 : " + session_set.size());
        } else if (event.equals("make_room")) {

            SessionInfo host = session_info.get(session);
            String title = data.getAsJsonObject().get("title").getAsString();
            HashSet<SessionInfo> people_set = new HashSet<>();
            ChatRoom new_room = ChatRoom.builder().idx(room_number).people_set(people_set).host(host).title(title).build();
            chatRoomSet.add(new_room);

            dto.put("code", "get_my_room_number");
            dto.put("value", String.valueOf(room_number++));
            session.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
        } else if (event.equals("enter_lobby")) {
            SessionInfo s = session_info.get(session);
            if (s == null) {
                log.info("등록되지 않은 세션");
                return;
            }
            int idx = data.getAsJsonObject().get("idx").getAsInt();
            for (ChatRoom room : chatRoomSet) {
                if (room.getIdx() != idx) continue;
                room.getPeople_set().remove(s);
                if (room.getPeople_set().isEmpty()) {
                    chatRoomSet.remove(room);
                }
                break;
            }
            s.room_number = 0;
            HashMap<String, Object> value = new HashMap<>();
            dto.put("code", "get_room_list");
            dto.put("value", value);

            value.put("chatroom_set", chatRoomSet);
            HashSet<SessionInfo> people_set_except_me = new HashSet<>(session_info.values());
            value.put("people_set", people_set_except_me);

            for (WebSocketSession ws : session_set) {
                people_set_except_me.remove(session_info.get(ws));
                ws.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
                people_set_except_me.add(session_info.get(ws));
            }
        } else if (event.equals("enter_room")) {
            SessionInfo s = session_info.get(session);
            if (s == null) {
                log.info("등록되지 않은 세션");
                return;
            }
            int idx = data.getAsJsonObject().get("idx").getAsInt();
            for (ChatRoom room : chatRoomSet) {
                if (room.getIdx() != idx) continue;
                System.out.println("찾음!");
                room.getPeople_set().add(s);
                break;
            }
            s.room_number = idx;
            HashMap<String, Object> value = new HashMap<>();
            dto.put("code", "get_room_list");
            dto.put("value", value);

            value.put("chatroom_set", chatRoomSet);
            HashSet<SessionInfo> people_set_except_me = new HashSet<>(session_info.values());
            System.out.println(people_set_except_me);
            value.put("people_set", people_set_except_me);

            for (WebSocketSession ws : session_set) {
                people_set_except_me.remove(session_info.get(ws));
                ws.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
                people_set_except_me.add(session_info.get(ws));
            }
        } else if (event.equals("room_chat")) {
            String sender = data.getAsJsonObject().get("sender").getAsString();
            String text = data.getAsJsonObject().get("text").getAsString();
            int room_number = data.getAsJsonObject().get("roomNumber").getAsInt();

            HashMap<String, String> content = new HashMap<>();
            dto.put("code", "get_lobby_chat");
            dto.put("value", content);
            content.put("sender", sender);
            content.put("text", text);
            for (WebSocketSession ws : session_set) {
                if (session_info.get(ws).room_number != room_number) {
                    System.out.println("다른곳에 있음");
                    continue;
                }
                ws.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // 일단 연결이 끊겼다는걸 로그에 찍을게요
        super.afterConnectionClosed(session, status);
        log.info("소켓과 연결이 끊겼어요! " + session.getId());

        // 더이상 끊어진 세션에는 볼일이 없으니 삭제해요
        session_set.remove(session);

        // dto를 만들게요
        dto.put("code", "person_leaved");
        value = new HashMap<>();
        SessionInfo si = session_info.get(session);
        value.put("person", si);
        int room_idx = si.getRoom_number();
        if (room_idx > 0) {
            for (ChatRoom room : chatRoomSet) {
                if (room.getIdx() != room_idx) continue;
                room.getPeople_set().remove(si);
                if (room.getPeople_set().isEmpty()) chatRoomSet.remove(room);
                break;
            }
        }
        value.put("room", chatRoomSet);
        dto.put("value", value);

        for (WebSocketSession s : session_set) {
            s.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
        }

        // 더이상 떠난 사람의 정보를 보관할 필요가 없어요
        session_info.remove(session);

        System.out.println("남은사람 수 : " + session_set.size());
    }
}