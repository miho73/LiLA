package com.github.miho73.lila.controllers;

import com.github.miho73.lila.services.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller("MainController")
public class MainController {

    private String sitemap, robots;

    @Autowired
    SessionService sessionService;

    @PostConstruct
    public void initCommonControl() {
        ClassPathResource robotsResource = new ClassPathResource("etc/robots.txt");
        ClassPathResource sitemapResource = new ClassPathResource("etc/sitemap.xml");

        try {
            BufferedReader robotStream = new BufferedReader(new InputStreamReader(robotsResource.getInputStream(), StandardCharsets.UTF_8));
            BufferedReader sitemapStream = new BufferedReader(new InputStreamReader(sitemapResource.getInputStream(), StandardCharsets.UTF_8));
            this.robots = readBufferedReader(robotStream);
            this.sitemap = readBufferedReader(sitemapStream);
        } catch (IOException | RuntimeException e) {
            log.error("failed to read file.", e);
            throw new RuntimeException(e);
        }
    }
    private String readBufferedReader(BufferedReader reader) throws IOException {
        StringBuilder stringBuffer = new StringBuilder();
        String temp;
        while ((temp = reader.readLine()) != null) {
            stringBuffer.append(temp)
                        .append("\n");
        }
        return stringBuffer.toString();
    }

    @GetMapping("")
    public String index(Model model,
                        HttpServletResponse response, HttpSession session) {
        sessionService.loadIdentity(model, session);
        return "index";
    }

    @GetMapping(value = "robots.txt", produces = "text/plain")
    @ResponseBody
    public String robots() {
        return robots;
    }

    @GetMapping(value = "sitemap.xml", produces = "application/xml")
    @ResponseBody
    public String sitemap(HttpServletResponse response) {
        return sitemap;
    }
}
