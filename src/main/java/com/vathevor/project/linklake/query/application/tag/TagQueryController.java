package com.vathevor.project.linklake.query.application.tag;

import com.vathevor.project.linklake.query.domain.tag.TagQuery;
import com.vathevor.project.linklake.query.domain.tag.TagQueryService;
import com.vathevor.project.linklake.query.domain.tag.TagView;
import com.vathevor.shared.spring.identity.UserId;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagQueryController {

    private final TagQueryService tagQueryService;

    @GetMapping
    public List<TagView> queryTags(@UserId ShortUUID userId) {
        var query = new TagQuery(userId);
        return tagQueryService.queryTags(query);
    }
}
