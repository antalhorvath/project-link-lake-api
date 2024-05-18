package com.vathevor.shared.spring.identity;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryUserIdentityRepositoryTest {

    InMemoryUserIdentityRepository repository = new InMemoryUserIdentityRepository();

    @Test
    void does_not_find_user_identity_of_non_existing_idpSub() {
        Optional<UserIdentity> userIdentityByIdpSub = repository.findUserIdentityByIdpSub("newUserSub");
        assertThat(userIdentityByIdpSub).isEmpty();
    }

    @Test
    void finds_saved_user_identity() {
        var userIdentity = new UserIdentity(UUID.randomUUID(), "dummySub");
        repository.save(userIdentity);

        Optional<UserIdentity> userIdentityByIdpSub = repository.findUserIdentityByIdpSub("dummySub");
        assertThat(userIdentityByIdpSub)
                .isPresent()
                .contains(userIdentity);
    }
}
