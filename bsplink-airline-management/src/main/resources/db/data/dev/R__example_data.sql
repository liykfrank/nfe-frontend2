/*
 * Example data for development.
 */
DELETE FROM airlinemanagement.airline;

INSERT INTO airlinemanagement.airline (
  airline_code, iso_country_code, designator, from_date, global_name, address1,
  city, country, postal_code, state, email,
  first_name, last_name, telephone, tax_number, to_date
)
VALUES (
  '123', 'AA', 'D1', '2018-01-01', 'AIRLINE_123', 'ADDRESS_123',
  'CITY_123', 'COUNTRY_123', 'PC_123', 'STATE_123', 'airline123@example.com',
  'FIRST_NAME_123', 'LAST_NAME_123', 'PHONE_123', '000000123', NULL
);

INSERT INTO airlinemanagement.airline (
  airline_code, iso_country_code, designator, from_date, global_name, address1,
  city, country, postal_code, state, email,
  first_name, last_name, telephone, tax_number, to_date
)
VALUES (
  '456', 'BB', 'D2', '2018-02-02', 'AIRLINE_456', 'ADDRESS_456',
  'CITY_456', 'COUNTRY_456', 'PC_456', 'STATE_456', 'airline456@example.com',
  'FIRST_NAME_456', 'LAST_NAME_456', 'PHONE_456', '000000456', NULL
);
