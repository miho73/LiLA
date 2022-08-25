package com.github.miho73.lila.controllers;

import com.github.miho73.lila.objects.Exception.LiLACParsingException;
import com.github.miho73.lila.objects.Problem;
import com.github.miho73.lila.services.ProblemService;
import com.github.miho73.lila.utils.LiLACRenderer;
import com.github.miho73.lila.services.SessionService;
import com.github.miho73.lila.utils.RestfulResponse;
import com.github.miho73.lila.utils.Verifiers;
import org.json.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.Map;

@Controller("ProblemController")
@RequestMapping("/problems")
public class ProblemController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SessionService sessionService;

    @Autowired
    ProblemService problemService;

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
    @PostMapping(
            value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseBody
    public String createProblemPost(@RequestBody Map<String, Object> requestBody) {
        try {
            Problem problem = new Problem();
            problem.setName(requestBody.get("problem_name").toString());
            problem.setTag((int)requestBody.get("tags"));
            problem.setBranch((int)requestBody.get("branch")-1);
            problem.setDifficulty((int)requestBody.get("difficulty")-1);
            problem.setContent(requestBody.get("content").toString());
            problem.setSolution(requestBody.get("solution").toString());

            //TODO: get client data
            problem.setAnswer("");
            problem.setStatus(Problem.PROBLEM_STATUS.CORRECTING);

            // Verify parameters
            if(!Verifiers.inRange(problem.getName().length(), 50, 1)) {
                logger.warn("Cannot create problem: problem name length out of bound");
                return RestfulResponse.responseMessage(HttpStatus.BAD_REQUEST, "Problem name has illegal length");
            }

            problemService.createProblem(problem);
        } catch (NullPointerException e) {
            logger.error("Cannot create problem", e);
            return RestfulResponse.responseMessage(HttpStatus.BAD_REQUEST, "Missing parameter(s)");
        } catch (ClassCastException e) {
            logger.warn("Cannot create problem", e);
            return RestfulResponse.responseMessage(HttpStatus.BAD_REQUEST, "Unexpected parameter type");
        } catch (IllegalStateException e) {
            logger.warn("Cannot create problem", e);
            return RestfulResponse.responseMessage(HttpStatus.BAD_REQUEST, "Illegal state for branch and/or difficulty");
        } catch (SQLException e) {
            logger.warn("Cannot create problem", e);
            return RestfulResponse.responseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
        return RestfulResponse.responseResult(HttpStatus.CREATED, problemService.PROBLEM_COUNT);
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


    @GetMapping("lilac/preview")
    public String previewLilac() {
        return "problem/lilacPreview";
    }
}
