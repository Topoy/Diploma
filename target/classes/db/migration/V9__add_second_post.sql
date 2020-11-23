insert into posts (id, is_active, moderation_status, moderator_id, text, `time`, title, view_count, user_id)
values ('2', '1', 'ACCEPTED', '2', 'Второй пост в блоге. Начну с того, что слуилось со мной при смене профессии', '2020-11-16 11:00:12',
'Второй пост в жизни блога', '3', (select users.id from users where id = 2));