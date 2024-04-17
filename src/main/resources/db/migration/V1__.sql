CREATE TABLE message_entity
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    content            LONGTEXT              NOT NULL,
    user_id            BIGINT                NOT NULL,
    is_public          BIT(1)                NOT NULL,
    created_date       datetime              NOT NULL,
    last_modified_date datetime              NOT NULL,
    CONSTRAINT pk_messageentity PRIMARY KEY (id)
);

CREATE TABLE user_entity
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    user_name       VARCHAR(255)          NOT NULL,
    profile_picture VARCHAR(255)          NULL,
    first_name      VARCHAR(255)          NOT NULL,
    last_name       VARCHAR(255)          NOT NULL,
    email           VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_userentity PRIMARY KEY (id)
);

ALTER TABLE user_entity
    ADD CONSTRAINT uc_userentity_email UNIQUE (email);

ALTER TABLE user_entity
    ADD CONSTRAINT uc_userentity_username UNIQUE (user_name);

ALTER TABLE message_entity
    ADD CONSTRAINT FK_MESSAGEENTITY_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

