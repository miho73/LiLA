package com.github.miho73.lila.services.oidc;

import com.github.miho73.lila.utils.HttpConnection;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("GoogleAuthService")
public class GoogleOIDCService extends OIDCService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${lila.oidc.google.client-id}") private String clientId;
    @Value("${lila.oidc.google.client-secret}") private String clientSecret;
    @Value("${lila.oidc.google.redirect-uri}") private String redirectUri;
    @Value("${lila.oidc.google.auth-request-uri}") private String authRequestUri;
    @Value("${lila.oidc.google.access-token-uri}") private String accessTokenUri;

    @Autowired HttpConnection httpConnection;

    @Override
    public String getAuthUri(String state) {
        return String.format(authRequestUri, clientId, redirectUri, state);
    }

    @Override
    public JSONObject getToken(String code) throws Exception {
        try {
            String response = httpConnection.httpPostRequest(accessTokenUri, Map.of(
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "code", code,
                    "grant_type", "authorization_code",
                    "redirect_uri", redirectUri
            ));
            return new JSONObject(response);
        } catch (Exception e) {
            logger.error("Failed to get access token from Google", e);
            throw e;
        }
    }
}
