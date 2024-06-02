package com.vathevor.project.linklake.query.application.link;

import com.vathevor.project.linklake.query.domain.link.LinkQuery;
import com.vathevor.project.linklake.query.domain.link.LinkQueryService;
import com.vathevor.project.linklake.query.domain.link.LinkView;
import com.vathevor.shared.spring.identity.UserIdentity;
import com.vathevor.shared.spring.identity.UserIdentityRepository;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("feature")
@WebMvcTest(controllers = LinkQueryController.class)
@AutoConfigureMockMvc
class LinkQueryControllerTest {

    @MockBean
    UserIdentityRepository userIdentityRepository;

    @MockBean
    LinkQueryService linkQueryService;

    @Autowired
    MockMvc mockMvc;

    UserIdentity userIdentity;

    SecurityMockMvcRequestPostProcessors.OAuth2LoginRequestPostProcessor oauth2Login;

    @BeforeEach
    void setUp() {
        userIdentity = new UserIdentity(ShortUUID.randomUUID(), "dummySub");

        when(userIdentityRepository.findUserIdentityByIdpSub(userIdentity.idpSub()))
                .thenReturn(Optional.of(userIdentity));

        oauth2Login = SecurityMockMvcRequestPostProcessors.oauth2Login()
                .attributes(attr -> attr.put("sub", userIdentity.idpSub()));
    }

    @Test
    void queries_links_of_user() throws Exception {
        when(linkQueryService.queryLinks(new LinkQuery(userIdentity.userId())))
                .thenReturn(List.of(
                        LinkView.builder()
                                .linkId(ShortUUID.fromString("e5bc7046193042e7b6fd561ac425d312"))
                                .name("user1 link2")
                                .link("https://link2.com")
                                .build(),
                        LinkView.builder()
                                .linkId(ShortUUID.fromString("9231bc7b95d84c50b1499b8a548204e3"))
                                .name("user1 link1")
                                .link("https://link1.com")
                                .build()
                ));

        mockMvc.perform(
                        get("/links")
                                .with(oauth2Login)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {
                                "linkId": "e5bc7046193042e7b6fd561ac425d312",
                                "name": "user1 link2",
                                "link": "https://link2.com"
                            },
                            {
                                "linkId": "9231bc7b95d84c50b1499b8a548204e3",
                                "name": "user1 link1",
                                "link": "https://link1.com"
                            }
                        ]
                        """));
    }
}
