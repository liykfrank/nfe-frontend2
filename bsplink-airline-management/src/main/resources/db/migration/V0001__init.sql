CREATE SCHEMA IF NOT EXISTS airlinemanagement;

DROP TABLE IF EXISTS airlinemanagement.airline CASCADE ;

CREATE TABLE airlinemanagement.airline (
    airline_code VARCHAR(3) NOT NULL,
    iso_country_code varchar(2) NOT NULL,
    designator VARCHAR(2) NOT NULL,
    from_date DATE NOT NULL,
    global_name VARCHAR(255) NOT NULL,
    address1 VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),
    postal_code VARCHAR(255),
    state VARCHAR(255),
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    telephone VARCHAR(255),
    tax_number VARCHAR(255),
    to_date DATE,
    PRIMARY KEY (airline_code, iso_country_code)
);
