package com.example.springapi.api.config;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * A class to auto configure Object Mapper to handle JTS/Geometry types
 *
@Configuration
public class JacksonJtsConfig {

    @Bean
    public Module jtsModule() {
        return new JtsModule();
    }
}
*/