insert into post_comments (id, text, `time`, post_id, user_id)
values ('2', 'А продолжение то будет?', '2020-12-02 10:45:43', (select posts.id from posts where id = 2), (select users.id from users where id = 7));
insert into post_comments (id, parent_id, text, `time`, post_id, user_id)
values ('3', '2', 'Ага, вроде бы начал, а дальше что было?', '2020-12-01 08:14:43', (select posts.id from posts where id = 2), (select users.id from users where id = 5));

