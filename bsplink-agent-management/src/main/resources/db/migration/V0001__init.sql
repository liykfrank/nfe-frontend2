CREATE SCHEMA IF NOT EXISTS agentmanagement;

DROP TABLE IF EXISTS agentmanagement.agent CASCADE;
DROP TABLE IF EXISTS agentmanagement.form_of_payment CASCADE;

DROP SEQUENCE IF EXISTS agentmanagement.hibernate_sequence;
CREATE SEQUENCE agentmanagement.hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE agentmanagement.agent (
    iata_code VARCHAR(8) NOT NULL,
    accreditation_date DATE,
    billing_city VARCHAR(40),
    billing_country VARCHAR(80),
    billing_postal_code VARCHAR(20),
    billing_state VARCHAR(80),
    billing_street VARCHAR(255),
    iso_country_code VARCHAR(2),
    default_date DATE,
    email VARCHAR(80),
    fax VARCHAR(40),
    location_class VARCHAR(255),
    location_type VARCHAR(255),
    name VARCHAR(255),
    parent_iata_code VARCHAR(8),
    phone VARCHAR(40),
    remittance_frequency VARCHAR(255),
    status VARCHAR(40),
    tax_id VARCHAR(20),
    top_parent_iata_code VARCHAR(8),
    trade_name VARCHAR(100),
    vat_number VARCHAR(30),
    PRIMARY KEY (iata_code)
);

CREATE TABLE agentmanagement.form_of_payment (
    id bigint NOT NULL,
    iata_code VARCHAR(8) REFERENCES AGENT,
    status varchar(255) not null,
    type varchar(255) not null,
    PRIMARY KEY (id)
);
