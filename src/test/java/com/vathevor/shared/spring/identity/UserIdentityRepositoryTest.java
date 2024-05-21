package com.vathevor.shared.spring.identity;

import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class UserIdentityRepositoryTest {

    UserIdentityRepository repository;

    @BeforeEach
    void setUp() {
        repository = createRepository();
    }

    protected abstract UserIdentityRepository createRepository();

    @Test
    void does_not_find_user_identity_of_non_existing_idpSub() {
        Optional<UserIdentity> userIdentityByIdpSub = repository.findUserIdentityByIdpSub("newUserSub");
        assertThat(userIdentityByIdpSub).isEmpty();
    }

    @Test
    void finds_saved_user_identity() {
        var userIdentity = new UserIdentity(ShortUUID.randomUUID(), "dummySub");
        repository.save(userIdentity);

        Optional<UserIdentity> userIdentityByIdpSub = repository.findUserIdentityByIdpSub("dummySub");
        assertThat(userIdentityByIdpSub)
                .isPresent()
                .contains(userIdentity);
    }
}
