package com.vathevor.shared.spring.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.vathevor.shared.spring.identity")
public class UserIdResolverConfig implements WebMvcConfigurer {

    private final UserIdResolver authenticatedUserIdResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserIdResolver);
    }
}
