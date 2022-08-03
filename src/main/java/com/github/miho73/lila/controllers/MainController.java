package com.github.miho73.lila.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller("MainController")
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String sitemap, robots;

    @PostConstruct
    public void initCommonControl() {
        ClassPathResource robotsResource = new ClassPathResource("etc/robots.txt");
        ClassPathResource sitemapResource = new ClassPathResource("etc/sitemap.xml");

        try {
            this.robots = String.join("\n", Files.readAllLines(Paths.get(robotsResource.getURI())));
            this.sitemap= String.join("\n", Files.readAllLines(Paths.get(sitemapResource.getURI())));
        } catch (IOException | RuntimeException e) {
            logger.error("Cannot read file.", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("")
    public String index(HttpServletResponse response) {
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
