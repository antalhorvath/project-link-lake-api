package com.vathevor.project.linklake.query.application.tag;

import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourceQueryService;
import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourceView;
import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourcesByTagsQuery;
import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourcesQuery;
import com.vathevor.shared.spring.identity.UserId;
import com.vathevor.shared.util.LogicalOperator;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class TaggedResourceQueryController {

    private final TaggedResourceQueryService service;

    @GetMapping
    public ResponseEntity<List<TaggedResourceView>> queryResources(@UserId ShortUUID userId) {
        var query = new TaggedResourcesQuery(userId);
        List<TaggedResourceView> taggedResources = service.queryTaggedResources(query);
        return ResponseEntity.ok(taggedResources);
    }

    @GetMapping(params = "tag_ids")
    public ResponseEntity<List<TaggedResourceView>> queryResourcesByTags(
            @UserId ShortUUID userId,
            @RequestParam("tag_ids") Set<ShortUUID> tagIds,
            @RequestParam(value = "operator", required = false, defaultValue = "OR") LogicalOperator operator) {

        var query = new TaggedResourcesByTagsQuery(userId, tagIds, operator);
        List<TaggedResourceView> taggedResources = service.queryTaggedResourcesByTags(query);
        return ResponseEntity.ok(taggedResources);
    }
}
