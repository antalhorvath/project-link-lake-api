package com.vathevor.project.linklake.query.application.tag;

import com.vathevor.project.linklake.query.domain.tag.SimpleTagView;
import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourceQueryService;
import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourceView;
import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourcesByTagsQuery;
import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourcesQuery;
import com.vathevor.project.linklake.shared.BaseMockMvcTest;
import com.vathevor.shared.util.LogicalOperator;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("feature")
@WebMvcTest(controllers = TaggedResourceQueryController.class)
class TaggedResourceQueryControllerTest extends BaseMockMvcTest {

    SimpleTagView TAG_JAVA = new SimpleTagView(ShortUUID.fromString("a430b6dbbdc848cb8766a3c33ceec941"), "java");
    SimpleTagView TAG_LEARNING = new SimpleTagView(ShortUUID.fromString("d931a3391a714ceb99f7054a289a7cf8"), "learning");

    TaggedResourceView RESOURCE_BAELDUNG = TaggedResourceView.builder()
            .resourceId(ShortUUID.fromString("02b349ed70434609b1c53f72ef874b7f"))
            .name("Baeldung")
            .tag(TAG_JAVA)
            .tag(TAG_LEARNING)
            .build();

    TaggedResourceView RESOURCE_INFOQ = TaggedResourceView.builder()
            .resourceId(ShortUUID.fromString("323007fa053745b6a63a89ce76372869"))
            .name("InfoQ")
            .tag(TAG_JAVA)
            .build();

    @MockBean
    TaggedResourceQueryService taggedResourceQueryService;

    @Test
    void queries_resources_of_user() throws Exception {
        var queryByUser = new TaggedResourcesQuery(userIdentity.userId());
        when(taggedResourceQueryService.queryTaggedResources(queryByUser))
                .thenReturn(List.of(RESOURCE_BAELDUNG, RESOURCE_INFOQ));

        mockMvc.perform(
                        get("/resources")
                                .with(oauth2Login)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "resourceId": "323007fa053745b6a63a89ce76372869",
                            "name": "InfoQ",
                            "tags": [
                              {
                                "tagId": "a430b6dbbdc848cb8766a3c33ceec941",
                                "name": "java"
                              }
                            ]
                          },
                          {
                            "resourceId": "02b349ed70434609b1c53f72ef874b7f",
                            "name": "Baeldung",
                            "tags": [
                              {
                                "tagId": "a430b6dbbdc848cb8766a3c33ceec941",
                                "name": "java"
                              },
                              {
                                "tagId": "d931a3391a714ceb99f7054a289a7cf8",
                                "name": "learning"
                              }
                            ]
                          }
                        ]
                        """));
    }

    @Test
    void queries_resources_of_user_by_tags_using_logical_AND_operator() throws Exception {
        var queryByUser = new TaggedResourcesByTagsQuery(userIdentity.userId(), Set.of(TAG_JAVA.tagId(), TAG_LEARNING.tagId()), LogicalOperator.AND);
        when(taggedResourceQueryService.queryTaggedResourcesByTags(queryByUser))
                .thenReturn(List.of(RESOURCE_BAELDUNG));

        mockMvc.perform(
                        get("/resources")
                                .with(oauth2Login)
                                .param("tag_ids", TAG_JAVA.tagId().value(), TAG_LEARNING.tagId().value())
                                .param("operator", "AND")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "resourceId": "02b349ed70434609b1c53f72ef874b7f",
                            "name": "Baeldung",
                            "tags": [
                              {
                                "tagId": "a430b6dbbdc848cb8766a3c33ceec941",
                                "name": "java"
                              },
                              {
                                "tagId": "d931a3391a714ceb99f7054a289a7cf8",
                                "name": "learning"
                              }
                            ]
                          }
                        ]
                        """));
    }

    @Test
    void queries_resources_of_user_by_tags_using_logical_OR_operator_as_default() throws Exception {
        var queryByUser = new TaggedResourcesByTagsQuery(userIdentity.userId(), Set.of(TAG_JAVA.tagId(), TAG_LEARNING.tagId()), LogicalOperator.OR);
        when(taggedResourceQueryService.queryTaggedResourcesByTags(queryByUser))
                .thenReturn(List.of(RESOURCE_BAELDUNG));

        mockMvc.perform(
                        get("/resources")
                                .with(oauth2Login)
                                .param("tag_ids", TAG_JAVA.tagId().value(), TAG_LEARNING.tagId().value())
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "resourceId": "02b349ed70434609b1c53f72ef874b7f",
                            "name": "Baeldung",
                            "tags": [
                              {
                                "tagId": "a430b6dbbdc848cb8766a3c33ceec941",
                                "name": "java"
                              },
                              {
                                "tagId": "d931a3391a714ceb99f7054a289a7cf8",
                                "name": "learning"
                              }
                            ]
                          }
                        ]
                        """));
    }
}
