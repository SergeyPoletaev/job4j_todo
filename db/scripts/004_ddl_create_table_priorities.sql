create table if not exists priorities
(
    id       serial primary key,
    name     varchar unique not null,
    position int            not null
);

comment on table priorities is 'Приоритет задачи';
comment on column priorities.id is 'Идентификатор приоритета';
comment on column priorities.name is 'Наименование приоритета';
comment on column priorities.position is 'Числовое обозначение приоритета';