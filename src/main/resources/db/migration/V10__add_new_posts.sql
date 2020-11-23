insert into posts (id, is_active, moderation_status, moderator_id, text, `time`, title, view_count, user_id)
values ('3', '1', 'ACCEPTED', '3', 'Бог года тоже любит троицу. Не спрашивайте почему, просто все так и есть', '2020-11-17 12:05:12',
'Бог кода', '4', (select users.id from users where id = 3));
insert into posts (id, is_active, moderation_status, moderator_id, text, `time`, title, view_count, user_id)
values ('4', '1', 'ACCEPTED', '4', 'Честно говоря это мой первый опыт написания поста в блоге. Хах я теперь блогер!', '2020-11-18 15:10:12',
'Первый опыт', '3', (select users.id from users where id = 4));
insert into posts (id, is_active, moderation_status, moderator_id, text, `time`, title, view_count, user_id)
values ('5', '1', 'ACCEPTED', '5', 'Почему в программированни так мало девушек? Это не нормально! Нужно срочно это менять', '2020-11-18 22:14:12',
'Дамы в коде', '3', (select users.id from users where id = 5));
insert into posts (id, is_active, moderation_status, moderator_id, text, `time`, title, view_count, user_id)
values ('6', '1', 'ACCEPTED', '6', 'Люблю писать код и путешестовать. Поэтому работать удаленно для меня стало открытием', '2020-11-19 12:06:12',
'Сказка на удаленке', '6', (select users.id from users where id = 6));
insert into posts (id, is_active, moderation_status, moderator_id, text, `time`, title, view_count, user_id)
values ('7', '1', 'ACCEPTED', '7', 'Мой первый fail на работе был такой, что любой бы мертвый суперкодер перевернулся в гробу', '2020-11-19 15:43:12',
'Перевернулся в гробу', '7', (select users.id from users where id = 7));
insert into posts (id, is_active, moderation_status, moderator_id, text, `time`, title, view_count, user_id)
values ('8', '1', 'ACCEPTED', '8', 'Люблю Мокачино и писать код в кафе Пенка. Нравится атмсофера, кофе придает сил', '2020-11-20 17:59:12',
'Кофе - друг кодера', '3', (select users.id from users where id = 8));
insert into posts (id, is_active, moderation_status, moderator_id, text, `time`, title, view_count, user_id)
values ('9', '1', 'ACCEPTED', '9', 'Когда оказываюсь в тупике и не знаю как решить проблему. Иду в зал и приседаю. Очень помогает', '2020-11-21 16:57:12',
'Присед коду голова', '3', (select users.id from users where id = 9));
insert into posts (id, is_active, moderation_status, moderator_id, text, `time`, title, view_count, user_id)
values ('10', '1', 'ACCEPTED', '10', 'Обожаю сладкое, особенно чай с медом пока пишу код. Будто вхожу в поток и все получается', '2020-11-22 11:30:12',
'Сладкий код', '9', (select users.id from users where id = 10));