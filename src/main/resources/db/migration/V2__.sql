ALTER TABLE Comment
    DROP FOREIGN KEY Comment_ibfk_1;

ALTER TABLE Comment
    DROP FOREIGN KEY Comment_ibfk_2;

ALTER TABLE Friend
    DROP FOREIGN KEY Friend_ibfk_1;

ALTER TABLE Friend
    DROP FOREIGN KEY Friend_ibfk_2;

ALTER TABLE Message
    DROP FOREIGN KEY Message_ibfk_1;

DROP TABLE Comment;

DROP TABLE Friend;

DROP TABLE Message;

DROP TABLE User;