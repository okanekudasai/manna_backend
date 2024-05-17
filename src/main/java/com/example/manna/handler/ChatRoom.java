package com.example.manna.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatRoom {
    int idx;
    String title;
    HashSet<SessionInfo> people_set;
    SessionInfo host;
}
