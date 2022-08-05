package com.github.miho73.lila.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.miho73.lila.utils.HttpConnection;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Service("JWTService")
public class JWTService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${lila.oidc.google.client-id}") String googleClientId;

    @Value("${lila.oidc.google.certs-uri}") String googleCertsUri;

    @Autowired HttpConnection httpConnection;

    private JSONArray googleCertKeys;

    @PostConstruct
    public void initVerifier() {
        try {
            String response = httpConnection.httpGetRequest(googleCertsUri, Map.of());
            googleCertKeys = new JSONObject(response).getJSONArray("keys");
        }
        catch (Exception e) {
            logger.error("Cannot initiate Google JWT validator.", e);
        }
    }

    /**
     * Verify if JWT token from Google is valid
     * @param token JWT to validate
     * @return true when valid. false when invalid
     */
    public boolean verifyGoogleToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);

            final var wrapper = new Object() {String n, e;};

            // select key from keys by kid
            googleCertKeys.forEach((key) -> {
                if(((JSONObject)key).getString("kid").equals(jwt.getKeyId())) {
                    wrapper.n = ((JSONObject)key).getString("n");
                    wrapper.e = ((JSONObject)key).getString("e");
                }
            });

            // RSA n, e > BigInt
            Base64.Decoder decoder = Base64.getUrlDecoder();
            BigInteger n = new BigInteger(1, decoder.decode(wrapper.n));
            BigInteger e = new BigInteger(1, decoder.decode(wrapper.e));

            // verify
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey)factory.generatePublic(keySpec), null);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("https://accounts.google.com", "accounts.google.com")
                    .withAudience(googleClientId)
                    .build();

            verifier.verify(token);
            return true;
        } catch (Exception e){
            logger.warn("cannot verify jwt token", e);
            return false;
        }
    }

    /**
     * Decode JWT token text. THIS FUNCTION WILL NOT VERIFY TOKEN
     * @param jwtText Base64 encoded jwt token
     * @return decoded jwt token
     */
    public DecodedJWT decodeJWT(String jwtText) {
        return JWT.decode(jwtText);
    }
}
