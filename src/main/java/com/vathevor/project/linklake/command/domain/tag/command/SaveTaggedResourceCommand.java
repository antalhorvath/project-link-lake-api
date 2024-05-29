package com.vathevor.project.linklake.command.domain.tag.command;

import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.shared.util.ShortUUID;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public record SaveTaggedResourceCommand(
        String name,
        @Singular
        List<SaveTagCommand> tags
) {

    public TaggedResourceEntity toEntity(ShortUUID userId, ShortUUID resourceId) {
        return TaggedResourceEntity.builder()
                .userId(userId)
                .resourceId(resourceId)
                .name(name)
                .tags(SaveTagCommand.toEntities(tags, userId))
                .build();
    }
}
