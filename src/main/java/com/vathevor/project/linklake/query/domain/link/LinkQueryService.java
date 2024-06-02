package com.vathevor.project.linklake.query.domain.link;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkQueryService {

    private final LinkReadOnlyRepository repository;

    public List<LinkView> queryLinks(LinkQuery query) {
        return repository.findLinks(query.userId());
    }
}
