package com.vathevor.project.linklake.shared;

import com.vathevor.shared.spring.identity.UserIdentity;
import com.vathevor.shared.spring.identity.UserIdentityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;

import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_2_ID;
import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_ID;

@JdbcTest
@Import(JdbcUserIdentityRepository.class)
public abstract class BaseJdbcRepositoryTest {

    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Autowired
    protected JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        insertUser();
    }

    protected void insertUser() {
        userIdentityRepository.save(new UserIdentity(USER_ID, "dummyIdpSub"));
        userIdentityRepository.save(new UserIdentity(USER_2_ID, "dummyIdpSubUser2"));
    }
}
