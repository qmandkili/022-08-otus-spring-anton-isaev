DROP TABLE IF EXISTS COMMENTS;
CREATE TABLE COMMENTS
(
    ID BIGSERIAL PRIMARY KEY,
    TEXT VARCHAR(255),
    BOOK_ID BIGINT
);

DROP TABLE IF EXISTS BOOKS;
CREATE TABLE BOOKS
(
    ID BIGSERIAL PRIMARY KEY,
    NAME VARCHAR(255),
    GENRE_ID BIGINT,
    AUTHOR_ID BIGINT
);

DROP TABLE IF EXISTS GENRES;
CREATE TABLE GENRES
(
    ID BIGSERIAL PRIMARY KEY,
    NAME VARCHAR(255)
);

DROP TABLE IF EXISTS AUTHORS;
CREATE TABLE AUTHORS
(
    ID BIGSERIAL PRIMARY KEY,
    FIRST_NAME VARCHAR(255),
    SECOND_NAME VARCHAR(255)
);

DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS
(
    ID BIGSERIAL PRIMARY KEY,
    USERNAME VARCHAR(255) UNIQUE,
    PASSWORD VARCHAR(255)
);

ALTER TABLE BOOKS
    ADD CONSTRAINT fk_bookGenre
        FOREIGN KEY (GENRE_ID) REFERENCES GENRES(Id);

ALTER TABLE BOOKS
    ADD CONSTRAINT fk_bookAuthor
        FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS(Id);

ALTER TABLE COMMENTS
    ADD CONSTRAINT fk_commentBook
        FOREIGN KEY (BOOK_ID) REFERENCES BOOKS(Id) ON DELETE CASCADE;