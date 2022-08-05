package com.github.miho73.lila.services.oidc;

import com.github.miho73.lila.utils.HttpConnection;
import com.google.common.collect.ListMultimap;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service("KakaoAuthService")
public class KakaoOIDCService extends OIDCService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${lila.oidc.kakao.client-id}") private String clientId;
    @Value("${lila.oidc.kakao.client-secret}") private String clientSecret;
    @Value("${lila.oidc.kakao.redirect-uri}") private String redirectUri;
    @Value("${lila.oidc.kakao.auth-request-uri}") private String authRequestUri;
    @Value("${lila.oidc.kakao.access-token-uri}") private String accessTokenUri;
    @Value("${lila.oidc.kakao.user-data-uri}") private String userDataUri;

    @Autowired HttpConnection httpConnection;

    @Override
    public String getAuthUri(String state) {
        return String.format(authRequestUri, clientId, redirectUri, state);
    }

    @Override
    public JSONObject getToken(String code) throws Exception {
        try {
            Map<String, List<String>> requestParams = Map.of(
                    "client_id", List.of(clientId),
                    "client_secret", List.of(clientSecret),
                    "code", List.of(code),
                    "grant_type", List.of("authorization_code"),
                    "redirect_uri", List.of(redirectUri)
            );
            String response = httpConnection.httpPostRequest(accessTokenUri, new LinkedMultiValueMap<>(requestParams), new LinkedMultiValueMap<>());
            return new JSONObject(response);
        } catch (Exception e) {
            logger.error("Failed to retrieve kakao token", e);
            throw e;
        }
    }

    public JSONObject getUserData(String accessToken) throws Exception {
        try {
            Map<String, List<String>> requestParams = Map.of(
                    "secure_resource", List.of("true"),
                    "property_keys", List.of("[\"kakao_account.profile\",\"kakao_account.email\"]")
            );
            Map<String, List<String>> header = Map.of(
                    "Authorization", List.of("Bearer "+accessToken)
            );
            String response = httpConnection.httpPostRequest(userDataUri, new LinkedMultiValueMap<>(requestParams), new LinkedMultiValueMap<>(header));
            return new JSONObject(response).getJSONObject("kakao_account");
        } catch (Exception e) {
            logger.error("Failed to retrieve kakao token", e);
            throw e;
        }
    }
}
