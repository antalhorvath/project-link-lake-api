package com.vathevor.project.linklake.query.domain.tag.taggedresource;

import com.vathevor.shared.util.ShortUUID;

import java.util.List;
import java.util.Set;

public interface TaggedResourceReadOnlyRepository {

    List<TaggedResourceView> queryResources(ShortUUID shortUUID);

    List<TaggedResourceView> queryResourcesHavingAllTags(ShortUUID userId, Set<ShortUUID> tagIds);

    List<TaggedResourceView> queryResourcesHavingEitherTags(ShortUUID userId, Set<ShortUUID> tagIds);
}
