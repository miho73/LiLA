package com.github.miho73.lila.services.oauth;

import java.math.BigInteger;
import java.security.SecureRandom;

public abstract class OAuthService {
    public String createStateCode() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    public abstract String[] getToken(String code) throws Exception;

    public abstract String getAuthUri(String state);
}
