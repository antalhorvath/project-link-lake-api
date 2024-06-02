package com.vathevor.project.linklake.shared.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vathevor.shared.spring.converter.ShortUUIDDeserializer;
import com.vathevor.shared.spring.converter.ShortUUIDSerializer;
import com.vathevor.shared.util.ShortUUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public SimpleModule simpleModule() {
        return new SimpleModule()
                .addDeserializer(ShortUUID.class, new ShortUUIDDeserializer())
                .addSerializer(ShortUUID.class, new ShortUUIDSerializer());
    }

    @Bean
    public ObjectMapper objectMapper(SimpleModule simpleModule) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
}
