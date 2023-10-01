create table forgotten_password_token(
    id            uuid not null
        constraint "forgotten_password_tokenPK"
            primary key,
    user_id       uuid,
    creation_date timestamp,
    constraint "forgotten_password_tokenFK"
        foreign key (user_id) references app_user(id)
)
