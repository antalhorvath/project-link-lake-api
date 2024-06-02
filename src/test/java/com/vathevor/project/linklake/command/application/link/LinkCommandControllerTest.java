package com.vathevor.project.linklake.command.application.link;

import com.vathevor.project.linklake.command.domain.link.LinkCommandService;
import com.vathevor.project.linklake.command.domain.link.LinkEntity;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.OAuth2LoginRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("feature")
@WebMvcTest(controllers = LinkCommandController.class)
@AutoConfigureMockMvc
class LinkCommandControllerTest {

    @MockBean
    UserIdentityRepository userIdentityRepository;

    @MockBean
    LinkCommandService linkCommandService;

    @Autowired
    MockMvc mockMvc;

    UserIdentity userIdentity;

    OAuth2LoginRequestPostProcessor oauth2Login;

    @BeforeEach
    void setUp() {
        userIdentity = new UserIdentity(ShortUUID.randomUUID(), "dummySub");

        when(userIdentityRepository.findUserIdentityByIdpSub(userIdentity.idpSub()))
                .thenReturn(Optional.of(userIdentity));

        oauth2Login = SecurityMockMvcRequestPostProcessors.oauth2Login()
                .attributes(attr -> attr.put("sub", userIdentity.idpSub()));
    }

    @Test
    void saves_link() throws Exception {
        LinkEntity link = LinkEntity.builder()
                .linkId(ShortUUID.randomUUID())
                .userId(userIdentity.userId())
                .name("example")
                .link("https://example.com")
                .modifiedAt(LocalDate.now())
                .build();

        mockMvc.perform(
                        put("/links/" + link.linkId().value())
                                .with(oauth2Login)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "example",
                                            "link": "https://example.com"
                                        }
                                        """)
                )
                .andExpect(status().isNoContent());

        verify(linkCommandService).save(link);
    }

    @Test
    void responds_with_bad_request() throws Exception {
        mockMvc.perform(
                        put("/links/" + ShortUUID.randomUUID().value())
                                .with(oauth2Login)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "",
                                            "link": ""
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(linkCommandService);
    }
}
