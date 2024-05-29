package com.vathevor.project.linklake.command.application.link;

import com.vathevor.project.linklake.command.domain.link.LinkCommandService;
import com.vathevor.project.linklake.command.domain.link.SaveLinkCommand;
import com.vathevor.shared.spring.identity.UserId;
import com.vathevor.shared.util.ShortUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkCommandController {

    private final LinkCommandService service;

    @PutMapping("/{linkId}")
    public ResponseEntity<Void> saveLink(@UserId ShortUUID userId,
                                         @PathVariable ShortUUID linkId,
                                         @Valid @RequestBody SaveLinkCommand request) {
        log.info("Received request: {}", request);
        var entity = request.toEntity(userId, linkId);
        service.save(entity);
        return ResponseEntity.noContent().build();
    }
}
