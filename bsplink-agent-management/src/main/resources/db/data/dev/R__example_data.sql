/*
 * Example data for development.
 */
DELETE FROM agentmanagement.form_of_payment;
DELETE FROM agentmanagement.agent;

/*
 * Agent 78200010
 */
INSERT INTO agentmanagement.agent (
  iata_code, accreditation_date, billing_city, billing_country, billing_postal_code,
  billing_state, billing_street, iso_country_code, default_date, email,
  fax, location_class, location_type, name, parent_iata_code, phone,
  remittance_frequency, status, tax_id, top_parent_iata_code, trade_name, vat_number
)
VALUES (
  '78200010', NULL, 'CITY_78200010', 'COUNTRY_78200010', 'PC_78200010',
  'STATE_78200010', 'STREET_78200010', 'AA', NULL, 'agent78200010@example.com',
  'FAX_78200010', 'A', 'AE', 'AGENT_78200010', NULL, 'PHONE_78200010',
  'W', 'STATUS_78200010', 'TAX_ID_78200010', NULL, 'TRADE_NAME_78200010', '00000078200010'
);

INSERT INTO agentmanagement.form_of_payment (
  id, iata_code, status, type
)
VALUES (
  1, '78200010', 'ACTIVE', 'CC'
);

INSERT INTO agentmanagement.form_of_payment (
  id, iata_code, status, type
)
VALUES (
  2, '78200010', 'ACTIVE', 'CA'
);

/*
 * Agent 78200021
 */
INSERT INTO agentmanagement.agent (
  iata_code, accreditation_date, billing_city, billing_country, billing_postal_code,
  billing_state, billing_street, iso_country_code, default_date, email,
  fax, location_class, location_type, name, parent_iata_code, phone,
  remittance_frequency, status, tax_id, top_parent_iata_code, trade_name, vat_number
)
VALUES (
  '78200021', NULL, 'CITY_78200021', 'COUNTRY_78200021', 'PC_78200021',
  'STATE_78200021', 'STREET_78200021', 'AA', NULL, 'agent78200021@example.com',
  'FAX_78200021', 'A', 'AE', 'AGENT_78200021', NULL, 'PHONE_78200021',
  'W', 'STATUS_78200021', 'TAX_ID_78200021', NULL, 'TRADE_NAME_78200021', '00000078200021'
);

INSERT INTO agentmanagement.form_of_payment (
  id, iata_code, status, type
)
VALUES (
  3, '78200021', 'NON_ACTIVE', 'CC'
);

INSERT INTO agentmanagement.form_of_payment (
  id, iata_code, status, type
)
VALUES (
  4, '78200021', 'ACTIVE', 'EP'
);

ALTER SEQUENCE agentmanagement.hibernate_sequence RESTART WITH 20;
