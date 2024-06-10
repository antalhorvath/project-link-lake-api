package com.vathevor.project.linklake.query.domain.tag.taggedresource;

import com.vathevor.shared.util.LogicalOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaggedResourceQueryService {

    private final TaggedResourceReadOnlyRepository repository;

    public List<TaggedResourceView> queryTaggedResources(TaggedResourcesQuery query) {
        return repository.queryResources(query.userId());
    }

    public List<TaggedResourceView> queryTaggedResourcesByTags(TaggedResourcesByTagsQuery query) {
        if (LogicalOperator.AND.equals(query.operator())) {
            return repository.queryResourcesHavingAllTags(query.userId(), query.tagIds());
        } else {
            return repository.queryResourcesHavingEitherTags(query.userId(), query.tagIds());
        }
    }
}
