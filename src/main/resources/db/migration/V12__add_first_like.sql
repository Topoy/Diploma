insert into post_votes (id, `time`, `value`, post_id, user_id)
values ('1', '2020-11-16 14:05:13', '1', (select posts.id from posts where id = 1), (select users.id from users where id = 2));