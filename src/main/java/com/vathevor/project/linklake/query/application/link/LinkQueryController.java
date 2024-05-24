package com.vathevor.project.linklake.query.application.link;

import com.vathevor.shared.spring.identity.UserId;
import com.vathevor.shared.util.ShortUUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/links")
public class LinkQueryController {

    @GetMapping
    public List<String> queryLinks(@UserId ShortUUID userId) {
        log.info("Query links of user: {}", userId);
        return List.of("my very first link");
    }
}
