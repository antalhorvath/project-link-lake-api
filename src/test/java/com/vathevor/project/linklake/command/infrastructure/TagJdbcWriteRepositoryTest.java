package com.vathevor.project.linklake.command.infrastructure;

import com.vathevor.project.linklake.command.domain.tag.entity.TagEntity;
import com.vathevor.project.linklake.shared.BaseJdbcRepositoryTest;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Set;

import static com.vathevor.project.linklake.command.domain.tag.TagTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = TagJdbcWriteRepository.class)
class TagJdbcWriteRepositoryTest extends BaseJdbcRepositoryTest {

    @Autowired
    TagJdbcWriteRepository repository;

    @Test
    void saves_tags() {
        List<TagEntity> tagsToSave = List.of(TAG_1, TAG_2);

        repository.saveAll(tagsToSave);

        Set<TagEntity> savedTags = jdbcClient.sql("SELECT tag_id, user_id, name FROM linklake.tag")
                .query((rs, r) -> TagEntity.builder()
                        .userId(ShortUUID.fromString(rs.getString("user_id")))
                        .tagId(ShortUUID.fromString(rs.getString("tag_id")))
                        .name(rs.getString("name"))
                        .build())
                .set();
        assertThat(savedTags).containsExactlyInAnyOrderElementsOf(tagsToSave);
    }

    @Test
    void returns_ids_of_users_existing_tags() {
        List<TagEntity> tagsToSave = List.of(TAG_OF_USER_1, TAG_OF_USER_2);
        repository.saveAll(tagsToSave);

        Set<ShortUUID> tagIdsWithOneExisting = Set.of(ShortUUID.randomUUID(), TAG_OF_USER_1.tagId(), ShortUUID.randomUUID());
        Set<ShortUUID> existingTagIds = repository.findExistingTagIds(TAG_OF_USER_1.userId(), tagIdsWithOneExisting);

        assertThat(existingTagIds).containsExactlyInAnyOrder(TAG_OF_USER_1.tagId());
    }
}
