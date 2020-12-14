insert into posts (id, is_active, moderation_status, moderator_id, text, `time`, title, view_count, user_id)
values ('11', '1', 'ACCEPTED', '1', 'Здесь недавно писали про мед и код. Взрывная комбинация. Особенно когда нужно подкрепить мозг чем-то сладким',
'2020-12-01 13:28:47', 'Медовый код', '10', (select users.id from users where id = 1));