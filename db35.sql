create table history
(
    user_id varchar(45) not null
        primary key,
    history text        not null,
    date    datetime    null
)
    collate = utf8mb3_unicode_ci;

create table users
(
    id       int auto_increment
        primary key,
    username varchar(255) not null,
    password varchar(255) not null,
    constraint username
        unique (username)
);


