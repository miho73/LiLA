package com.github.miho73.lila.services;

import com.github.miho73.lila.Repositories.UserRepository;
import com.github.miho73.lila.objects.User;
import com.github.miho73.lila.services.oauth.GoogleOAuthService;
import com.github.miho73.lila.services.oauth.KakaoOAuthService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Service("AuthService")
public class AuthService {

    @Autowired
    KakaoOAuthService kakaoOAuthService;
    @Autowired
    GoogleOAuthService googleOAuthService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionService sessionService;

    /**
     * complete authentication via kakao
     * @param code authorization code
     * @param state state of request
     * @param session session of user
     * @return 0: success / 1: state error / 2: get token error / 3: get user data error / 4: user creation error / 5: session error
     */
    public int proceedKakaoAuth(String code, String state, HttpSession session) {
        // 1. state check
        if(session.getAttribute("kakao_auth_state") == null) {
            log.warn("state not found. kakao login canceled");
            return 1;
        }

        String stateSession = session.getAttribute("kakao_auth_state").toString();
        session.removeAttribute("kakao_auth_state");
        if(!state.equals(stateSession)) {
            log.warn("invalid state. kakao login canceled");
            return 1;
        }

        // 2. get token
        String accessToken, refreshToken;
        try {
            String[] tokens = kakaoOAuthService.getToken(code);
            accessToken = tokens[0];
            refreshToken = tokens[1];
        } catch (Exception e) {
            log.error("failed to retrieve access token from kakao.", e);
            return 2;
        }

        // 3. get user data
        User newUser = new User();
        String uid;
        try {
            JSONObject userData = kakaoOAuthService.getUserData(accessToken);
            JSONObject userAccount = userData.getJSONObject("kakao_account");

            newUser.setAuthFrom(User.AUTH_SOURCES.KAKAO);
            newUser.setUserId(userData.getString("id"));
            uid = userData.getString("id");

            if(!userAccount.getBoolean("profile_nickname_needs_agreement")) {
                newUser.setUserName(
                        userAccount.getJSONObject("profile")
                                   .getString("nickname")
                );
            }
            else newUser.setUserName("");

            if(!userAccount.getBoolean("email_needs_agreement") && userAccount.getBoolean("has_email")) {
                newUser.setEmail(userAccount.getString("email"));
            }
            else newUser.setEmail("");

            newUser.setRefreshToken(refreshToken);
        } catch (Exception e) {
            log.error("failed to get user data from kakao.", e);
            return 3;
        }

        // 4. create user
        try {
            proceedUserCreation(newUser);
        } catch (Exception e) {
            log.error("failed to create user via kakao", e);
            return 4;
        }

        // 5. set session
        try {
            completeSignIn(uid, session);
            return 0;
        } catch (SQLException e) {
            log.error("failed to set session for login(kakao)", e);
            return 5;
        }
    }

    /**
     * complete authentication via google
     * @param code authorization code
     * @param state state of request
     * @param session session of user
     * @return 0: success / 1: state error / 2: get token error / 3: get user data error / 4: user creation error / 5: session error
     */
    public int proceedGoogleAuth(String code, String state, HttpSession session) throws Exception {
        // 1. state check
        if(session.getAttribute("google_auth_state") == null) {
            log.warn("state not found. google login canceled");
            return 1;
        }

        String stateSession = session.getAttribute("google_auth_state").toString();
        session.removeAttribute("google_auth_state");
        if(!state.equals(stateSession)) {
            log.warn("invalid state. google login canceled");
            return 1;
        }

        // 2. get token
        String[] tokens;
        String accessToken, refreshToken;
        try {
            tokens = googleOAuthService.getToken(code);
            accessToken = tokens[0];
            refreshToken = tokens[1];
        } catch (Exception e) {
            log.error("Failed to retrieve access token from google", e);
            return 2;
        }

        // 3. get user data
        User newUser = new User();
        String uid;
        try {
            JSONObject userData = googleOAuthService.getUserData(accessToken);
            newUser.setAuthFrom(User.AUTH_SOURCES.GOOGLE);
            newUser.setUserId(userData.getString("id"));
            newUser.setUserName(userData.getString("name"));
            newUser.setEmail(userData.getString("email"));
            newUser.setRefreshToken(refreshToken);
            uid = userData.getString("id");
        } catch (Exception e) {
            log.error("Failed to get user data from google");
            return 3;
        }

        // 4. create user
        try {
            proceedUserCreation(newUser);
        } catch (Exception e) {
            log.error("failed to create user via google", e);
            return 4;
        }

        // 5. set session
        try {
            completeSignIn(uid, session);
            return 0;
        } catch (SQLException e) {
            log.error("failed to set session for login(kakao)", e);
            return 5;
        }
    }

    /**
     * create user to database if not exists
     */
    public void proceedUserCreation(User user) throws SQLException {
        Connection connection = userRepository.openConnectionForEdit();
        if(!userRepository.queryUserExistence(user.getUserId(), connection)) {
            try {
                userRepository.addUser(user, connection);
                userRepository.commit(connection);
            }
            catch (Exception e) {
                log.error("Failed to add user to database", e);
                userRepository.rollback(connection);
            }
        }
        userRepository.close(connection);
    }

    public void completeSignIn(String id, HttpSession session) throws SQLException {
        Connection connection = userRepository.openConnection();
        User user = userRepository.queryUser(id, connection);
        userRepository.close(connection);

        sessionService.initLoginSession(session, user.getUserId(), user.getUserName(), user.getPrivilege());
    }
}
