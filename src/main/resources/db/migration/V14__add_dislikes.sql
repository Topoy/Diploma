insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('26', '2020-12-01 13:47:05', '-1', (select posts.id from posts where id = 1), (select users.id from users where id = 5));

insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('27', '2020-12-02 14:47:05', '-1', (select posts.id from posts where id = 2), (select users.id from users where id = 3));
insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('28', '2020-12-02 15:15:05', '-1', (select posts.id from posts where id = 2), (select users.id from users where id = 7));

insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('29', '2020-12-04 11:53:05', '-1', (select posts.id from posts where id = 3), (select users.id from users where id = 10));

insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('30', '2020-12-04 15:01:05', '-1', (select posts.id from posts where id = 4), (select users.id from users where id = 6));

insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('31', '2020-12-05 02:11:05', '-1', (select posts.id from posts where id = 5), (select users.id from users where id = 1));
insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('32', '2020-12-05 13:03:05', '-1', (select posts.id from posts where id = 5), (select users.id from users where id = 10));

insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('33', '2020-12-08 15:29:05', '-1', (select posts.id from posts where id = 8), (select users.id from users where id = 3));

insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('34', '2020-12-10 23:07:05', '-1', (select posts.id from posts where id = 9), (select users.id from users where id = 11));

insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('35', '2020-12-11 11:23:05', '-1', (select posts.id from posts where id = 10), (select users.id from users where id = 9));


