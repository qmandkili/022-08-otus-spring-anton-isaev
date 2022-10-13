insert into authors (first_name, second_name) values ('UNKNOWN', 'UNKNOWN');
insert into authors (first_name, second_name) values ('Anton', 'Isaev');
insert into authors (first_name, second_name) values ('First', 'Second');

insert into genres (name) values ('UNKNOWN');
insert into genres (name) values ('genre1');
insert into genres (name) values ('genre2');

insert into books (name, author_id, genre_id) values ('book1', 1, 1);
insert into books (name, author_id, genre_id) values ('book2', 2, 2);
insert into books (name, author_id, genre_id) values ('book3', 2, 3);
insert into books (name, author_id, genre_id) values ('book4', 3, 3);
