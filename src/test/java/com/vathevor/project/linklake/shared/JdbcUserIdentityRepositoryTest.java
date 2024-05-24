package com.vathevor.project.linklake.shared;

import com.vathevor.shared.spring.identity.UserIdentityRepository;
import com.vathevor.shared.spring.identity.UserIdentityRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

@JdbcTest
@ContextConfiguration(classes = {
        JdbcUserIdentityRepository.class
})
class JdbcUserIdentityRepositoryTest extends UserIdentityRepositoryTest {

    @Autowired
    JdbcUserIdentityRepository repository;

    @Override
    protected UserIdentityRepository createRepository() {
        return repository;
    }
}
