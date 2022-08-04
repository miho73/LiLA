package com.github.miho73.lila.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.miho73.lila.Repositories.UserRepository;
import com.github.miho73.lila.objects.User;
import com.github.miho73.lila.services.oidc.GoogleOIDCService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired GoogleOIDCService googleOIDCService;
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
    public void proceedAuth(User.AUTH_SOURCES auth_source, String code, HttpServletResponse response) throws SQLException {
        String JWTToken = null;
        User newUser = new User();
        Connection userDatabase = null;

        try {
            if (auth_source == User.AUTH_SOURCES.GOOGLE) {
                JSONObject oidcResponse = googleOIDCService.getToken(code);
                JWTToken = oidcResponse.getString("id_token");
                DecodedJWT jwtDecoded = jwtService.decodeJWT(JWTToken);

                newUser.setAuthFrom(User.AUTH_SOURCES.GOOGLE);
                newUser.setEmail(jwtDecoded.getClaim("email").asString());
                newUser.setUserId(jwtDecoded.getSubject());
                newUser.setUserName(jwtDecoded.getClaim("name").asString());
                newUser.setRefreshToken(oidcResponse.getString("refresh_token"));

                userDatabase = userRepository.openConnectionForEdit();
                userRepository.addUser(newUser, userDatabase);
                userRepository.commitAndClose(userDatabase);
            }

            // set cookie
            Cookie cookie = new Cookie("lila-access", JWTToken);
            cookie.setHttpOnly(JWTCookieHttpOnly);
            cookie.setSecure(JWTCookieSecure);
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        } catch (Exception e) {
            if(userDatabase != null) {
                logger.error("Failed to create user into database. Transaction was rollback.", e);
                userDatabase.rollback();
            }
            logger.error("Failed to create user into database. Connection is not initiated.", e);
            throw e;
        }
    }
}
