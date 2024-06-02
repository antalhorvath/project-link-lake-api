-- Schema: linklake  ---------------------------------------------------------------------------------------------------

CREATE SCHEMA IF NOT EXISTS linklake;


-- Table ---------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS linklake.user_identity
(
    user_id character varying(32) NOT NULL,
    idp_sub character varying(64) NOT NULL, -- sub claim of authentication token issued by idp

    CONSTRAINT user_pk PRIMARY KEY (user_id)
);


-- Table ---------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS linklake.link
(
    link_id character varying(32)  NOT NULL,
    user_id character varying(32)  NOT NULL,
    name    character varying(128) NOT NULL,
    link    character varying(512) NOT NULL,
    modified_at date NOT NULL,

    CONSTRAINT link_pk PRIMARY KEY (link_id),

    CONSTRAINT user_fk_of_link FOREIGN KEY (user_id)
        REFERENCES linklake.user_identity (user_id)
);


-- Table ---------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS linklake.tag
(
    tag_id  character varying(32) NOT NULL,
    user_id character varying(32) NOT NULL,
    name    character varying(32) NOT NULL,

    CONSTRAINT tag_pk PRIMARY KEY (tag_id),

    CONSTRAINT user_fk_of_tag FOREIGN KEY (user_id)
        REFERENCES linklake.user_identity (user_id)
);


-- Table ---------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS linklake.resource
(
    resource_id character varying(32)  NOT NULL,
    user_id     character varying(32)  NOT NULL,
    name        character varying(128) NOT NULL,

    CONSTRAINT resource_pk PRIMARY KEY (resource_id),

    CONSTRAINT user_fk_of_resource FOREIGN KEY (user_id)
        REFERENCES linklake.user_identity (user_id)
);


-- Table ---------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS linklake.resource_tag
(
    user_id     character varying(32) NOT NULL,
    resource_id character varying(32) NOT NULL,
    tag_id      character varying(32) NOT NULL,

    CONSTRAINT user_fk_of_resource_tag FOREIGN KEY (user_id)
        REFERENCES linklake.user_identity (user_id),

    CONSTRAINT link_fk_of_resource_tag FOREIGN KEY (resource_id)
        REFERENCES linklake.resource (resource_id)
        ON DELETE CASCADE,

    CONSTRAINT tag_fk_of_resource_tag FOREIGN KEY (tag_id)
        REFERENCES linklake.tag (tag_id)
        ON DELETE CASCADE
);
