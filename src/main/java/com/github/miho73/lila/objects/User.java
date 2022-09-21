package com.github.miho73.lila.objects;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class User {
    private int userCode;
    private String userId;
    private String refreshToken;
    private String userName;
    private String email;
    private String privilege;
    private AUTH_SOURCES authFrom;
    private Timestamp joinDate;
    private Timestamp lastLogin;

    public enum AUTH_SOURCES {
        GOOGLE(0),
        KAKAO(1);

        private final int value;

        AUTH_SOURCES(int value) {
            this.value = value;
        }

        public int getInteger() {
            return value;
        }
    }
}
