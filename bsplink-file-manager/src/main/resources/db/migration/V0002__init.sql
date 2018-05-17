DROP TABLE IF EXISTS bsplink_config CASCADE;

CREATE TABLE bsplink_config (
    id CHAR(16) NOT NULL,
    config TEXT NOT NULL,
    PRIMARY KEY (id)
);
