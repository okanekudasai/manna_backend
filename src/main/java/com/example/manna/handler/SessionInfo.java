package com.example.manna.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SessionInfo {
    String session_id;
    String nickname;
    int level;
    int exp;
    int room_number;
}
