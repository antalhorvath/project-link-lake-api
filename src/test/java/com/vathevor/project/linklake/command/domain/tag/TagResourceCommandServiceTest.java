package com.vathevor.project.linklake.command.domain.tag;

import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.project.linklake.command.domain.tag.repository.TagRepository;
import com.vathevor.project.linklake.command.domain.tag.repository.TaggedResourceRepository;
import com.vathevor.project.linklake.command.infrastructure.TagJdbcWriteRepository;
import com.vathevor.project.linklake.command.infrastructure.TaggedResourceJdbcWriteRepository;
import com.vathevor.project.linklake.shared.BaseJdbcRepositoryTest;
import com.vathevor.project.linklake.shared.SharedTestConstants;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import static com.vathevor.project.linklake.command.domain.tag.TagTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@Transactional
@Import({
        TagResourceCommandService.class,
        TaggedResourceJdbcWriteRepository.class,
        TagJdbcWriteRepository.class
})
class TagResourceCommandServiceTest extends BaseJdbcRepositoryTest {

    @SpyBean
    TaggedResourceRepository taggedResourceRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TagResourceCommandService service;

    TaggedResourceEntity taggedResourceEntity = TaggedResourceEntity.builder()
            .userId(SharedTestConstants.USER_ID)
            .resourceId(RESOURCE_ID)
            .name(RESOURCE_NAME)
            .tag(TAG_1)
            .tag(TAG_2)
            .build();

    @Test
    void save_entity() {
        service.save(taggedResourceEntity);

        assertThat(countRowsInTable(jdbcClient, "linklake.resource")).isOne();
        assertThat(countRowsInTable(jdbcClient, "linklake.tag")).isEqualTo(2);
        assertThat(countRowsInTable(jdbcClient, "linklake.resource_tag")).isEqualTo(2);
    }

    @Nested
    class TransactionRollback {

        @Test
        void on_save_tagged_resource_failure() {
            doThrow(RuntimeException.class).when(taggedResourceRepository).saveTaggedResource(taggedResourceEntity);
            assertThatThrownBy(() -> service.save(taggedResourceEntity)).isInstanceOf(RuntimeException.class);
            TestTransaction.end();

            assertThat(countRowsInTable(jdbcClient, "linklake.tag")).isZero();
        }
    }
}
