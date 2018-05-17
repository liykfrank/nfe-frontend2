CREATE SCHEMA IF NOT EXISTS sftpaccountmanager ;

DROP TABLE IF EXISTS sftpaccountmanager.account CASCADE;

CREATE TABLE sftpaccountmanager.account(
    login VARCHAR(255) NOT NULL,
    creation_time TIMESTAMP NOT NULL,
    mode VARCHAR(255) NOT NULL,
    public_key VARCHAR(3000),
    status VARCHAR(255) NOT NULL,
    updated_time TIMESTAMP NOT NULL,
    password CHAR(60),
    PRIMARY KEY(login)
);