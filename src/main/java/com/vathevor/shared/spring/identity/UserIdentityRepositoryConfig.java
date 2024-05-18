package com.vathevor.shared.spring.identity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class UserIdentityRepositoryConfig {

    @ConditionalOnMissingBean(UserIdentityRepository.class)
    @Bean
    public UserIdentityRepository userIdentityRepository() {
        log.warn("Initializing InMemoryUserIdentityRepository due to no other UserIdentityRepository");
        return new InMemoryUserIdentityRepository();
    }
}
