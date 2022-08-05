package com.github.miho73.lila.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Service("SessionService")
public class SessionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public void initLoginSession(HttpSession session, String uid, String uname, String privilege) {
        session.setAttribute("log", true);
        session.setAttribute("uid", uid);
        session.setAttribute("nam", uname);
        session.setAttribute("pri", privilege);
    }

    public boolean checkLogin(HttpSession session) {
        if(session == null) return false;
        Object logged = session.getAttribute("log");
        if(logged == null) return false;
        return (boolean)logged;
    }

    public String getId(HttpSession session) {
        return (String)session.getAttribute("uid");
    }
    public String getPrivilege(HttpSession session) {
        return (String)session.getAttribute("pri");
    }
    public void setName(HttpSession session, String name) {
        session.setAttribute("nam", name);
    }
    public String getName(HttpSession session) {
        return (String)session.getAttribute("nam");
    }

    public void invalidSession(HttpSession session) {
        logger.info("Session invalidated. ID="+getId(session));
        session.invalidate();
    }

    public void loadIdentity(Model model, HttpSession session) {
        if(checkLogin(session)) {
            model.addAllAttributes(Map.of(
                    "login", true,
                    "username", getName(session)
            ));
        }
        else {
            model.addAllAttributes(Map.of(
                    "login", false
            ));
        }
    }
}
