create table user_authority(
    user_id     uuid not null,
    authorities varchar(255)
);

insert into app_user(id,
                     mail,
                     password,
                     firstname,
                     lastname,
                     is_active,
                     creation_date,
                     update_date) values
    ('d1e3a28b-e479-4e26-8e1a-38cbf916b5a3', 'admin@local.fr', '$2a$10$3eFJIWoXqnui5rbyLUOvlevP22F3gtwVHC2kFbXs6cFxiFskhvtLW', 'admin', 'admin', true, now(), now());

insert into user_authority(user_id, authorities) values
    ('d1e3a28b-e479-4e26-8e1a-38cbf916b5a3', 'USER'),
    ('d1e3a28b-e479-4e26-8e1a-38cbf916b5a3', 'ADMIN');


