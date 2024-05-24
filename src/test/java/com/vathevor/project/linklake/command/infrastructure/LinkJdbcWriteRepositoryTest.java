package com.vathevor.project.linklake.command.infrastructure;

import com.vathevor.project.linklake.command.domain.link.LinkEntity;
import com.vathevor.project.linklake.shared.BaseJdbcRepositoryTest;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = LinkJdbcWriteRepository.class)
class LinkJdbcWriteRepositoryTest extends BaseJdbcRepositoryTest {

    @Autowired
    LinkJdbcWriteRepository repository;

    @Test
    void saves_link() {
        LinkEntity link = LinkEntity.builder()
                .linkId(ShortUUID.randomUUID())
                .userId(userId)
                .name("example")
                .link("https://example.com")
                .build();

        repository.save(link);

        Optional<LinkEntity> savedLink = repository.findUsersLinkById(link.linkId(), link.userId());
        assertThat(savedLink)
                .isPresent()
                .contains(link);
    }
}
