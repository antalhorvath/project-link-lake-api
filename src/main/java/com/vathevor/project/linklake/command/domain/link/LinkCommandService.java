package com.vathevor.project.linklake.command.domain.link;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkCommandService {

    private final LinkRepository repository;

    public void save(LinkEntity linkEntity) {
        repository.save(linkEntity);
    }
}
