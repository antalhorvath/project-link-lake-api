package com.vathevor.project.linklake.command.application.tag;

import com.vathevor.project.linklake.command.domain.tag.TagResourceCommandService;
import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.project.linklake.shared.SharedTestConstants;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.vathevor.project.linklake.command.domain.tag.TagTestConstants.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("feature")
@WebMvcTest(controllers = TaggedResourceCommandController.class)
@AutoConfigureMockMvc
class TaggedResourceCommandControllerTest {

    @MockBean
    UserIdentityRepository userIdentityRepository;

    @MockBean
    TagResourceCommandService tagResourceCommandService;

    @Autowired
    MockMvc mockMvc;

    UserIdentity userIdentity;

    SecurityMockMvcRequestPostProcessors.OAuth2LoginRequestPostProcessor oauth2Login;

    @BeforeEach
    void setUp() {
        userIdentity = new UserIdentity(SharedTestConstants.USER_ID, "dummySub");

        when(userIdentityRepository.findUserIdentityByIdpSub(userIdentity.idpSub()))
                .thenReturn(Optional.of(userIdentity));

        oauth2Login = SecurityMockMvcRequestPostProcessors.oauth2Login()
                .attributes(attr -> attr.put("sub", userIdentity.idpSub()));
    }

    @Test
    void saves_tagged_resource() throws Exception {
        TaggedResourceEntity taggedResource = TaggedResourceEntity.builder()
                .resourceId(ShortUUID.randomUUID())
                .userId(SharedTestConstants.USER_ID)
                .name("resource name")
                .tag(TAG_1)
                .tag(TAG_2)
                .build();

        mockMvc.perform(
                        put("/resources/" + taggedResource.resourceId().value())
                                .with(oauth2Login)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "resource name",
                                            "tags": [
                                              {
                                                "tagId": "${TAG_1_ID}",
                                                "name": "${TAG_1_NAME}"
                                              },
                                              {
                                                "tagId": "${TAG_2_ID}",
                                                "name": "${TAG_2_NAME}"
                                              }
                                            ]
                                        }
                                        """
                                        .replace("${TAG_1_ID}", TAG_1_ID.value())
                                        .replace("${TAG_1_NAME}", TAG_1_NAME)
                                        .replace("${TAG_2_ID}", TAG_2_ID.value())
                                        .replace("${TAG_2_NAME}", TAG_2_NAME)
                                )
                )
                .andExpect(status().isNoContent());

        verify(tagResourceCommandService).save(taggedResource);
    }
}
