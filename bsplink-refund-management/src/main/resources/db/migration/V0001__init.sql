CREATE SCHEMA IF NOT EXISTS refund;
    
    DROP TABLE IF EXISTS refund.bsplink_file CASCADE;
    
    DROP TABLE IF EXISTS refund.comment CASCADE;   		   
 
    drop table if exists refund.contact CASCADE;

    
    drop table if exists refund.refund CASCADE;

    
    drop table if exists refund.refund_conjunctions CASCADE;

    
    drop table if exists refund.refund_form_of_payment_amounts CASCADE;

    drop table  if exists refund.refund_tax_miscellaneous_fees CASCADE;

	drop table if exists refund.refund_issue_permission CASCADE;
	
	drop table if exists refund.config CASCADE;
	
	drop table if exists refund.reason CASCADE;

    drop sequence if exists refund.hibernate_sequence;

	create sequence refund.hibernate_sequence start with 1 increment by 1;
    
    create table refund.contact (
       id bigint not null,
        contact_name varchar(49) not null,
        email varchar(100) not null,
        phone_fax_number varchar(30) not null,
        primary key (id)
    );
    
    create table refund.refund (
       id bigint not null,
        agent_code varchar(8) not null,
        agent_registration_number varchar(20),
        agent_vat_number varchar(30),
        airline_code varchar(3),
        airline_code_related_document varchar(3),
        airline_registration_number varchar(20),
        airline_remark varchar(500),
        airline_vat_number varchar(60),
        cancellation_penalty decimal(20,9) not null,
        commission_amount decimal(20,9) not null,
        commission_on_cp_and_mf_amount decimal(20,9) not null,
        commission_on_cp_and_mf_rate decimal(4,2) not null,
        commission_rate decimal(4,2) not null,
        gross_fare decimal(20,9) not null,
        less_gross_fare_used decimal(20,9) not null,
        miscellaneous_fee decimal(20,9) not null,
        refund_to_passenger decimal(20,9) not null,
        spam decimal(20,9) not null,
        tax decimal(20,9) not null,
        tax_on_cancellation_penalty decimal(20,9) not null,
        tax_on_miscellaneous_fee decimal(20,9) not null,
        billing_period integer check (billing_period<=9999999 AND billing_period>=1000000),
        currency_code varchar(3) not null,
        currency_decimals integer not null check (currency_decimals<=9),
        customer_file_reference varchar(27),
        date_of_airline_action date,
        date_of_issue date not null,
        date_of_issue_related_document date,
        exchange boolean,
        iso_country_code varchar(2) not null,
        issue_reason varchar(500),
        net_reporting boolean not null,
        original_agent_code varchar(8),
        original_airline_code varchar(3),
        original_date_of_issue date,
        original_location_city_code varchar(3),
        original_ticket_document_number varchar(10),
        partial_refund boolean not null,
        passenger varchar(49),
        rejection_reason varchar(500),
        related_ticket_coupon1 boolean not null,
        related_ticket_coupon2 boolean not null,
        related_ticket_coupon3 boolean not null,
        related_ticket_coupon4 boolean not null,
        related_ticket_document_number varchar(10) not null,
        settlement_authorisation_code varchar(14),
        statistical_code varchar(3) not null,
        status varchar(19) not null,
        tour_code varchar(15),
        waiver_code varchar(14),
        agent_contact_id bigint,
        airline_contact_id bigint,
        primary key (id)
    );

    
    create table refund.refund_conjunctions (
       refund_id bigint not null,
        related_ticket_coupon1 boolean not null,
        related_ticket_coupon2 boolean not null,
        related_ticket_coupon3 boolean not null,
        related_ticket_coupon4 boolean not null,
        related_ticket_document_number varchar(10),
        order_nr integer not null,
        primary key (refund_id, order_nr)
    );
    
    create table refund.refund_issue_permission (
       id bigint not null,
        agent_code varchar(8) not null,
        airline_code varchar(3) not null,
        iso_country_code varchar(2) not null,
        primary key (id)
    );
    
    create table refund.refund_form_of_payment_amounts (
       refund_id bigint not null,
        amount decimal(20,9),
        number varchar(20),
        type varchar(4),
        vendor_code varchar(2),
        order_nr integer not null,
        primary key (refund_id, order_nr)
    );

    
    
    create table refund.refund_tax_miscellaneous_fees (
       	refund_id bigint not null,
        amount decimal(20,9),
        type varchar(8),
        order_nr integer not null,
        primary key (refund_id, order_nr)
    );

    create table refund.config (
        iso_country_code varchar(2) not null,
        agent_vat_number_enabled boolean not null,
        airline_mf_and_cp_configuration_allowed boolean not null,
        airline_vat_number_enabled boolean not null,
        all_gds_agents_issue_refund_notice_allowed boolean not null,
        commission_on_cp_and_mf_enabled boolean not null,
        company_registration_number_enabled boolean not null,
        credit_on_direct_refunds_enabled boolean not null,
        credit_on_indirect_refunds_enabled boolean not null,
        default_currency varchar(3),
        default_stat varchar(3) not null,
        easy_pay_enabled boolean not null,
        electronic_ticket_validations_enabled boolean not null,
        fare_adjustment_amount_enabled boolean not null,
        free_stat boolean not null,
        handling_fee_enabled boolean not null,
        issue_refunds_without_coupons_allowed boolean not null,
        mf_amount decimal(20,9) not null,
        mixed_taxes_allowed boolean not null,
        nr_refunds_allowed boolean not null,
        penalty_charge_enabled boolean not null,
        refund_notice_number_considered boolean not null,
        tctp_for_vat_on_cp varchar(6),
        tctp_for_vat_on_mf varchar(6),
        vat_on_mf_and_vat_on_cp_enabled boolean not null,
        max_coupons_in_related_documents integer not null,
        max_conjunctions integer not null,
        primary key (iso_country_code)
    );
    
   	CREATE TABLE refund.bsplink_file (
	    id BIGINT NOT NULL,
	    name VARCHAR(255) NOT NULL,
	    path VARCHAR(255),
	    bytes INT8 NOT NULL,   
	    upload_date_time TIMESTAMP NOT NULL,
	    refund_id BIGINT,
	    PRIMARY KEY (id)
	);
	
	CREATE TABLE refund.comment(
		id BIGINT NOT NULL,
		text VARCHAR(255) NOT NULL,
		insert_date_time TIMESTAMP NOT NULL,
		refund_id BIGINT,
		PRIMARY KEY (id)
	);
	
	CREATE TABLE refund.refund_history(
		id BIGINT NOT NULL,
		refund_id BIGINT,
		insert_date_time TIMESTAMP NOT NULL,
		action VARCHAR(255),
		file_name VARCHAR(255),
		PRIMARY KEY (id)
	);
	
	create table refund.reason (
       id bigint not null,
        detail varchar(500) not null,
        iso_country_code varchar(2) not null,
        title varchar(255) not null,
        type integer not null,
        primary key (id)
    );
	

    ALTER TABLE refund.bsplink_file
   	   ADD CONSTRAINT bsplink_file_id_refund_id 
   	   FOREIGN KEY (refund_id) 
   	   REFERENCES refund.refund;
   	   
   	ALTER TABLE refund.comment
   		ADD CONSTRAINT comment_id_refund_id
   		FOREIGN KEY (refund_id)
   		REFERENCES refund.refund;
   		
   	ALTER TABLE refund.refund_history
   		ADD CONSTRAINT refund_history_refund_id
   		FOREIGN KEY (refund_id)
   		REFERENCES refund.refund;
    
    alter table refund.refund 
       add constraint FKq835wniwaeoc5d1puk21fgy2f 
       foreign key (agent_contact_id) 
       references refund.contact;

    
    alter table refund.refund 
       add constraint FKruml3jv9elgw28wrhc23uy363 
       foreign key (airline_contact_id) 
       references refund.contact;

    
    alter table refund.refund_conjunctions 
       add constraint FK25dp3ht85u0hj4f6qfym1vddd 
       foreign key (refund_id) 
       references refund.refund;

    
    alter table refund.refund_form_of_payment_amounts 
       add constraint FK9vbu11esbbe7k2js63pqqwvm9 
       foreign key (refund_id) 
       references refund.refund;

    
    alter table refund.refund_tax_miscellaneous_fees 
       add constraint FKe3idqul203gilvqct27w5wqjt 
       foreign key (refund_id) 
       references refund.refund;
       
   alter table refund.refund_issue_permission 
   	add constraint UKolovqplm7oplte9d31ucdt347 unique (agent_code, airline_code);
       
