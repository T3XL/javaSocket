use chatUser;
    CREATE TABLE IF NOT EXISTS `user` (
        username VARCHAR(20) PRIMARY KEY,
        password VARCHAR(20)
        );
    INSERT INTO user VALUES ('user1', '123456');
    INSERT INTO user VALUES ('user2', '123456');
    INSERT INTO user VALUES ('user3', '123456');
