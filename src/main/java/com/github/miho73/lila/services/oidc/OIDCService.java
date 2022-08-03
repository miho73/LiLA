package com.github.miho73.lila.services.oidc;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.SecureRandom;

public abstract class OIDCService {

    public abstract JSONObject getToken(String code);

    public String createStateCode() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    public abstract String getAuthUri(String state);
}
