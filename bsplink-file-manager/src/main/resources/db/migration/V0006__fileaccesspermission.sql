drop table if exists filemanagement.file_access_permission;

    create table filemanagement.file_access_permission (
       id bigint not null,
        access varchar(8) not null,
        file_type varchar(2) not null,
        iso_country_code varchar(2) not null,
        user_id varchar(36) not null,
        primary key (id)
    );
