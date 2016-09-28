USE jooq;

INSERT INTO posts (id, title, content, created_on) VALUES (1, 'Post 1', 'This is post 1', '2016-01-03');
INSERT INTO posts (id, title, content, created_on) VALUES (2, 'Post 2', 'This is post 2', '2016-01-05');
INSERT INTO posts (id, title, content, created_on) VALUES (3, 'Post 3', 'This is post 3', '2016-01-07');

INSERT INTO comments (id, post_id, name, email, content, created_on)
VALUES (1, 1, 'User1', 'user1@gmail.com', 'This is comment 1 on post 1', '2016-01-07');

INSERT INTO comments (id, post_id, name, email, content, created_on)
VALUES (2, 1, 'User2', 'user2@gmail.com', 'This is comment 2 on post 1', '2016-01-07');

INSERT INTO comments (id, post_id, name, email, content, created_on)
VALUES (3, 2, 'User1', 'user1@gmail.com', 'This is comment 1 on post 2', '2016-01-07');
