create table article
(
    id            uuid         not null
        constraint "articlePK"
            primary key,
    title         varchar(128) not null,
    content       text         not null,
    category      varchar(255) not null unique,
    creation_date timestamp,
    update_date   timestamp
);

insert into article(id, title, content, category, creation_date, update_date)
values ('81949274-b09b-43dc-acf0-0565edf285bc', 'Home', 'This is the home page!', 'HOME', now(), now()),
       ('8364e747-1947-47aa-a7db-0ccf5dac6179', 'Links', 'This is the list of public links!', 'PUBLIC_LINKS', now(), now()),
       ('3a7cfc53-e0c7-48d3-b279-105079bcdbff', 'Internal links', 'This is the list of private links!', 'PRIVATE_LINKS', now(), now());




