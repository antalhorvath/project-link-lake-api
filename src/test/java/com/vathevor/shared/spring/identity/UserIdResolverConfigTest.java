package com.vathevor.shared.spring.identity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserIdResolverConfigTest {

    @SpringBootTest(classes = UserIdResolverConfig.class)
    @Nested
    class DefaultConfig {

        @Autowired
        UserIdentityRepository repository;

        @Test
        void autowires_InMemoryUserIdentityRepository() {
            assertThat(repository).isInstanceOf(InMemoryUserIdentityRepository.class);
        }
    }

    static class CustomUserIdentityRepository implements UserIdentityRepository {

        @Override
        public Optional<UserIdentity> findUserIdentityByIdpSub(String idpSub) {
            return Optional.empty();
        }

        @Override
        public void save(UserIdentity userIdentity) {

        }
    }

    @SpringBootTest(classes = {UserIdResolverConfig.class, CustomUserIdentityRepository.class})
    @Nested
    class CustomUserIdentityConfig {

        @Autowired
        UserIdentityRepository repository;

        @Test
        void autowires_CustomUserIdentityRepository() {
            assertThat(repository).isInstanceOf(CustomUserIdentityRepository.class);
        }
    }
}
