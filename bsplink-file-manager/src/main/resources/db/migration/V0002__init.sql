DROP TABLE IF EXISTS filemanagement.bsplink_config CASCADE;

CREATE TABLE filemanagement.bsplink_config (
    id CHAR(16) NOT NULL,
    config TEXT NOT NULL,
    PRIMARY KEY (id)
);
