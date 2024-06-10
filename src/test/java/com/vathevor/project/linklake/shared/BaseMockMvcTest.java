package com.vathevor.project.linklake.shared;

import com.vathevor.shared.spring.identity.UserIdentity;
import com.vathevor.shared.spring.identity.UserIdentityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_1_ID;
import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_1_IDP_SUB;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
public abstract class BaseMockMvcTest {

    @MockBean
    UserIdentityRepository userIdentityRepository;

    @Autowired
    protected MockMvc mockMvc;

    protected UserIdentity userIdentity;

    protected SecurityMockMvcRequestPostProcessors.OAuth2LoginRequestPostProcessor oauth2Login;

    @BeforeEach
    void setUp() {
        userIdentity = new UserIdentity(USER_1_ID, USER_1_IDP_SUB);

        when(userIdentityRepository.findUserIdentityByIdpSub(userIdentity.idpSub()))
                .thenReturn(Optional.of(userIdentity));

        oauth2Login = SecurityMockMvcRequestPostProcessors.oauth2Login()
                .attributes(attr -> attr.put("sub", userIdentity.idpSub()));
    }
}
