package com.github.miho73.lila.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Service("SessionService")
public class SessionService {

    public void initLoginSession(HttpSession session, String uid, String uname, String privilege) {
        session.setAttribute("log", true);
        session.setAttribute("uid", uid);
        session.setAttribute("nam", uname);
        session.setAttribute("pri", privilege);
    }

    /**
     * check if account is logged in
     *
     * @param session session to test
     * @return true when session is logged in
     */
    public boolean checkLogin(HttpSession session) {
        if (session == null) return false;
        Object logged = session.getAttribute("log");
        if (logged == null) return false;
        return (boolean) logged;
    }

    public enum PRIVILEGE {
        USER,
        PROBLEM_EDITOR,
        ISSUE_TRACKER,
        ROOT
    }

    /**
     * check if session has ONE OF given privilege
     *
     * @param session session to test
     * @return true if session has sufficient privilege
     */
    public boolean checkPrivilege(HttpSession session, PRIVILEGE... privilege) {
        if (!checkLogin(session)) return false;

        String hasPrivilege = getPrivilege(session);

        if (hasPrivilege.contains("r")) return true;

        for (PRIVILEGE priv : privilege) {
            switch (priv) {
                case USER -> {
                    if (hasPrivilege.contains("u")) return true;
                }
                case PROBLEM_EDITOR -> {
                    if (hasPrivilege.contains("p")) return true;
                }
                case ISSUE_TRACKER -> {
                    if (hasPrivilege.contains("i")) return true;
                }
            }
        }
        return false;
    }

    public String getId(HttpSession session) {
        return (String) session.getAttribute("uid");
    }

    public String getPrivilege(HttpSession session) {
        return (String) session.getAttribute("pri");
    }

    public void setName(HttpSession session, String name) {
        session.setAttribute("nam", name);
    }

    public String getName(HttpSession session) {
        return (String) session.getAttribute("nam");
    }

    public void invalidSession(HttpSession session) {
        log.info("Session invalidated. ID=" + getId(session));
        session.invalidate();
    }

    public void loadIdentity(Model model, HttpSession session) {
        if (checkLogin(session)) {
            model.addAllAttributes(Map.of(
                    "login", true,
                    "username", getName(session)
            ));
        } else {
            model.addAllAttributes(Map.of(
                    "login", false
            ));
        }
    }
}
