package org.App;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("org.controller")
@ComponentScan("org.configurator")
@ComponentScan("org.dispatcher")
@ComponentScan("org.userService")
@ComponentScan("org.eventListener")
@ComponentScan("org.sessionUtil")
@EnableScheduling
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Main.class,args);
        logger.info("application started");
    }
}