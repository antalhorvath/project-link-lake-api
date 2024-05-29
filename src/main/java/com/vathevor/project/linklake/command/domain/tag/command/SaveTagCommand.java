package com.vathevor.project.linklake.command.domain.tag.command;

import com.vathevor.project.linklake.command.domain.tag.entity.TagEntity;
import com.vathevor.shared.util.ShortUUID;

import java.util.Collection;
import java.util.List;

public record SaveTagCommand(
        ShortUUID tagId,
        String name
) {

    public static List<TagEntity> toEntities(Collection<SaveTagCommand> commands, ShortUUID userId) {
        return commands.stream()
                .map(c -> c.toEntity(userId))
                .toList();
    }

    public TagEntity toEntity(ShortUUID userId) {
        return TagEntity.builder()
                .userId(userId)
                .tagId(tagId)
                .name(name)
                .build();
    }
}
