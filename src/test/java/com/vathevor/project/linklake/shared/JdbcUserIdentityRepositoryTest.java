package com.vathevor.project.linklake.shared;

import com.vathevor.project.linklake.TestJdbcClientConfig;
import com.vathevor.shared.spring.identity.UserIdentity;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = {
        JdbcUserIdentityRepository.class,
        TestJdbcClientConfig.class // TODO - Spring JdbcClient should be autoconfigured, why not working?
})
class JdbcUserIdentityRepositoryTest {

    @Autowired
    JdbcUserIdentityRepository repository;

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