package com.github.miho73.lila.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component("HttpConnection")
public class HttpConnection {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate = null;

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

    public String httpPostRequest(String uri, Map<String, String> requestParams) throws Exception {
        return this.httpRequest(uri, requestParams, HttpMethod.POST);
    }
    public String httpGetRequest(String uri, Map<String, String> requestParams) throws Exception {
        return this.httpRequest(uri, requestParams, HttpMethod.GET);
    }
    public String httpPostRequest(String uri, MultiValueMap<String, String> requestParams, MultiValueMap<String, String> header) throws Exception {
        return this.httpRequest(uri, requestParams, header, HttpMethod.POST);
    }
    public String httpGetRequest(String uri, MultiValueMap<String, String> requestParams, MultiValueMap<String, String> header) throws Exception {
        return this.httpRequest(uri, requestParams, header, HttpMethod.GET);
    }

    private String httpRequest(String uri, Map<String, String> requestParams, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Map<String, String >> requestEntity = new HttpEntity<>(requestParams, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);
            return response.getBody();
        } catch (Exception e) {
            logger.error("Failed to complete http request", e);
            throw e;
        }
    }
    private String httpRequest(String uri, MultiValueMap<String, String> requestParams, MultiValueMap<String, String> header, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.addAll(header);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestParams, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);
            return response.getBody();
        } catch (Exception e) {
            logger.error("Failed to complete http request", e);
            throw e;
        }
    }
}
