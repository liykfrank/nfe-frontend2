DROP TABLE IF EXISTS bsplink_user.user_templates CASCADE;
drop table if exists bsplink_user.user_template CASCADE;
drop table if exists bsplink_user.user_template_iso_country_codes CASCADE;

    create table bsplink_user.user_template (
       id varchar(69) not null,
        template varchar(32),
        user_id varchar(36),
        primary key (id)
    );

    
    create table bsplink_user.user_template_iso_country_codes (
       user_template_id varchar(69) not null,
        iso_country_codes varchar(2)
    );

    
    alter table bsplink_user.user_template 
       add constraint FKjorba6gxmmveub7e51u0v6q2k 
       foreign key (template) 
       references bsplink_user.bsplink_template;

    
    alter table bsplink_user.user_template 
       add constraint FKcfsaa7nkbr6viwqqvuwhglelw 
       foreign key (user_id) 
       references bsplink_user.user;

    
    alter table bsplink_user.user_template_iso_country_codes 
       add constraint FKd4c17gsdh8y90kewuich0soj2 
       foreign key (user_template_id) 
       references bsplink_user.user_template;