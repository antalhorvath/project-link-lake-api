package com.vathevor.project.linklake.command.domain.tag.repository;

import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.shared.util.ShortUUID;

import java.util.Optional;

public interface TaggedResourceRepository {

    Optional<TaggedResourceEntity> findUserResourceById(ShortUUID userId, ShortUUID resourceId);

    void saveTaggedResource(TaggedResourceEntity taggedResource);

    void delete(TaggedResourceEntity resourceToDelete);
}
