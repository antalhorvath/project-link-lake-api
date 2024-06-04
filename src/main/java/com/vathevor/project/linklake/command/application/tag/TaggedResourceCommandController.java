package com.vathevor.project.linklake.command.application.tag;

import com.vathevor.project.linklake.command.domain.tag.TaggedResourceCommandService;
import com.vathevor.project.linklake.command.domain.tag.command.SaveTaggedResourceCommand;
import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.shared.spring.identity.UserId;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class TaggedResourceCommandController {

    private final TaggedResourceCommandService service;

    @PutMapping("/{resourceId}")
    public ResponseEntity<Void> saveTaggedResource(@UserId ShortUUID userId,
                                                   @PathVariable ShortUUID resourceId,
                                                   @RequestBody SaveTaggedResourceCommand command) {
        var resource = command.toEntity(userId, resourceId);
        service.save(resource);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Void> deleteTaggedResource(@UserId ShortUUID userId,
                                           @PathVariable ShortUUID resourceId) {
        var resource = TaggedResourceEntity.builder()
                .resourceId(resourceId)
                .userId(userId)
                .build();
        service.delete(resource);
        return ResponseEntity.noContent().build();
    }
}
