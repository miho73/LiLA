package com.github.miho73.lila.services.oauth;

import com.github.miho73.lila.utils.HttpConnection;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("GoogleOAuthService")
public class GoogleOAuthService extends OAuthService {

    @Value("${lila.oauth.google.client-id}")
    private String clientId;
    @Value("${lila.oauth.google.client-secret}")
    private String clientSecret;
    @Value("${lila.oauth.google.auth-request-uri}")
    private String authRequestUri;
    @Value("${lila.oauth.google.access-token-uri}")
    private String accessTokenUri;
    @Value("${lila.oauth.google.redirect-uri}")
    private String redirectUri;
    @Value("${lila.oauth.google.user-data-uri}")
    private String userDataUri;

    @Autowired
    HttpConnection httpConnection;

    @Override
    public String getAuthUri(String state) {
        return String.format(authRequestUri, clientId, redirectUri, state);
    }

    public String[] getToken(String code) throws Exception {
        try {
            String response = httpConnection.httpPostRequest(accessTokenUri, Map.of(
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "code", code,
                    "grant_type", "authorization_code",
                    "redirect_uri", redirectUri
            ));
            JSONObject responseJson = new JSONObject(response);
            return new String[] {
                    responseJson.getString("access_token"),
                    responseJson.getString("refresh_token")
            };
        } catch (Exception e) {
            log.error("Failed to get access token from Google", e);
            throw e;
        }
    }

    public JSONObject getUserData(String accessToken) throws Exception {
        try {
            Map<String, List<String>> requestParams = Map.of();
            Map<String, List<String>> header = Map.of(
                    "Authorization", List.of("Bearer "+accessToken)
            );
            String response = httpConnection.httpGetRequest(userDataUri, new LinkedMultiValueMap<>(requestParams), new LinkedMultiValueMap<>(header));
            return new JSONObject(response);
        } catch (Exception e) {
            log.error("Failed to get user data from google", e);
            throw e;
        }
    }
}
