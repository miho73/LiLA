package com.github.miho73.lila.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

@Controller("MainController")
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String sitemap, robots;

    @PostConstruct
    public void initCommonControl() {
        ClassPathResource robotsResource = new ClassPathResource("static/etc/robots.txt");
        ClassPathResource sitemapResource = new ClassPathResource("static/etc/sitemap.xml");

        try {
            this.robots = String.join("\n", Files.readAllLines(Paths.get(robotsResource.getURI())));
            this.sitemap= String.join("\n", Files.readAllLines(Paths.get(sitemapResource.getURI())));
        } catch (IOException | RuntimeException e) {
            logger.error("Cannot read file.", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("")
    public String index() {
        return "index.html";
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
