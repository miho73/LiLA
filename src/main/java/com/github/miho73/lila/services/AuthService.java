package com.github.miho73.lila.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.miho73.lila.Repositories.UserRepository;
import com.github.miho73.lila.objects.User;
import com.github.miho73.lila.services.oidc.GoogleOIDCService;
import com.github.miho73.lila.services.oidc.KakaoOIDCService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Nonnull;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired GoogleOIDCService googleOIDCService;
    @Autowired KakaoOIDCService kakaoOIDCService;
    @Autowired JWTService jwtService;
    @Autowired UserRepository userRepository;

    @Value("${server.servlet.jwt.cookie.http-only}") boolean JWTCookieHttpOnly;
    @Value("${server.servlet.jwt.cookie.secure}") boolean JWTCookieSecure;

    /**
     * Get token from authentication code and complete login
     * Create account if not exists
     * @param auth_source 0: Google
     * @param code authentication code
     */
    public void proceedAuth(User.AUTH_SOURCES auth_source, String code, HttpServletResponse response) throws Exception {
        String JWTToken = null;
        User newUser = new User();
        Connection userDatabase = null;
        String userId = null;

        try {
            // 1. Retrieve token from API server
            // 2. Check if user is already on database
            // 3. if not on database, add
            // 4. update user login time
            // 5. set jwt cookie
            if (auth_source == User.AUTH_SOURCES.GOOGLE) {
                logger.debug("Auth request via Google");

                JSONObject oidcResponse = googleOIDCService.getToken(code);
                JWTToken = oidcResponse.getString("id_token");
                DecodedJWT jwtDecoded = jwtService.decodeJWT(JWTToken);
                userId = jwtDecoded.getSubject();
                logger.debug("Received token from OIDC server");

                userDatabase = userRepository.openConnectionForEdit();

                if(!userRepository.queryUserExistence(userId, userDatabase)) {
                    logger.info("User not listed on database. Proceed user creation");
                    newUser.setAuthFrom(User.AUTH_SOURCES.GOOGLE);
                    newUser.setEmail(jwtDecoded.getClaim("email").asString());
                    newUser.setUserId(userId);
                    if(jwtDecoded.getClaim("name") != null) newUser.setUserName(jwtDecoded.getClaim("name").asString());
                    else newUser.setUserName("username");
                    newUser.setRefreshToken(oidcResponse.getString("refresh_token"));

                    userRepository.addUser(newUser, userDatabase);
                }
            }
            else if(auth_source == User.AUTH_SOURCES.KAKAO) {
                logger.debug("Auth request via Kakao");

                JSONObject oidcResponse = kakaoOIDCService.getToken(code);
                JSONObject userAccount = kakaoOIDCService.getUserData(oidcResponse.getString("access_token"));
                JWTToken = oidcResponse.getString("id_token");
                DecodedJWT jwtDecoded = jwtService.decodeJWT(JWTToken);
                userId = jwtDecoded.getSubject();
                logger.debug("Received token from OIDC server");

                userDatabase = userRepository.openConnectionForEdit();

                if(!userRepository.queryUserExistence(userId, userDatabase)) {
                    logger.info("User not listed on database. Proceed user creation");
                    newUser.setAuthFrom(User.AUTH_SOURCES.KAKAO);
                    newUser.setUserId(userId);
                    if(userAccount.getBoolean("profile_nickname_needs_agreement")) newUser.setUserName(userAccount.getString("nickname"));
                    else newUser.setUserName("");
                    if(userAccount.getBoolean("email_needs_agreement")) newUser.setEmail(userAccount.getString("email"));
                    else newUser.setEmail("");
                    newUser.setRefreshToken(oidcResponse.getString("refresh_token"));

                    userRepository.addUser(newUser, userDatabase);
                }
            }
            else {
                logger.error("Unknown OIDC provider: "+auth_source);
                throw new RuntimeException("Unknown OIDC provider.");
            }

            userRepository.updateUserLoginTime(userId, userDatabase);
            userRepository.commitAndClose(userDatabase);

            // set cookie
            Cookie cookie = new Cookie("lila-access", JWTToken);
            cookie.setHttpOnly(JWTCookieHttpOnly);
            cookie.setSecure(JWTCookieSecure);
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);

            logger.info("Auth completed of user " + userId);
        } catch (Exception e) {
            if(userDatabase != null) {
                logger.error("Failed to create user into database. Transaction was rollbacked.", e);
                userDatabase.rollback();
            }
            logger.error("Failed to create user into database. Connection is not initiated.", e);
            throw e;
        }
    }

    public void loadIdentityToModel(Model model, String jwtToken, HttpSession session) {
        if(jwtToken.equals("")) return;
    }
}
