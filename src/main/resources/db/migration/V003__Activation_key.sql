create table activation_key(
    id            uuid not null
        constraint "activation_keyPK"
            primary key,
    user_id       uuid,
    creation_date timestamp,
    constraint "activation_key_userFK"
        foreign key (user_id) references app_user(id)
)
