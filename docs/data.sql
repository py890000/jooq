USE jooq;

INSERT INTO posts (id, title, content, created_on) VALUES
  (1, 'Post 1', 'This is post 1', '2016-01-03'),
  (2, 'Post 2', 'This is post 2', '2016-01-05'),
  (3, 'Post 3', 'This is post 3', '2016-01-07');

INSERT INTO comments (id, post_id, name, email, content, created_on) VALUES
  (1, 1, 'User1', 'user1@gmail.com', 'This is comment 1 on post 1', '2016-01-07'),
  (2, 1, 'User2', 'user2@gmail.com', 'This is comment 2 on post 1', '2016-01-07'),
  (3, 2, 'User1', 'user1@gmail.com', 'This is comment 1 on post 2', '2016-01-07');

INSERT INTO author (first_name, last_name, date_of_birth, year_of_birth, address) VALUES
  ('George', 'Orwell', '1903-06-25', 1903, NULL),
  ('Paulo', 'Coelho', '1947-08-24', 1947, NULL);

INSERT INTO book VALUES
  (1, 1, NULL, NULL, '1984', 1948, 1,
      'To know and not to know, to be conscious of complete truthfulness while telling carefully constructed lies, to hold simultaneously two opinions which cancelled out, knowing them to be contradictory and believing in both of them, to use logic against logic, to repudiate morality while laying claim to it, to believe that democracy was impossible and that the Party was the guardian of democracy, to forget, whatever it was necessary to forget, then to draw it back into memory again at the moment when it was needed, and then promptly to forget it again, and above all, to apply the same process to the process itself -- that was the ultimate subtlety; consciously to induce unconsciousness, and then, once again, to become unconscious of the act of hypnosis you had just performed. Even to understand the word ''doublethink'' involved the use of doublethink..',
      NULL, 1, '2010-01-01 00:00:00'),
  (2, 1, NULL, NULL, 'Animal Farm', 1945, 1, NULL, NULL, NULL, '2010-01-01 00:00:00'),
  (3, 2, NULL, NULL, 'O Alquimista', 1988, 4, NULL, NULL, 1, NULL),
  (4, 2, NULL, NULL, 'Brida', 1990, 2, NULL, NULL, NULL, NULL);

INSERT INTO book_store (name) VALUES
  ('Orell Füssli'),
  ('Ex Libris'),
  ('Buchhandlung im Volkshaus');

INSERT INTO book_to_book_store VALUES
  ('Orell Füssli', 1, 10),
  ('Orell Füssli', 2, 10),
  ('Orell Füssli', 3, 10),
  ('Ex Libris', 1, 1),
  ('Ex Libris', 3, 2),
  ('Buchhandlung im Volkshaus', 3, 1);
