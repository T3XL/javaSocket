use chatUser;
    CREATE TABLE IF NOT EXISTS `user` (
        username VARCHAR(20) PRIMARY KEY,
        password VARCHAR(20)
        );
    INSERT INTO user VALUES ('user1', '123456');
    INSERT INTO user VALUES ('user2', '123456');
    INSERT INTO user VALUES ('user3', '123456');
    CREATE TABLE chat_log (
        id INT AUTO_INCREMENT PRIMARY KEY,
        timestamp TIMESTAMP NOT NULL,
        username VARCHAR(255) NOT NULL,
        content TEXT NOT NULL
        );

