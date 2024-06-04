package com.vathevor.project.linklake.command.domain.tag;

import com.vathevor.project.linklake.command.domain.tag.entity.TagEntity;
import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.project.linklake.command.domain.tag.repository.TagRepository;
import com.vathevor.project.linklake.command.domain.tag.repository.TaggedResourceRepository;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaggedResourceCommandService {

    private final TagRepository tagRepository;
    private final TaggedResourceRepository taggedResourceRepository;

    @Transactional
    public void save(TaggedResourceEntity taggedResource) {
        Set<ShortUUID> existingTagIds = tagRepository.findExistingTagIds(taggedResource.userId(), taggedResource.tagIds());
        Set<TagEntity> tagsToCreate = taggedResource.filterTagsToCreate(existingTagIds);
        tagRepository.saveAll(tagsToCreate);
        taggedResourceRepository.saveTaggedResource(taggedResource);
    }

    @Transactional
    public void delete(TaggedResourceEntity resourceToDelete) {
        taggedResourceRepository.delete(resourceToDelete);
    }
}
