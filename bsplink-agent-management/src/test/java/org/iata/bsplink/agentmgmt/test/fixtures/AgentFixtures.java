package org.iata.bsplink.agentmgmt.test.fixtures;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.agentmgmt.model.entity.Agent;
import org.iata.bsplink.agentmgmt.model.entity.FormOfPayment;
import org.iata.bsplink.agentmgmt.model.entity.FormOfPaymentStatus;
import org.iata.bsplink.agentmgmt.model.entity.FormOfPaymentType;
import org.iata.bsplink.agentmgmt.model.entity.LocationClass;
import org.iata.bsplink.agentmgmt.model.entity.LocationType;
import org.iata.bsplink.agentmgmt.model.entity.RemittanceFrequency;

/**
 * Data fixtures for test use.
 */
public class AgentFixtures {

    public static final String IATA_CODE_1 = "11111111";
    public static final String IATA_CODE_2 = "22222222";

    /**
     * Gets a list of agents.
     */
    public static List<Agent> getAgentsFixture() {

        Agent agent1 = new Agent();
        agent1.setAccreditationDate(LocalDate.now());
        agent1.setBillingCity("BILLING_CITY_1");
        agent1.setBillingCountry("BILLING_COUNTRY_1");
        agent1.setBillingPostalCode("BILLING_POSTAL_CODE1");
        agent1.setBillingState("BILLING_STATE_1");
        agent1.setBillingStreet("BILLING_STREET_1");
        agent1.setIsoCountryCode("YI");
        agent1.setDefaultDate(LocalDate.now());
        agent1.setEmail("e1@mail.com");
        agent1.setFax("FAX_1");

        agent1.setFormOfPayment(Arrays.asList(
                new FormOfPayment(FormOfPaymentType.CA, FormOfPaymentStatus.ACTIVE),
                new FormOfPayment(FormOfPaymentType.CC, FormOfPaymentStatus.NON_ACTIVE),
                new FormOfPayment(FormOfPaymentType.EP, FormOfPaymentStatus.NOT_AUTHORIZED)
                ));

        agent1.setIataCode(IATA_CODE_1);
        agent1.setLocationClass(LocationClass.A);
        agent1.setLocationType(LocationType.HE);
        agent1.setName("NAME_1");
        agent1.setParentIataCode("00000044");
        agent1.setPhone("PHONE_1");
        agent1.setRemittanceFrequency(RemittanceFrequency.W);

        agent1.setTaxId("TAXID_1");
        agent1.setTopParentIataCode("00000055");
        agent1.setTradeName("TRADE_NAME_1");
        agent1.setVatNumber("VAT_NUMBER_1");

        Agent agent2 = new Agent();
        agent2.setAccreditationDate(LocalDate.now());
        agent2.setBillingCity("BILLING_CITY_2");
        agent2.setBillingCountry("BILLING_COUNTRY_2");
        agent2.setBillingPostalCode("BILLING_POSTAL_CODE2");
        agent2.setBillingState("BILLING_STATE_2");
        agent2.setBillingStreet("BILLING_STREET_2");
        agent2.setIsoCountryCode("IY");
        agent2.setDefaultDate(LocalDate.now());
        agent2.setEmail("e2@mail.com");
        agent2.setFax("FAX_2");

        agent2.setFormOfPayment(Arrays.asList(
                new FormOfPayment(FormOfPaymentType.CA, FormOfPaymentStatus.NON_ACTIVE),
                new FormOfPayment(FormOfPaymentType.CC, FormOfPaymentStatus.NON_ACTIVE),
                new FormOfPayment(FormOfPaymentType.EP, FormOfPaymentStatus.ACTIVE)
                ));

        agent2.setIataCode(IATA_CODE_2);
        agent2.setLocationClass(LocationClass.C);
        agent2.setLocationType(LocationType.HE);
        agent2.setName("NAME_2");
        agent2.setParentIataCode("00000744");
        agent2.setPhone("PHONE_2");
        agent2.setRemittanceFrequency(RemittanceFrequency.T);

        agent2.setTaxId("TAXID_2");
        agent2.setTopParentIataCode("00000755");
        agent2.setTradeName("TRADE_NAME_2");
        agent2.setVatNumber("VAT_NUMBER_2");

        return Arrays.asList(agent1, agent2);
    }
}
