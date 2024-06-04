INSERT INTO linklake.resource (resource_id, user_id, name)
VALUES ('627e1a5b40ec4c6e9c0ebaacffb40477', 'c7034e68e0524fd5a5def8a478e72225', '9GAG'),
       ('61826d9dc6d2445d9cf8c76fd3fad619', 'c7034e68e0524fd5a5def8a478e72225', 'Dechatlon'),
       ('636fa195b24244cba3e7d3f10605cf99', 'c7034e68e0524fd5a5def8a478e72225', 'Pluralsight'),
       ('3b392dbb0f694865a9716217d233232d', 'c7034e68e0524fd5a5def8a478e72225', 'Brilliant'),
       ('0bb2e9cfeae34c7eab83fc3089ddb14c', 'c7034e68e0524fd5a5def8a478e72225', 'Workday');

INSERT INTO linklake.tag (tag_id, user_id, name)
VALUES ('f393ce3e5da640b58c10aa0cd39cc77d', 'c7034e68e0524fd5a5def8a478e72225', 'fun'),
       ('d3435d8a3c8d4836939a0f882728cb72', 'c7034e68e0524fd5a5def8a478e72225', 'exercise'),
       ('b7156e08bbe04e7abcfb213a0248a60a', 'c7034e68e0524fd5a5def8a478e72225', 'work'),
       ('4f5c18dce5f6423087593a91a0e68d00', 'c7034e68e0524fd5a5def8a478e72225', 'learning');

INSERT INTO linklake.resource_tag (user_id, resource_id, tag_id)
VALUES ('c7034e68e0524fd5a5def8a478e72225', '627e1a5b40ec4c6e9c0ebaacffb40477', 'f393ce3e5da640b58c10aa0cd39cc77d'),
       ('c7034e68e0524fd5a5def8a478e72225', '61826d9dc6d2445d9cf8c76fd3fad619', 'd3435d8a3c8d4836939a0f882728cb72'),
       ('c7034e68e0524fd5a5def8a478e72225', '636fa195b24244cba3e7d3f10605cf99', '4f5c18dce5f6423087593a91a0e68d00'),
       ('c7034e68e0524fd5a5def8a478e72225', '3b392dbb0f694865a9716217d233232d', '4f5c18dce5f6423087593a91a0e68d00'),
       ('c7034e68e0524fd5a5def8a478e72225', '0bb2e9cfeae34c7eab83fc3089ddb14c', 'b7156e08bbe04e7abcfb213a0248a60a');
