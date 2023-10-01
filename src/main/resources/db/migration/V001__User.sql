create table app_user(
    id            uuid         not null
        constraint "app_userPK"
            primary key,
    mail          varchar(255) not null,
    password      varchar(255) not null,
    firstname     varchar(255),
    lastname      varchar(255),
    is_active     boolean      not null,
    creation_date timestamp,
    update_date   timestamp
);
