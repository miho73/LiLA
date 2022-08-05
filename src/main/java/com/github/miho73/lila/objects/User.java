package com.github.miho73.lila.objects;

import java.sql.Timestamp;

public class User {
    private int userCode;
    private String userId;
    private String refreshToken;
    private String userName;
    private String email;

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    private String privilege;
    private AUTH_SOURCES authFrom;
    private Timestamp joinDate;

    public AUTH_SOURCES getAuthFrom() {
        return authFrom;
    }

    public void setAuthFrom(AUTH_SOURCES authFrom) {
        this.authFrom = authFrom;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

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

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
