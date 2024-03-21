create DATABASE springBootLabb2;


CREATE TABLE User (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(255) UNIQUE NOT NULL,
                      email VARCHAR(255) UNIQUE NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      display_name VARCHAR(255),
                      profile_picture VARCHAR(255)
);


CREATE TABLE Message (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL,
                         content TEXT NOT NULL,
                         creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         last_edit_date TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES User(id)
);






