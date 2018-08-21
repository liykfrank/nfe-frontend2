CREATE SCHEMA IF NOT EXISTS bsplink_user;

DROP TABLE IF EXISTS bsplink_user.user CASCADE;

DROP TABLE IF EXISTS bsplink_user.address CASCADE;

DROP SEQUENCE IF EXISTS bsplink_user.hibernate_sequence;

CREATE SEQUENCE bsplink_user.hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE bsplink_user.address(
	id bigint NOT NULL,
	description varchar(30) DEFAULT NULL,
	locality varchar(30) DEFAULT NULL,
	city varchar(30) DEFAULT NULL,
	zip varchar(10) DEFAULT NULL,
	country varchar(26) DEFAULT NULL,	
	PRIMARY KEY (id)
);

CREATE TABLE bsplink_user.user(
	id varchar(36) NOT NULL,
	username varchar(255) NOT NULL,	
	register_date timestamp NULL DEFAULT now(),
	expiry_date timestamp DEFAULT NULL,
	user_type varchar(20) NOT NULL,
	user_code varchar(10) NOT NULL,
	name varchar(49) DEFAULT NULL,
	email varchar(200) DEFAULT NULL,
	telephone varchar(15) DEFAULT NULL,
	organization varchar(30) DEFAULT NULL,
  	last_modified_date timestamp DEFAULT NULL,
  	address_id bigint DEFAULT NULL,  	
	PRIMARY KEY (id)
);

ALTER TABLE bsplink_user.user ADD CONSTRAINT USERNAME_UNIQUE UNIQUE(username);
ALTER TABLE bsplink_user.user ADD FOREIGN KEY (address_id) REFERENCES bsplink_user.address(id);