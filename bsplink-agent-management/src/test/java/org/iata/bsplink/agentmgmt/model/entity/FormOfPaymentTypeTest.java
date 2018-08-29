package org.iata.bsplink.agentmgmt.model.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FormOfPaymentTypeTest {

    /**
     * Bug NFE-483/NFEIATA-293 - MSCA and MSCC payments are not allowed.
     *
     * <p>
     * See:
     * https://nfe-bsplink.atlassian.net/browse/NFEIATA-293
     * https://iatasfdc.atlassian.net/browse/NFE-483
     * </p>
     */
    @Test
    public void testHasExpectedFormOfPayments() {

        assertThat(FormOfPaymentType.valueOf("CC"), equalTo(FormOfPaymentType.CC));
        assertThat(FormOfPaymentType.valueOf("CA"), equalTo(FormOfPaymentType.CA));
        assertThat(FormOfPaymentType.valueOf("EP"), equalTo(FormOfPaymentType.EP));
        assertThat(FormOfPaymentType.valueOf("MSCC"), equalTo(FormOfPaymentType.MSCC));
        assertThat(FormOfPaymentType.valueOf("MSCA"), equalTo(FormOfPaymentType.MSCA));
    }

}
