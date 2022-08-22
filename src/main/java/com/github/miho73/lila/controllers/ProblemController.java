package com.github.miho73.lila.controllers;

import com.github.miho73.lila.objects.Exception.LiLACParsingException;
import com.github.miho73.lila.services.LiLACRenderer;
import com.github.miho73.lila.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller("ProblemController")
@RequestMapping("/problems")
public class ProblemController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SessionService sessionService;

    @GetMapping("")
    public String problem(Model model, HttpSession session) {
        sessionService.loadIdentity(model, session);
        model.addAttribute("page", 0);
        return "problem/problemList";
    }

    @GetMapping("lilac/preview")
    public String previewLilac() {
        return "problem/lilacPreview";
    }
    @PostMapping(value = "/lilac/compile", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> compileLilac(@RequestBody Map<String,String> requestBody) {
        try {
            String html = LiLACRenderer.render(requestBody.get("lilac"));
            return ResponseEntity.ok().body(html);
        } catch (LiLACParsingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
