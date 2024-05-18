package com.vathevor.project.linklake.app.controller;

import com.vathevor.shared.spring.identity.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/links")
public class QueryLinksController {

    @GetMapping
    public List<String> queryLinks(@UserId UUID userId) {
        log.info("Query links of user: {}", userId);
        return List.of("my very first link");
    }
}
