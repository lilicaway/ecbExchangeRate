package com.liliana.sample.exchange.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Configuration that takes care of loading the beans from the controller
 * package
 */
@Configuration
@ComponentScan
@EnableWebMvc
public class ControllersTestConfiguration {
}
