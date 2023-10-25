create table action
(
    id                 uuid        not null
        constraint "actionPK" primary key,
    name               varchar(64) not null,
    runnable_file_name varchar(64) not null unique,
    runnable_exists    boolean     not null,
    creation_date      timestamp,
    update_date        timestamp
);
