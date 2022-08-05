package com.github.miho73.lila.controllers;

import com.github.miho73.lila.objects.User;
import com.github.miho73.lila.services.AuthService;
import com.github.miho73.lila.services.JWTService;
import com.github.miho73.lila.services.oidc.GoogleOIDCService;
import com.github.miho73.lila.services.oidc.KakaoOIDCService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@Controller("AuthController")
@RequestMapping("/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired GoogleOIDCService googleOIDCService;
    @Autowired KakaoOIDCService kakaoOIDCService;
    @Autowired JWTService jwtService;
    @Autowired AuthService authService;

    @GetMapping("")
    public String login(Model model) {
        return "auth/login";
    }

    @GetMapping("oidc/google")
    public String googleOIDC(HttpSession session) {
        String state = googleOIDCService.createStateCode();
        session.setAttribute("google_auth_state", state);
        return "redirect:"+googleOIDCService.getAuthUri(state);
    }
    @GetMapping("oidc/callback/google")
    public void googleOIDCCallback(HttpSession session, HttpServletResponse response, HttpServletRequest request,
                                   @RequestParam("code") String code,
                                   @RequestParam("state") String state) throws Exception {
        // Check state
        if(session.getAttribute("google_auth_state") == null) {
            response.sendError(401);
            return;
        }
        String stateSession = session.getAttribute("google_auth_state").toString();
        session.removeAttribute("google_auth_state");

        if(state.equals(stateSession)) {
            authService.proceedAuth(User.AUTH_SOURCES.GOOGLE, code, response);
            response.sendRedirect("/");
        }
        else {
            response.sendError(401);
        }
    }

    @GetMapping("oidc/kakao")
    public String kakaoOIDC(HttpSession session) {
        String state = kakaoOIDCService.createStateCode();
        session.setAttribute("kakao_auth_state", state);
        return "redirect:"+kakaoOIDCService.getAuthUri(state);
    }
    @GetMapping("oidc/callback/kakao")
    public void kakaoOIDCCallback(HttpSession session, HttpServletResponse response, HttpServletRequest request,
                                   @RequestParam("code") String code,
                                   @RequestParam("state") String state) throws Exception {
        // Check state
        if(session.getAttribute("kakao_auth_state") == null) {
            response.sendError(401);
            return;
        }
        String stateSession = session.getAttribute("kakao_auth_state").toString();
        session.removeAttribute("kakao_auth_state");

        if(state.equals(stateSession)) {
            authService.proceedAuth(User.AUTH_SOURCES.KAKAO, code, response);
            response.sendRedirect("/");
        }
        else {
            response.sendError(401);
        }
    }

    @GetMapping("/verify")
    @ResponseBody
    public String verifyJwtToken(@CookieValue(value = "lila-access", required = false, defaultValue = "") String jwt) {
        if(jwtService.verifyGoogleToken(jwt)) {
            return "your token is valid!\n\n";
        }
        else {
            return "your token is invalid!\n\n";
        }
    }
}
