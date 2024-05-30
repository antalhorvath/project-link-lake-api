package com.vathevor.project.linklake.command.infrastructure;

import com.vathevor.project.linklake.command.domain.tag.entity.TagEntity;
import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.project.linklake.shared.BaseJdbcRepositoryTest;
import com.vathevor.project.linklake.shared.SharedTestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.vathevor.project.linklake.command.domain.tag.TagTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = TaggedResourceJdbcWriteRepository.class)
class TaggedResourceJdbcWriteRepositoryTest extends BaseJdbcRepositoryTest {

    @Autowired
    TaggedResourceJdbcWriteRepository repository;

    @BeforeEach
    void setUp() {
        insertUser();
        insertTag(TAG_1);
        insertTag(TAG_2);
    }

    private void insertTag(TagEntity t) {
        jdbcClient.sql("INSERT INTO linklake.tag (tag_id, user_id, name) VALUES (:tag_id, :user_id, :name)")
                .param("tag_id", t.tagId().value())
                .param("user_id", t.userId().value())
                .param("name", t.name())
                .update();
    }

    @Test
    void returns_empty_when_resource_not_found_by_id() {
        Optional<TaggedResourceEntity> resource = repository.findUserResourceById(SharedTestConstants.USER_ID, RESOURCE_ID);
        assertThat(resource).isEmpty();
    }

    @Test
    void saves_resource() {
        TaggedResourceEntity taggedResource = TaggedResourceEntity.builder()
                .resourceId(RESOURCE_ID)
                .userId(SharedTestConstants.USER_ID)
                .name(RESOURCE_NAME)
                .tag(TAG_1)
                .tag(TAG_2)
                .build();

        repository.saveTaggedResource(taggedResource);
        Optional<TaggedResourceEntity> retrievedResource = repository.findUserResourceById(SharedTestConstants.USER_ID, RESOURCE_ID);

        assertThat(retrievedResource).isPresent().contains(taggedResource);
    }
}
