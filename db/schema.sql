create table if not exists person(
     id serial primary key,
     login text,
     password text
);

create table if not exists room(
     id serial primary key,
     name text default 'unknown'
);

create table if not exists message(
     id serial primary key,
     text text default '',
     author_id int references person(id),
     room_id int references room(id)
);

create table if not exists person_rooms (
     room_id int references room(id),
     person_id int references person(id)
);

CREATE TABLE IF NOT EXISTS role (
     id serial primary key,
     role varchar(50)
);

CREATE TABLE IF NOT EXISTS person_roles (
     person_id int references person(id),
     role_id int references role(id)
);
