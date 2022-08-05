package com.github.miho73.lila.controllers;

import com.github.miho73.lila.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller("ProblemController")
@RequestMapping("/problems")
public class ProblemController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SessionService sessionService;

    @GetMapping("")
    public String problem(Model model, HttpSession session) {
        sessionService.loadIdentity(model, session);
        return "problem/problemList";
    }
}
