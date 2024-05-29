package com.vathevor.project.linklake.command.domain.tag;

import com.vathevor.project.linklake.command.domain.tag.command.SaveTagCommand;
import com.vathevor.project.linklake.command.domain.tag.entity.TagEntity;
import com.vathevor.project.linklake.shared.SharedTestConstants;
import com.vathevor.shared.util.ShortUUID;

public class TagTestConstants {

    public static final ShortUUID RESOURCE_ID = ShortUUID.randomUUID();
    public static final String RESOURCE_NAME = "my resource";

    public static final ShortUUID TAG_1_ID = ShortUUID.randomUUID();
    public static final String TAG_1_NAME = "my tag 1";

    public static final ShortUUID TAG_2_ID = ShortUUID.randomUUID();
    public static final String TAG_2_NAME = "my tag 2";

    public static final SaveTagCommand SAVE_TAG_1_COMMAND = new SaveTagCommand(TAG_1_ID, TAG_1_NAME);
    public static final SaveTagCommand SAVE_TAG_2_COMMAND = new SaveTagCommand(TAG_2_ID, TAG_2_NAME);

    public static final TagEntity TAG_1 = TagEntity.builder()
            .tagId(TAG_1_ID)
            .userId(SharedTestConstants.USER_ID)
            .name(TAG_1_NAME)
            .build();
    public static final TagEntity TAG_2 = TagEntity.builder()
            .tagId(TAG_2_ID)
            .userId(SharedTestConstants.USER_ID)
            .name(TAG_2_NAME)
            .build();

    public static final TagEntity TAG_OF_USER_1 = TAG_1;
    public static final TagEntity TAG_OF_USER_2 = TagEntity.builder()
            .tagId(ShortUUID.randomUUID())
            .userId(SharedTestConstants.USER_2_ID)
            .name("random tag")
            .build();
    ;
}
