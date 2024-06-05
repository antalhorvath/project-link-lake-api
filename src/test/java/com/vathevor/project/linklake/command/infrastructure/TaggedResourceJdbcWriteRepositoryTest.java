package com.vathevor.project.linklake.command.infrastructure;

import com.vathevor.project.linklake.command.domain.tag.entity.TagEntity;
import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static com.vathevor.project.linklake.command.domain.tag.TagTestConstants.RESOURCE_ID;
import static com.vathevor.project.linklake.command.domain.tag.TagTestConstants.RESOURCE_NAME;
import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_1_ID;
import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_2_ID;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = TaggedResourceJdbcWriteRepository.class)
class TaggedResourceJdbcWriteRepositoryTest {

    @Autowired
    TaggedResourceJdbcWriteRepository repository;

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Test
    void returns_empty_when_resource_not_found_by_id() {
        Optional<TaggedResourceEntity> resource = repository.findUserResourceById(USER_1_ID, RESOURCE_ID);
        assertThat(resource).isEmpty();
    }

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_01.sql")
    @Test
    void saves_resource() {
        TaggedResourceEntity taggedResource = TaggedResourceEntity.builder()
                .resourceId(RESOURCE_ID)
                .userId(USER_1_ID)
                .name(RESOURCE_NAME)
                .tag(TagEntity.builder().userId(USER_1_ID).tagId((ShortUUID.fromString("a430b6dbbdc848cb8766a3c33ceec941"))).name("java").build())
                .tag(TagEntity.builder().userId(USER_1_ID).tagId((ShortUUID.fromString("d931a3391a714ceb99f7054a289a7cf8"))).name("learning").build())
                .build();

        repository.saveTaggedResource(taggedResource);
        Optional<TaggedResourceEntity> retrievedResource = repository.findUserResourceById(USER_1_ID, RESOURCE_ID);

        assertThat(retrievedResource).isPresent().contains(taggedResource);
    }

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_01.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_02.sql")
    @Nested
    class Delete {

        @Test
        void deletes_resource_and_tag_reference_of_owner() {
            ShortUUID resourceIdBelongingToUser01 = ShortUUID.fromString("07cd5b428f944817b43ec0c35269b4db");
            var resourceToDelete = TaggedResourceEntity.builder()
                    .resourceId(resourceIdBelongingToUser01)
                    .userId(USER_1_ID)
                    .build();

            repository.delete(resourceToDelete);

            assertThat(repository.findUserResourceById(USER_1_ID, resourceIdBelongingToUser01)).isEmpty();
        }

        @Test
        void does_not_delete_resource_of_someone_else() {
            ShortUUID resourceIdBelongingToUser02 = ShortUUID.fromString("3b392dbb0f694865a9716217d233232d");
            var resourceToDelete = TaggedResourceEntity.builder()
                    .resourceId(resourceIdBelongingToUser02)
                    .userId(USER_1_ID)
                    .build();

            repository.delete(resourceToDelete);

            assertThat(repository.findUserResourceById(USER_2_ID, resourceIdBelongingToUser02)).isPresent();
        }
    }
}
