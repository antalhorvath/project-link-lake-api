package com.vathevor.project.linklake.query.domain.tag.taggedresource;

import com.vathevor.project.linklake.command.domain.tag.TagTestConstants;
import com.vathevor.shared.util.LogicalOperator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_1_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaggedResourceQueryServiceTest {

    @Mock
    TaggedResourceReadOnlyRepository repository;

    @InjectMocks
    TaggedResourceQueryService service;

    @Test
    void queries_resources_of_user() {
        List<TaggedResourceView> resources = List.of(TaggedResourceView.builder().build());
        when(repository.queryResources(USER_1_ID)).thenReturn(resources);

        var result = service.queryTaggedResources(new TaggedResourcesQuery(USER_1_ID));

        assertThat(result).isEqualTo(resources);
    }

    @Test
    void queries_resources_of_user_having_all_tags() {
        List<TaggedResourceView> resources = List.of(TaggedResourceView.builder().build());
        var query = new TaggedResourcesByTagsQuery(USER_1_ID, Set.of(TagTestConstants.TAG_1_ID), LogicalOperator.AND);
        when(repository.queryResourcesHavingAllTags(query.userId(), query.tagIds()))
                .thenReturn(resources);

        var result = service.queryTaggedResourcesByTags(query);

        assertThat(result).isEqualTo(resources);
    }

    @Test
    void queries_resources_of_user_having_either_tags() {
        List<TaggedResourceView> resources = List.of(TaggedResourceView.builder().build());
        var query = new TaggedResourcesByTagsQuery(USER_1_ID, Set.of(TagTestConstants.TAG_1_ID), LogicalOperator.OR);
        when(repository.queryResourcesHavingEitherTags(query.userId(), query.tagIds()))
                .thenReturn(resources);

        var result = service.queryTaggedResourcesByTags(query);

        assertThat(result).isEqualTo(resources);
    }
}
