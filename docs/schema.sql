DROP SCHEMA `jooq`;

CREATE DATABASE IF NOT EXISTS `jooq`
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE jooq;

# 1) tables for blog test =========================

DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS comments;

CREATE TABLE posts (
  id         INT(11)      NOT NULL AUTO_INCREMENT,
  title      VARCHAR(200) NOT NULL,
  content    LONGTEXT              DEFAULT NULL,
  created_on DATETIME              DEFAULT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE comments (
  id         INT(11)      NOT NULL AUTO_INCREMENT,
  post_id    INT(11)      NOT NULL,
  name       VARCHAR(200) NOT NULL,
  email      VARCHAR(200) NOT NULL,
  content    LONGTEXT              DEFAULT NULL,
  created_on DATETIME              DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (post_id) REFERENCES posts (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 2) tables for transaction test =========================

DROP TABLE IF EXISTS book_to_book_store;
DROP TABLE IF EXISTS book_store;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;

CREATE TABLE author (
  id            INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name    VARCHAR(50),
  last_name     VARCHAR(50) NOT NULL,
  date_of_birth DATE,
  year_of_birth INT,
  address       VARCHAR(50)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE book (
  id            INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  author_id     INT          NOT NULL,
  co_author_id  INT,
  details_id    INT,
  title         VARCHAR(400) NOT NULL,
  published_in  INT,
  language_id   INT,
  content_text  LONGTEXT,
  content_pdf   BLOB,

  rec_version   INT,
  rec_timestamp TIMESTAMP,

  CONSTRAINT fk_book_author_id FOREIGN KEY (author_id) REFERENCES author (id),
  CONSTRAINT fk_book_co_author_id FOREIGN KEY (co_author_id) REFERENCES author (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE book_store (
  name VARCHAR(400) NOT NULL PRIMARY KEY UNIQUE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE book_to_book_store (
  book_store_name VARCHAR(400) NOT NULL,
  book_id         INTEGER      NOT NULL,
  stock           INTEGER,

  CONSTRAINT pk_b2bs PRIMARY KEY (book_store_name, book_id),

  CONSTRAINT fk_b2bs_bs_name FOREIGN KEY (book_store_name)
  REFERENCES book_store (name)
    ON DELETE CASCADE,

  CONSTRAINT fk_b2bs_b_id FOREIGN KEY (book_id)
  REFERENCES book (id)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
