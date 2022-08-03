package com.github.miho73.lila.services;

import com.github.miho73.lila.objects.User;
import com.github.miho73.lila.services.oidc.GoogleOIDCService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired GoogleOIDCService googleOIDCService;

    /**
     * Get token from authentication code and complete login
     * Create account if not exists
     * @param auth_source 0: Google
     * @param code authentication code
     */
    public void proceedAuth(int auth_source, String code, HttpServletResponse response) {
        if (auth_source == 0) {
            JSONObject oidcResponse = googleOIDCService.getToken(code);

            // set cookie
            Cookie cookie = new Cookie("lila-access", oidcResponse.getString("id_token"));
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        }
    }
}
