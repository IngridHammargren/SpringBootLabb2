create DATABASE springBootLabb2;


CREATE TABLE User (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(255) UNIQUE NOT NULL,
                      email VARCHAR(255) UNIQUE NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      display_name VARCHAR(255),
                      profile_picture VARCHAR(255),
                      registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      last_login_date TIMESTAMP
);


CREATE TABLE Message (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL,
                         content TEXT NOT NULL,
                         creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         last_edit_date TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES User(id)
);


CREATE TABLE Comment (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL,
                         message_id BIGINT NOT NULL,
                         content TEXT NOT NULL,
                         creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         last_edit_date TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES User(id),
                         FOREIGN KEY (message_id) REFERENCES Message(id)
);


CREATE TABLE Friend (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        friend_id BIGINT NOT NULL,
                        status ENUM('REQUESTED', 'ACCEPTED', 'REJECTED') DEFAULT 'REQUESTED',
                        request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        response_date TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES User(id),
                        FOREIGN KEY (friend_id) REFERENCES User(id),
                        CHECK (user_id < friend_id)  -- Assuming bidirectional friendship
);

Drop Table Friend;
Drop Table Comment;





