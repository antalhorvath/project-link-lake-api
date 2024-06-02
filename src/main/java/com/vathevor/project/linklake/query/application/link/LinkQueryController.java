package com.vathevor.project.linklake.query.application.link;

import com.vathevor.project.linklake.query.domain.link.LinkQuery;
import com.vathevor.project.linklake.query.domain.link.LinkQueryService;
import com.vathevor.project.linklake.query.domain.link.LinkView;
import com.vathevor.shared.spring.identity.UserId;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkQueryController {

    private final LinkQueryService linkQueryService;

    @GetMapping
    public List<LinkView> queryLinks(@UserId ShortUUID userId) {
        var query = new LinkQuery(userId);
        return linkQueryService.queryLinks(query);
    }
}
