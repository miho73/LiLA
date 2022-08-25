package com.github.miho73.lila.controllers;

import com.github.miho73.lila.objects.Exception.LiLACParsingException;
import com.github.miho73.lila.utils.LiLACRenderer;
import com.github.miho73.lila.services.SessionService;
import com.github.miho73.lila.utils.RestfulResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping("/create")
    public String createProblem(Model model, HttpSession session) {
        sessionService.loadIdentity(model, session);
        model.addAttribute("newProblem", true);
        return "problem/problemSettings";
    }

    @GetMapping("lilac/preview")
    public String previewLilac() {
        return "problem/lilacPreview";
    }

    @PostMapping(
            value = "/lilac/compile",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public String compileLilac(@RequestBody Map<String,String> requestBody) {
        try {
            if(requestBody.get("lilac") == null) {
                return RestfulResponse.responseMessage(HttpStatus.BAD_REQUEST, "Given LiLAC code is null");
            }
            String html = LiLACRenderer.render(requestBody.get("lilac"));
            return RestfulResponse.responseResult(HttpStatus.OK, html);
        } catch (LiLACParsingException e) {
            return RestfulResponse.responseMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
