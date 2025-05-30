package com.galaxe.gxworkflow.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableWebMvc
@Configuration
@ComponentScan("com.galaxe.gxworkflow")
public class CorsRegistryConfig implements WebMvcConfigurer{

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("*").allowedOrigins("*").maxAge(4800);
	}
}
