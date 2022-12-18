create table if not exists todo_user
(
    id       serial primary key,
    name     varchar not null,
    login    varchar not null unique,
    password varchar not null
);

comment on table todo_user is 'Пользователи';
comment on column todo_user.id is 'Идентификатор пользователя';
comment on column todo_user.name is 'Имя пользователя';
comment on column todo_user.login is 'Логин пользователя';
comment on column todo_user.password is 'Пароль пользователя';