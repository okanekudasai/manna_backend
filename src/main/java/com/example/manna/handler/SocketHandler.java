package com.example.manna.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
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
    HashMap<String, Object> dto = new HashMap<>();
    HashMap<String, Object> value;

//    @PostConstruct
//    public void init() {
//        // 빈 초기화 작업 수행
//        dto = new HashMap<>();
//        dto.put("code", "");
//        dto.put("value", value);
//    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // 일단 누가 연결됐다는걸 로그로 기록할게요
        super.afterConnectionEstablished(session);
        log.info("소켓에 연결되었어요! " + session.getId());

        // 기존에 있던 사람들에게 새로운 사람이 왔다는걸 알려요
        SessionInfo new_person_info = SessionInfo.builder().session_id(session.getId()).build();
        dto.put("code", "new_person_come");
        value = new HashMap<>();
        value.put("person", new_person_info);
        dto.put("value", value);
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
        session_info.put(session, SessionInfo.builder().session_id(session.getId()).build());

        // 현재 몇명이 접속한지를 출력해요
        System.out.println("현재접속인원 : " + session_set.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        JsonElement element = JsonParser.parseString(message.getPayload());
        String event = element.getAsJsonObject().get("event").getAsString();
        JsonElement data = element.getAsJsonObject().get("data").getAsJsonObject();
        if (event.equals("ping")) {
            log.info("pong! " + session.getId());
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
        value.put("person", session_info.get(session));
        dto.put("value", value);

        for (WebSocketSession s : session_set) {
            s.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
        }

        // 더이상 떠난 사람의 정보를 보관할 필요가 없어요
        session_info.remove(session);

        System.out.println("남은사람 수 : " + session_set.size());
    }
}