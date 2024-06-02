package com.vathevor.project.linklake.command.domain.tag.command;

import com.vathevor.project.linklake.command.domain.tag.entity.TagEntity;
import com.vathevor.project.linklake.shared.SharedTestConstants;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.vathevor.project.linklake.command.domain.tag.TagTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

class SaveTagCommandTest {

    @Test
    void converts_to_entity() {
        TagEntity entity = SAVE_TAG_1_COMMAND.toEntity(SharedTestConstants.USER_1_ID);
        assertThat(entity).isEqualTo(
                TagEntity.builder()
                        .userId(SharedTestConstants.USER_1_ID)
                        .tagId(TAG_1_ID)
                        .name(TAG_1_NAME)
                        .build()
        );
    }

    @Test
    void converts_to_list_of_entities() {
        List<TagEntity> entities = SaveTagCommand.toEntities(List.of(
                SAVE_TAG_1_COMMAND,
                SAVE_TAG_2_COMMAND
        ), SharedTestConstants.USER_1_ID);
        assertThat(entities).containsExactlyInAnyOrder(TAG_1, TAG_2);
    }
}
