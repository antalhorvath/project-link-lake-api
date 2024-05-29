package com.vathevor.project.linklake.command.domain.tag.entity;

import com.vathevor.shared.util.ShortUUID;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record TaggedResourceEntity(
        ShortUUID userId,
        ShortUUID resourceId,
        String name,
        @Singular
        List<TagEntity> tags
) {

    public Set<ShortUUID> tagIds() {
        return tags.stream()
                .map(TagEntity::tagId)
                .collect(Collectors.toSet());
    }

    public Set<TagEntity> filterTagsToCreate(Set<ShortUUID> existingTagIds) {
        return tags.stream()
                .filter(t -> !existingTagIds.contains(t.tagId()))
                .collect(Collectors.toSet());
    }
}
