DROP TABLE IF EXISTS bsplink_file CASCADE;
DROP SEQUENCE IF EXISTS hibernate_sequence;

CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

CREATE TABLE bsplink_file (
    id INT8 NOT NULL,
    bytes INT8 NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    type VARCHAR(255),
    upload_date_time TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
