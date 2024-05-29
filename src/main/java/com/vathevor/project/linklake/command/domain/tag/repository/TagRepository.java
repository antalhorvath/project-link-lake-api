package com.vathevor.project.linklake.command.domain.tag.repository;

import com.vathevor.project.linklake.command.domain.tag.entity.TagEntity;
import com.vathevor.shared.util.ShortUUID;

import java.util.Collection;
import java.util.Set;

public interface TagRepository {

    Set<ShortUUID> findExistingTagIds(ShortUUID userId, Set<ShortUUID> tagIds);

    void saveAll(Collection<TagEntity> tags);
}
