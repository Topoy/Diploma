insert into post_comments (id, text, `time`, post_id, user_id)
values ('1', 'Разделяю ваши надежды', '2020-12-01 08:14:43', (select posts.id from posts where id = 1), (select users.id from users where id = 4));
