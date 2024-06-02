package com.vathevor.project.linklake.shared;

import com.vathevor.shared.spring.identity.UserIdentityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;

import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_1;
import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_2;

@JdbcTest
@Import(JdbcUserIdentityRepository.class)
public abstract class BaseJdbcRepositoryTest {

    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Autowired
    protected JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        insertUsers();
    }

    protected void insertUsers() {
        userIdentityRepository.save(USER_1);
        userIdentityRepository.save(USER_2);
    }
}
