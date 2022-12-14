package com.github.miho73.lila;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})

@EnableRedisHttpSession
@SpringBootApplication
public class Lila {
    public static void main(String[] args) {
        SpringApplication.run(Lila.class, args);
    }
}
