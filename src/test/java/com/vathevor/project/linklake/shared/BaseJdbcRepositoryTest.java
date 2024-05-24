package com.vathevor.project.linklake.shared;

import com.vathevor.shared.spring.identity.UserIdentity;
import com.vathevor.shared.spring.identity.UserIdentityRepository;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;

@JdbcTest
@Import(JdbcUserIdentityRepository.class)
public abstract class BaseJdbcRepositoryTest {

    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Autowired
    protected JdbcClient jdbcClient;

    protected ShortUUID userId;

    @BeforeEach
    void setUp() {
        userId = ShortUUID.randomUUID();
        userIdentityRepository.save(new UserIdentity(userId, "dummyIdpSub"));
    }
}
