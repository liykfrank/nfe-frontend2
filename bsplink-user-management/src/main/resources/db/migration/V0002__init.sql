    
DROP TABLE IF EXISTS bsplink_user.bsplink_template CASCADE;

DROP TABLE IF EXISTS bsplink_user.bsplink_option CASCADE;

    create table bsplink_user.bsplink_option (
       id varchar(32) not null,
        primary key (id)
    );

    
    create table bsplink_user.bsplink_option_user_types (
       bsplink_option_id varchar(32) not null,
        user_types varchar(255) not null
    );

    
    create table bsplink_user.bsplink_template (
       id varchar(32) not null,
        primary key (id)
    );

    
    create table bsplink_user.bsplink_template_options (
       bsplink_template_id varchar(32) not null,
        options_id varchar(32) not null
    );

    
    create table bsplink_user.bsplink_template_user_types (
       bsplink_template_id varchar(32) not null,
        user_types varchar(255) not null
    );
    
    
    alter table bsplink_user.bsplink_template_options 
       add constraint UK1g2s2fcr6oigjbq1g02siidw3 unique (bsplink_template_id, options_id);

    
    alter table bsplink_user.bsplink_option_user_types 
       add constraint FKkbep49d5v9r5179ol53dmuc6l 
       foreign key (bsplink_option_id) 
       references bsplink_user.bsplink_option;

    
    alter table bsplink_user.bsplink_template_options 
       add constraint FKg579vi7oh0dlydbfii3tvl5rh 
       foreign key (options_id) 
       references bsplink_user.bsplink_option;

    
    alter table bsplink_user.bsplink_template_options 
       add constraint FKhnjfwpq3vetevsia82k4jwm4l 
       foreign key (bsplink_template_id) 
       references bsplink_user.bsplink_template;

    
    alter table bsplink_user.bsplink_template_user_types 
       add constraint FKay33kv0342tg0v33jopqo7ui1 
       foreign key (bsplink_template_id) 
       references bsplink_user.bsplink_template;