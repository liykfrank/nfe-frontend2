

alter table bsplink_user.user
       add column lastName varchar(100) DEFAULT NULL;

 
 DROP TABLE IF EXISTS bsplink_user.user_templates CASCADE;
 DROP TABLE IF EXISTS bsplink_user.user_template CASCADE;
 DROP TABLE IF EXISTS bsplink_user.user_template_iso_country_codes CASCADE;
 
    create table bsplink_user.user_templates (
       user_id varchar(255) not null,
        templates_id bigint not null
    );

    
    create table bsplink_user.user_template (
       id bigint not null,
        template_id varchar(32) not null,
        primary key (id)
    );

    
    create table bsplink_user.user_template_iso_country_codes (
       user_template_id bigint not null,
        iso_country_codes varchar(2)
    );

    
    alter table bsplink_user.user_templates 
       add constraint UK_ajuvslvlwq152melxswvagyx8 unique (templates_id);
  
    
    
    alter table bsplink_user.user_templates 
       add constraint FKfn71u5iawo1yijvm1npqx5bsu 
       foreign key (templates_id) 
       references bsplink_user.user_template;

    
    alter table bsplink_user.user_templates 
       add constraint FKjn7hsagts7u1pqnsfqj5pi945 
       foreign key (user_id) 
       references bsplink_user.user;

    
    alter table bsplink_user.user_template 
       add constraint FK6o2cui3gel6vxtb00v64ldre6 
       foreign key (template_id) 
       references bsplink_user.bsplink_template;

    
    alter table bsplink_user.user_template_iso_country_codes 
       add constraint FKd4c17gsdh8y90kewuich0soj2 
       foreign key (user_template_id) 
       references bsplink_user.user_template;

       