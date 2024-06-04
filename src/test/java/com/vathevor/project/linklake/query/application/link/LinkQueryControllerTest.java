package com.vathevor.project.linklake.query.application.link;

import com.vathevor.project.linklake.query.domain.link.LinkQuery;
import com.vathevor.project.linklake.query.domain.link.LinkQueryService;
import com.vathevor.project.linklake.query.domain.link.LinkView;
import com.vathevor.project.linklake.shared.BaseMockMvcTest;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("feature")
@WebMvcTest(controllers = LinkQueryController.class)
class LinkQueryControllerTest extends BaseMockMvcTest {

    @MockBean
    LinkQueryService linkQueryService;

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
