create table if not exists tasks
(
    id          serial primary key,
    name        varchar   not null,
    description varchar   not null,
    created     timestamp not null,
    modified    timestamp not null,
    done        boolean   not null
);

comment on table tasks is 'Задачи';
comment on column tasks.id is 'Идентификатор задачи';
comment on column tasks.name is 'Краткое описание задачи';
comment on column tasks.description is 'Полное описание задачи';
comment on column tasks.created is 'Дата и время создания задачи';
comment on column tasks.modified is 'Дата и время изменения задачи';
comment on column tasks.done is 'Статус задачи';

