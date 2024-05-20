-- Schema: Application specific schema ---------------------------------------------------------------------------------

CREATE SCHEMA IF NOT EXISTS linklake;


-- Table: linklake.user_identity ---------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS linklake.user_identity
(
    user_id character varying(32) NOT NULL,
    idp_sub character varying(64) NOT NULL, -- sub claim of authentication token issued by idp
    CONSTRAINT user_pk PRIMARY KEY (user_id)
);


-- Table: linklake.link ------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS linklake.link
(
    link_id character varying(32) NOT NULL,
    user_id character varying(32) NOT NULL,
    name character varying(128) NOT NULL,
    link character varying(512) NOT NULL,
    CONSTRAINT link_pk PRIMARY KEY (link_id),
    CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES linklake.user_identity (user_id)
);


-- Table: linklake.tag -------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS linklake.tag
(
    tag_id character varying(32) NOT NULL,
    user_id character varying(32) NOT NULL,
    name character varying(32) NOT NULL,
    CONSTRAINT tag_pk PRIMARY KEY (tag_id),
    CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES linklake.user_identity (user_id)
);


-- Table: linklake.link_tag --------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS linklake.link_tag
(
    user_id character varying(32) NOT NULL,
    link_id character varying(32) NOT NULL,
    tag_id character varying(32) NOT NULL,
    CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES linklake.user_identity (user_id),
    CONSTRAINT link_fk FOREIGN KEY (link_id)
        REFERENCES linklake.link (link_id)
        ON DELETE CASCADE,

    CONSTRAINT tag_fk FOREIGN KEY (tag_id)
        REFERENCES linklake.tag (tag_id)
        ON DELETE CASCADE
);
