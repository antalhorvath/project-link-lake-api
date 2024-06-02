package com.vathevor.project.linklake.shared;

import com.vathevor.shared.spring.identity.UserIdentity;
import com.vathevor.shared.util.ShortUUID;

public class SharedTestConstants {
    public static final String USER_1_ID_STRING = "7c329b4921004ed6bf14fda2a4f6d832";
    public static final ShortUUID USER_1_ID = ShortUUID.fromString(USER_1_ID_STRING);
    public static final String USER_1_IDP_SUB = "user1IdpSub";
    public static final UserIdentity USER_1 = new UserIdentity(USER_1_ID, USER_1_IDP_SUB);

    public static final String USER_2_ID_STRING = "c7034e68e0524fd5a5def8a478e72225";
    public static final ShortUUID USER_2_ID = ShortUUID.fromString(USER_2_ID_STRING);
    public static final String USER_2_IDP_SUB = "user2IdpSub";
    public static final UserIdentity USER_2 = new UserIdentity(USER_2_ID, USER_2_IDP_SUB);
}
