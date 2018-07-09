/*
 * Example data for development.
 */  
DELETE FROM refund.refund_issue_permission;

INSERT INTO refund.refund_issue_permission (
    id, agent_code, airline_code, iso_country_code
)
VALUES (
    nextval('refund.hibernate_sequence'), '78200010', '123', 'AA'
);

INSERT INTO refund.refund_issue_permission (
    id, agent_code, airline_code, iso_country_code
)
VALUES (
    nextval('refund.hibernate_sequence'), '78200010', '456', 'AA'
);

INSERT INTO refund.refund_issue_permission (
    id, agent_code, airline_code, iso_country_code
)
VALUES (
    nextval('refund.hibernate_sequence'), '78200021', '123', 'AA'
);

INSERT INTO refund.refund_issue_permission (
    id, agent_code, airline_code, iso_country_code
)
VALUES (
    nextval('refund.hibernate_sequence'), '78200021', '456', 'AA'
);
