package com.vathevor.project.linklake.command.application.link;

import com.vathevor.project.linklake.command.domain.link.LinkCommandService;
import com.vathevor.project.linklake.command.domain.link.LinkEntity;
import com.vathevor.project.linklake.shared.BaseMockMvcTest;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("feature")
@WebMvcTest(controllers = LinkCommandController.class)
class LinkCommandControllerTest extends BaseMockMvcTest {

    @MockBean
    LinkCommandService linkCommandService;

    @Nested
    class SaveLink {
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

    @Nested
    class DeleteLink {

        @Test
        void deletes_link() throws Exception {
            LinkEntity link = LinkEntity.builder()
                    .linkId(ShortUUID.randomUUID())
                    .userId(userIdentity.userId())
                    .build();

            mockMvc.perform(
                            delete("/links/" + link.linkId().value())
                                    .with(oauth2Login)
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNoContent());

            verify(linkCommandService).delete(link);
        }
    }
}
