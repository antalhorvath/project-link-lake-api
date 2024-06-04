package com.vathevor.project.linklake.command.domain.tag;

import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.project.linklake.command.domain.tag.repository.TagRepository;
import com.vathevor.project.linklake.command.domain.tag.repository.TaggedResourceRepository;
import com.vathevor.project.linklake.command.infrastructure.TagJdbcWriteRepository;
import com.vathevor.project.linklake.command.infrastructure.TaggedResourceJdbcWriteRepository;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.TestTransaction;

import java.util.Optional;

import static com.vathevor.project.linklake.command.domain.tag.TagTestConstants.*;
import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_1_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@JdbcTest
@Import({
        TaggedResourceCommandService.class,
        TaggedResourceJdbcWriteRepository.class,
        TagJdbcWriteRepository.class
})
class TaggedResourceCommandServiceTest {

    @SpyBean
    JdbcClient jdbcClient;

    @SpyBean
    TaggedResourceRepository taggedResourceRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TaggedResourceCommandService service;

    TaggedResourceEntity taggedResourceEntity = TaggedResourceEntity.builder()
            .userId(USER_1_ID)
            .resourceId(RESOURCE_ID)
            .name(RESOURCE_NAME)
            .tag(TAG_1)
            .tag(TAG_2)
            .build();

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Nested
    class Save {

        @Test
        void saves_tagged_resource() {
            service.save(taggedResourceEntity);

            assertThat(countRowsInTable(jdbcClient, "linklake.resource")).isOne();
            assertThat(countRowsInTable(jdbcClient, "linklake.tag")).isEqualTo(2);
            assertThat(countRowsInTable(jdbcClient, "linklake.resource_tag")).isEqualTo(2);
        }

        @Test
        void on_save_tagged_resource_failure() {
            doThrow(RuntimeException.class).when(taggedResourceRepository).saveTaggedResource(taggedResourceEntity);
            assertThatThrownBy(() -> service.save(taggedResourceEntity)).isInstanceOf(RuntimeException.class);
            TestTransaction.end();

            assertThat(countRowsInTable(jdbcClient, "linklake.tag")).isZero();
        }
    }

    @Nested
    class Delete {

        @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
        @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_01.sql")
        @Test
        void deletes_tagged_resource() {
            TaggedResourceEntity resource = TaggedResourceEntity.builder()
                    .resourceId(ShortUUID.fromString("323007fa053745b6a63a89ce76372869"))
                    .userId(USER_1_ID)
                    .build();

            service.delete(resource);

            Optional<TaggedResourceEntity> deletedResource = taggedResourceRepository.findUserResourceById(USER_1_ID, resource.resourceId());
            assertThat(deletedResource).isEmpty();
        }

        @Nested
        class TransactionRollback {

            @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
            @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_01.sql")
            @Test
            void on_delete_resource_failure() {
                TestTransaction.end(); // to persist rows via Sql annotations, so next rollback will not wipe that data
                TestTransaction.start();
                int numberOfResources = countRowsInTable(jdbcClient, "linklake.resource");
                int numberOfResourceTags = countRowsInTable(jdbcClient, "linklake.resource_tag");
                ShortUUID resourceId = ShortUUID.fromString("07cd5b428f944817b43ec0c35269b4db");
                TaggedResourceEntity resourceToDelete = TaggedResourceEntity.builder()
                        .userId(USER_1_ID)
                        .resourceId(resourceId)
                        .build();
                doThrow(RuntimeException.class)
                        .when(jdbcClient)
                        .sql("DELETE FROM linklake.resource WHERE user_id = :user_id AND resource_id = :resource_id");

                assertThatThrownBy(() -> service.delete(resourceToDelete)).isInstanceOf(RuntimeException.class);
                TestTransaction.end();

                assertThat(countRowsInTable(jdbcClient, "linklake.resource")).isEqualTo(numberOfResources);
                assertThat(countRowsInTable(jdbcClient, "linklake.resource_tag")).isEqualTo(numberOfResourceTags);
            }
        }
    }
}
