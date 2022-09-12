package com.github.miho73.lila.controllers;

import com.github.miho73.lila.objects.User;
import com.github.miho73.lila.services.AuthService;
import com.github.miho73.lila.services.SessionService;
import com.github.miho73.lila.services.oauth.GoogleOAuthService;
import com.github.miho73.lila.services.oauth.KakaoOAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.http.HttpResponse;

@Slf4j
@Controller("AuthController")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    SessionService sessionService;

    @Autowired
    KakaoOAuthService kakaoOAuthService;
    @Autowired
    GoogleOAuthService googleOAuthService;

    @Autowired
    AuthService authService;

    @GetMapping("")
    public String login(Model model, HttpSession session) {
        sessionService.loadIdentity(model, session);
        return "auth/login";
    }

    @GetMapping("signout")
    public void signOut(HttpSession session, HttpServletResponse response) throws IOException {
        sessionService.invalidSession(session);
        response.sendRedirect("/");
    }

    @GetMapping("oauth/kakao")
    public void kakaoLogin(HttpSession session, HttpServletResponse response) throws IOException {
        String state = kakaoOAuthService.createStateCode();
        session.setAttribute("kakao_auth_state", state);
        response.sendRedirect(kakaoOAuthService.getAuthUri(state));
    }
    @GetMapping("oauth/google")
    public void googleLogin(HttpSession session, HttpServletResponse response) throws IOException {
        String state = googleOAuthService.createStateCode();
        session.setAttribute("google_auth_state", state);
        response.sendRedirect(googleOAuthService.getAuthUri(state));
    }

    @GetMapping("oauth/callback/kakao")
    public void kakaoOAuth(HttpSession session, HttpServletResponse response,
                           @RequestParam("code") String code,
                           @RequestParam("state") String state,
                           @RequestParam(value = "error", required = false, defaultValue = "") String error,
                           @RequestParam(value = "error_description", required = false, defaultValue = "") String errorMsg) throws IOException {
        if(!error.equals("")) {
            log.warn("kakao login canceled: "+error+". "+errorMsg);
            return;
        }

        int result = authService.proceedKakaoAuth(code, state, session);
        switch (result) {
            case 0 -> {
                response.sendRedirect("/");
            }
            case 1 -> {
                response.sendError(401);
            }
            case 2, 3, 4, 5 -> {
                response.sendError(500);
            }
        }
    }
    @GetMapping("oauth/callback/google")
    public void googleOAuth(HttpSession session, HttpServletResponse response,
                            @RequestParam("code") String code,
                            @RequestParam("state") String state,
                            @RequestParam(value = "error", required = false, defaultValue = "") String error) throws Exception {
        if(!error.equals("")) {
            log.warn("google login canceled: "+error+". "+error);
            return;
        }

        int result = authService.proceedGoogleAuth(code, state, session);
        switch (result) {
            case 0 -> {
                response.sendRedirect("/");
            }
            case 1 -> {
                response.sendError(401);
            }
            case 2, 3, 4, 5 -> {
                response.sendError(500);
            }
        }
    }
}
