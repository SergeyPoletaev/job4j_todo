create table if not exists tasks_categories
(
    id          serial primary key,
    task_id     int references tasks (id)      not null,
    category_id int references categories (id) not null
);

comment on table tasks_categories is 'Таблица связи tasks-categories';
comment on column tasks_categories.id is 'Идентификатор';
comment on column tasks_categories.task_id is 'Идентификатор из таблицы tasks';
comment on column tasks_categories.category_id is 'Наименование из таблицы categories';