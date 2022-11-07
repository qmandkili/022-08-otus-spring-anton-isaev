insert into genres(name) values ('FIRST_GENRE_NAME');
insert into genres(name) values ('SECOND_GENRE_NAME');
insert into authors(first_name, second_name) values ('FIRST_NAME', 'SECOND_NAME');
insert into authors(first_name, second_name) values ('SECOND_FIRST_NAME', 'SECOND_SECOND_NAME');
insert into books(name, author_id, genre_id) values ('FIRST_BOOK_NAME', 1, 1);
insert into books(name, author_id, genre_id) values ('SECOND_BOOK_NAME', 1, 2);
insert into comments(text, book_id) values ('FIRST_NAME', 1);
insert into comments(text, book_id) values ('SECOND_NAME', 1);