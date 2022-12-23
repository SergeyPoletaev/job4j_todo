insert into todo_user(name, login, password)
values ('Неизвестный Бурундук', '***', '***');

alter table tasks
    add column user_id int references todo_user (id);

update tasks
set user_id = (select id from todo_user where login = '***');

alter table tasks
    alter column user_id set not null;

comment on column tasks.user_id is 'Идентификатор пользователя создавшего задачу';