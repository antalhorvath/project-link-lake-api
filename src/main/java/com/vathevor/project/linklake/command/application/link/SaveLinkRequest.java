package com.vathevor.project.linklake.command.application.link;

import com.vathevor.project.linklake.command.domain.link.LinkEntity;
import com.vathevor.shared.util.ShortUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SaveLinkRequest(

        @NotNull(message = "name is required")
        @NotBlank(message = "name is required")
        @Pattern(regexp = "[A-Za-z0-9\\s]+", message = "name may contain alphanumeric letters and whitespaces")
        @Size(min = 3, max = 128, message = "name may contain minimum {min} and maximum {max} letters.")
        String name,

        @NotNull(message = "link is required")
        @NotBlank(message = "link is required")
        @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#]+\\.[^\\s/$.?#]+$", message = "link must be a valid URL")
        @Size(min = 5, max = 512, message = "link may contain minimum {min} and maximum {max} letters.")
        String link
) {

        LinkEntity toEntity(ShortUUID userId, ShortUUID linkId) {
                return LinkEntity.builder()
                        .userId(userId)
                        .linkId(linkId)
                        .name(name)
                        .link(link)
                        .build();
        }
}
