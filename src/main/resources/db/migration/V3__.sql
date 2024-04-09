ALTER TABLE user_entity
    ADD github_id BIGINT NULL;

ALTER TABLE user_entity
    MODIFY github_id BIGINT NOT NULL;