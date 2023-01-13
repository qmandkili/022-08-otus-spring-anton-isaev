insert into genres(name, access_level) values ('Classic', 2);
insert into genres(name, access_level) values ('Folklore', 1);
insert into genres(name, access_level) values ('Historical', 1);
insert into genres(name, access_level) values ('Epic', 1);
insert into genres(name, access_level) values ('Fantasy', 1);
insert into authors(first_name, second_name, access_level) values ('Anton', 'Isaev', 1);
insert into authors(first_name, second_name, access_level) values ('NAME', 'NAME', 1);
insert into books(name, author_id, genre_id, access_level) values ('The Lord of the Rings', 1, 1, 1);
insert into comments(text, book_id, access_level) values ('1st comment', 1, 1);
insert into comments(text, book_id, access_level) values ('2nd comment', 1, 1);
insert into comments(text, access_level) values ('THIRD_NAME', 1);

insert into users (username, password, access_level) values ('a', '$2a$12$oQdyeKR2dEn8F/6WpgyvauNUSlE1QqnEJms3bPv/O65FOkbGPLIFq', 2);
insert into users (username, password, access_level) values ('u1', '$2a$12$oQdyeKR2dEn8F/6WpgyvauNUSlE1QqnEJms3bPv/O65FOkbGPLIFq', 1);
insert into users (username, password, access_level) values ('u2', '$2a$12$oQdyeKR2dEn8F/6WpgyvauNUSlE1QqnEJms3bPv/O65FOkbGPLIFq', 0);

insert into user_role (user_id, roles) values (1, 'ADMIN');
insert into user_role (user_id, roles) values (2, 'USER');
insert into user_role (user_id, roles) values (3, 'BANNED_USER');