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
@Service("KakaoOAuthService")
public class KakaoOAuthService extends OAuthService {

    @Value("${lila.oauth.kakao.client-id}")
    private String clientId;
    @Value("${lila.oauth.kakao.client-secret}")
    private String clientSecret;
    @Value("${lila.oauth.kakao.auth-request-uri}")
    private String authRequestUri;
    @Value("${lila.oauth.kakao.access-token-uri}")
    private String accessTokenUri;
    @Value("${lila.oauth.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${lila.oauth.kakao.user-data-uri}")
    private String userDataUri;

    @Autowired
    HttpConnection httpConnection;

    @Override
    public String getAuthUri(String state) {
        return String.format(authRequestUri, clientId, redirectUri, state);
    }

    public String[] getToken(String code) throws Exception {
        try {
            Map<String, List<String>> requestParams = Map.of(
                    "client_id", List.of(clientId),
                    "client_secret", List.of(clientSecret),
                    "code", List.of(code),
                    "grant_type", List.of("authorization_code"),
                    "redirect_uri", List.of(redirectUri)
            );
            String response = httpConnection.httpPostRequest(
                    accessTokenUri,
                    new LinkedMultiValueMap<>(requestParams),
                    new LinkedMultiValueMap<>()
            );
            JSONObject responseJson = new JSONObject(response);
            return new String[] {
                    responseJson.getString("access_token"),
                    responseJson.getString("refresh_token")
            };
        } catch (Exception e) {
            log.error("Failed to retrieve kakao token", e);
            throw e;
        }
    }

    public JSONObject getUserData(String accessToken) throws Exception {
        try {
            Map<String, List<String>> requestParams = Map.of(
                    "secure_resource", List.of("true")
            );
            Map<String, List<String>> header = Map.of(
                    "Authorization", List.of("Bearer "+accessToken)
            );
            String response = httpConnection.httpPostRequest(userDataUri, new LinkedMultiValueMap<>(requestParams), new LinkedMultiValueMap<>(header));
            return new JSONObject(response);
        } catch (Exception e) {
            log.error("Failed to get user data from kakao", e);
            throw e;
        }
    }
}
