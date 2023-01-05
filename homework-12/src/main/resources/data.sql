insert into genres( name) values ('Classic');
insert into genres(name) values ('Folklore');
insert into genres(name) values ('Historical');
insert into genres(name) values ('Epic');
insert into genres(name) values ('Fantasy');
insert into authors(first_name, second_name) values ('Anton', 'Isaev');
insert into authors(first_name, second_name) values ('NAME', 'NAME');
insert into books(name, author_id, genre_id) values ('The Lord of the Rings', 1, 1);
insert into comments(text, book_id) values ('1st comment', 1);
insert into comments(text, book_id) values ('2nd comment', 1);
insert into comments(text) values ('THIRD_NAME');

insert into users (username, password) values ('admin', 'password');
