CREATE SCHEMA IF NOT EXISTS filemanagement;
DROP TABLE IF EXISTS filemanagement.bsplink_file CASCADE;
DROP SEQUENCE IF EXISTS filemanagement.hibernate_sequence;

CREATE SEQUENCE filemanagement.hibernate_sequence START 1 INCREMENT 1;

CREATE TABLE filemanagement.bsplink_file (
    id INT8 NOT NULL,
    bytes INT8 NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    type VARCHAR(255),
    upload_date_time TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
