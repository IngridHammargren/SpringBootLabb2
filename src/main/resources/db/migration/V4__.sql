ALTER TABLE message_entity
    ADD last_modified_by VARCHAR(255) NULL;

ALTER TABLE message_entity
    MODIFY last_modified_by VARCHAR (255) NOT NULL;