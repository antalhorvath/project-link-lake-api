package com.vathevor.project.linklake.query.domain.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagQueryService {

    private final TagReadOnlyRepository tagRepository;

    public List<TagView> queryTags(TagQuery query) {
        return tagRepository.findTags(query.userId());
    }
}
