package com.vathevor.project.linklake.query.application.tag;

import com.vathevor.project.linklake.query.domain.tag.TagQuery;
import com.vathevor.project.linklake.query.domain.tag.TagQueryService;
import com.vathevor.project.linklake.query.domain.tag.TagView;
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
@WebMvcTest(controllers = TagQueryController.class)
class TagQueryControllerTest extends BaseMockMvcTest {

    @MockBean
    TagQueryService tagQueryService;

    @Test
    void queries_tags_of_user() throws Exception {
        when(tagQueryService.queryTags(new TagQuery(userIdentity.userId())))
                .thenReturn(List.of(
                        TagView.builder()
                                .tagId(ShortUUID.fromString("e5bc7046193042e7b6fd561ac425d312"))
                                .name("user1 tag1")
                                .numberOfTaggedResources(100)
                                .build(),
                        TagView.builder()
                                .tagId(ShortUUID.fromString("9231bc7b95d84c50b1499b8a548204e3"))
                                .name("user1 tag2")
                                .numberOfTaggedResources(42)
                                .build()
                ));

        mockMvc.perform(
                        get("/tags")
                                .with(oauth2Login)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {
                                "tagId": "e5bc7046193042e7b6fd561ac425d312",
                                "name": "user1 tag1",
                                "numberOfTaggedResources": 100
                            },
                            {
                                "tagId": "9231bc7b95d84c50b1499b8a548204e3",
                                "name": "user1 tag2",
                                "numberOfTaggedResources": 42
                            }
                        ]
                        """));
    }
}
