CREATE SCHEMA IF NOT EXISTS agencymemo;
    
    drop table if exists agencymemo.acdm CASCADE;
    
    drop table if exists agencymemo.reason CASCADE;
    
    drop table if exists agencymemo.acdm_related_ticket_documents CASCADE;
    
    drop table if exists agencymemo.acdm_tax_miscellaneous_fees CASCADE;
    
    drop table if exists agencymemo.calculations CASCADE;
    
    drop table if exists agencymemo.config CASCADE;
    
    drop table if exists agencymemo.tax_on_commission_type CASCADE;
       
    DROP TABLE if exists agencymemo.bsplink_file CASCADE;
    
    DROP TABLE if exists agencymemo.comment CASCADE;
    
    DROP sequence if exists agencymemo.hibernate_sequence;
    
	create sequence agencymemo.hibernate_sequence start with 1 increment by 1;	
 
    create table agencymemo.acdm (
       id bigint not null,
        agent_code varchar(8) not null,
        airline_code varchar(3) not null,
        contact_name varchar(49),
        email varchar(100),
        phone_fax_number varchar(30),
        amount_paid_by_customer decimal(20,9) not null,
        billing_period integer not null check (billing_period<=9999999 AND billing_period>=1000000),
        concerns_indicator char(1),
        currency_code char(3),
        currency_decimals integer,
        date_of_issue date not null,
        date_of_issue_related_document date,
        iso_country_code varchar(2) not null,
        net_reporting boolean not null,
        passenger varchar(49),
        reason_for_memo text,
        reason_for_memo_issuance_code varchar(5),
        regularized boolean not null,
        statistical_code varchar(3),
        tax_on_commission_type varchar(6),
        transaction_code char(4) not null,
        agent_calculations_id bigint not null,
        airline_calculations_id bigint not null,
        agent_vat_number varchar(30),
        airline_vat_number varchar(60),
        agent_registration_number varchar(20),
        airline_registration_number varchar(20),
        primary key (id)
    );

    
    create table agencymemo.acdm_related_ticket_documents (
       acdm_id bigint not null,
        related_ticket_document_number varchar(13),
        order_nr integer not null,
        primary key (acdm_id, order_nr)
    );

    
    create table agencymemo.acdm_tax_miscellaneous_fees (
       acdm_id bigint not null,
        agent_amount decimal(20,9),
        airline_amount decimal(20,9),
        type varchar(8),
        order_nr integer not null,
        primary key (acdm_id, order_nr)
    );

    create table agencymemo.calculations (
       id bigint not null,
        commission decimal(20,9) not null,
        fare decimal(20,9) not null,
        spam decimal(20,9) not null,
        tax decimal(20,9) not null,
        tax_on_commission decimal(20,9) not null,
        primary key (id)
    );

    create table agencymemo.config (
       iso_country_code varchar(2) not null,
        agent_vat_number_enabled boolean not null,
        airline_vat_number_enabled boolean not null,
        company_registration_number_enabled boolean not null,
        cp_permitted_for_concerning_issue boolean not null,
        cp_permitted_for_concerning_refund boolean not null,
        default_currency varchar(3),
        default_stat varchar(3) not null,
        free_stat boolean not null,
        max_number_of_related_documents integer not null,
        mf_permitted_for_concerning_issue boolean not null,
        mf_permitted_for_concerning_refund boolean not null,
        nrid_and_spam_enabled boolean not null,
        tax_on_commission_enabled boolean not null,
        tax_on_commission_sign integer not null,
        primary key (iso_country_code)
    );

    
    create table agencymemo.tax_on_commission_type (
       code varchar(6) not null,
        iso_country_code varchar(2) not null,
        description varchar(50) not null,
        primary key (code, iso_country_code)
    );
    
    CREATE TABLE agencymemo.bsplink_file (
	    id serial NOT NULL,
	    name VARCHAR(255) NOT NULL,
	    path VARCHAR(255),
	    bytes INT8 NOT NULL,   
	    upload_date_time TIMESTAMP NOT NULL,
	    acdm_id int8 not null,
	    PRIMARY KEY (id)
	);
	
	CREATE TABLE agencymemo.comment(
		id serial NOT NULL,
		text VARCHAR(255) NOT NULL,
		acdm_id int8 not null,
		insert_date_time TIMESTAMP NOT NULL,
		PRIMARY KEY (id)
	);    

    alter table agencymemo.acdm
       add constraint FKbwttnt214a9vl7ei6lubeokxe 
       foreign key (agent_calculations_id) 
       references agencymemo.calculations;

    alter table agencymemo.acdm
       add constraint FK8v2byqssxjirsgx222y75gjee 
       foreign key (airline_calculations_id) 
       references agencymemo.calculations;

    alter table agencymemo.acdm_related_ticket_documents 
       add constraint FK4mjl7dkt42dxef7c2o358w02e 
       foreign key (acdm_id) 
       references agencymemo.acdm;

    
    alter table agencymemo.acdm_tax_miscellaneous_fees 
       add constraint FK3b8mvgy76tmbxcsm42nl2h2ht 
       foreign key (acdm_id) 
       references agencymemo.acdm;
       
    ALTER TABLE agencymemo.bsplink_file
   	   ADD CONSTRAINT bsplink_file_id_acdm_id 
   	   FOREIGN KEY (acdm_id) 
   	   REFERENCES agencymemo.acdm;
   	   
   	ALTER TABLE agencymemo.comment
   		ADD CONSTRAINT comment_id_acdm_id
   		FOREIGN KEY (acdm_id)
   		REFERENCES agencymemo.acdm;

    create table agencymemo.reason (
        id bigint not null,
        detail varchar(4500),
        iso_country_code varchar(2),
        title varchar(255),
        primary key (id)
    );

    create index reason_iso_country_code on agencymemo.reason (iso_country_code);
