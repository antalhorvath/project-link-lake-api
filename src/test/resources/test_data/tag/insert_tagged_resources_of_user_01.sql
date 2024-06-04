INSERT INTO linklake.resource (resource_id, user_id, name)
VALUES ('323007fa053745b6a63a89ce76372869', '7c329b4921004ed6bf14fda2a4f6d832', 'InfoQ'),
       ('02b349ed70434609b1c53f72ef874b7f', '7c329b4921004ed6bf14fda2a4f6d832', 'Baeldung'),
       ('b57693dbb18e4c25bf01a57cfe45b201', '7c329b4921004ed6bf14fda2a4f6d832', 'Voxxeddays'),
       ('48c7c5ee32424bfeb9660a4d1915ac3e', '7c329b4921004ed6bf14fda2a4f6d832', 'Pluralsight'),
       ('07cd5b428f944817b43ec0c35269b4db', '7c329b4921004ed6bf14fda2a4f6d832', 'Udemy'),
       ('37bdae5015814c359acd976a28a051c7', '7c329b4921004ed6bf14fda2a4f6d832', 'Workday');

INSERT INTO linklake.tag (tag_id, user_id, name)
VALUES ( 'a430b6dbbdc848cb8766a3c33ceec941', '7c329b4921004ed6bf14fda2a4f6d832', 'java'),
       ( 'd931a3391a714ceb99f7054a289a7cf8', '7c329b4921004ed6bf14fda2a4f6d832', 'learning'),
       ( 'a17f3f8ddcbb45c2a6409129d357ff63', '7c329b4921004ed6bf14fda2a4f6d832', 'work');

INSERT INTO linklake.resource_tag (user_id, resource_id, tag_id)
VALUES ('7c329b4921004ed6bf14fda2a4f6d832', '323007fa053745b6a63a89ce76372869', 'a430b6dbbdc848cb8766a3c33ceec941'),
       ('7c329b4921004ed6bf14fda2a4f6d832', '02b349ed70434609b1c53f72ef874b7f', 'a430b6dbbdc848cb8766a3c33ceec941'),
       ('7c329b4921004ed6bf14fda2a4f6d832', '02b349ed70434609b1c53f72ef874b7f', 'd931a3391a714ceb99f7054a289a7cf8'),
       ('7c329b4921004ed6bf14fda2a4f6d832', 'b57693dbb18e4c25bf01a57cfe45b201', 'd931a3391a714ceb99f7054a289a7cf8'),
       ('7c329b4921004ed6bf14fda2a4f6d832', '48c7c5ee32424bfeb9660a4d1915ac3e', 'd931a3391a714ceb99f7054a289a7cf8'),
       ('7c329b4921004ed6bf14fda2a4f6d832', '07cd5b428f944817b43ec0c35269b4db', 'd931a3391a714ceb99f7054a289a7cf8'),
       ('7c329b4921004ed6bf14fda2a4f6d832', '37bdae5015814c359acd976a28a051c7', 'a17f3f8ddcbb45c2a6409129d357ff63');