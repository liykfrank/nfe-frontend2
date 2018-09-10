
    drop table if exists bsplink_user.user_region_iso_country_codes CASCADE;
    drop table if exists bsplink_user.user_region CASCADE;
    drop table if exists bsplink_user.user_preferences CASCADE;
    
    alter table bsplink_user.user
       add column preferences_id integer;
     
    create table bsplink_user.user_preferences (
       id integer not null,
        language varchar(255),
        time_zone varchar(255),
        primary key (id)
    );
    
    create table bsplink_user.user_region (
       id integer not null,
        name varchar(255),
        is_default boolean not null,
        user_preferences_id integer,
        primary key (id)
    );
    
    create table bsplink_user.user_region_iso_country_codes (
       user_region_id integer not null,
        iso_country_codes varchar(2)
    );
 
    alter table bsplink_user.user 
       add constraint FKddefmvbrws3hvl5t0hnnsv8ox 
       foreign key (address_id) 
       references bsplink_user.address;
    
    alter table bsplink_user.user 
       add constraint FK79dj7o015q3xbh7328v8kw5xk 
       foreign key (preferences_id) 
       references bsplink_user.user_preferences;

    alter table bsplink_user.user_region 
       add constraint FKmrbn0mv7h57r9mgye6stix0ks 
       foreign key (user_preferences_id) 
       references bsplink_user.user_preferences;

    alter table bsplink_user.user_region_iso_country_codes 
       add constraint FKq28nhcga3ov89e37vpkljl1ki 
       foreign key (user_region_id) 
       references bsplink_user.user_region;
