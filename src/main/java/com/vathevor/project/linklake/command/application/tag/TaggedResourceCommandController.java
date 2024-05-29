package com.vathevor.project.linklake.command.application.tag;

import com.vathevor.project.linklake.command.domain.tag.TagResourceCommandService;
import com.vathevor.project.linklake.command.domain.tag.command.SaveTaggedResourceCommand;
import com.vathevor.shared.spring.identity.UserId;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class TaggedResourceCommandController {

    private final TagResourceCommandService tagResourceCommandService;

    @PutMapping("/{resourceId}")
    public ResponseEntity<Void> saveTaggedResource(@UserId ShortUUID userId,
                                                   @PathVariable ShortUUID resourceId,
                                                   @RequestBody SaveTaggedResourceCommand command) {
        var entity = command.toEntity(userId, resourceId);
        tagResourceCommandService.save(entity);
        return ResponseEntity.noContent().build();
    }
}
