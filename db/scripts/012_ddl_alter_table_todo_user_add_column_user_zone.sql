alter table todo_user
    add column user_zone varchar;

comment on column todo_user.user_zone is 'ID timezone пользователя';