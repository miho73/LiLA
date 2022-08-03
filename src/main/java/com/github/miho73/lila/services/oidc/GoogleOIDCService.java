package com.github.miho73.lila.services.oidc;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service("GoogleAuthService")
public class GoogleOIDCService extends OIDCService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${lila.oidc.google.client-id}") private String clientId;
    @Value("${lila.oidc.google.client-secret}") private String clientSecret;
    @Value("${lila.oidc.google.redirect-uri}") private String redirectUri;
    @Value("${lila.oidc.google.auth-request-uri}") private String authRequestUri;
    @Value("${lila.oidc.google.access-token-uri}") private String accessTokenUri;

    private static RestTemplate restTemplate;

    @PostConstruct
    public void initConnection() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(2000);
        httpRequestFactory.setReadTimeout(3000);
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(20)
                .build();
        httpRequestFactory.setHttpClient(httpClient);
        restTemplate = new RestTemplate(httpRequestFactory);
    }

    @Override
    public String getAuthUri(String state) {
        return String.format(authRequestUri, clientId, redirectUri, state);
    }

    @Override
    public JSONObject getToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String > requestParams = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "code", code,
                "grant_type", "authorization_code",
                "redirect_uri", redirectUri
        );
        HttpEntity<Map<String, String >> requestEntity = new HttpEntity<>(requestParams, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(accessTokenUri, requestEntity, String.class);
            return new JSONObject(response.getBody());
        } catch (RestClientException e) {
            logger.error("Failed to get access token from Google", e);
            throw e;
        }
    }
}
