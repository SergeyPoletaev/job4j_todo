create table if not exists categories
(
    id   serial primary key,
    name varchar not null
);

comment on table categories is 'Категории';
comment on column categories.id is 'Идентификатор категории';
comment on column categories.name is 'Наименование категории';