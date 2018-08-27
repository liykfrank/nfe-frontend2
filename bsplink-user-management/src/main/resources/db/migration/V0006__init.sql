alter table bsplink_user.user
       drop column lastname;

alter table bsplink_user.user
       add column last_name varchar(100) DEFAULT NULL;