package com.vathevor.project.linklake.command.domain.tag.command;

import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.project.linklake.shared.SharedTestConstants;
import org.junit.jupiter.api.Test;

import static com.vathevor.project.linklake.command.domain.tag.TagTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

class SaveTaggedResourceCommandTest {

    @Test
    void converts_to_entity() {
        SaveTaggedResourceCommand command = SaveTaggedResourceCommand.builder()
                .name(RESOURCE_NAME)
                .tag(SAVE_TAG_1_COMMAND)
                .tag(SAVE_TAG_2_COMMAND)
                .build();

        TaggedResourceEntity entity = command.toEntity(SharedTestConstants.USER_1_ID, RESOURCE_ID);

        assertThat(entity).isEqualTo(TaggedResourceEntity.builder()
                .userId(SharedTestConstants.USER_1_ID)
                .resourceId(RESOURCE_ID)
                .name(RESOURCE_NAME)
                .tag(TAG_1)
                .tag(TAG_2)
                .build());
    }
}
