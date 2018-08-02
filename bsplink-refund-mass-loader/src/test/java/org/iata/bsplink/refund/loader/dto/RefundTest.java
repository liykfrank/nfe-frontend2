package org.iata.bsplink.refund.loader.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.iata.bsplink.refund.loader.test.Tools.getObjectMapper;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.json.JacksonTester;

public class RefundTest {

    private JacksonTester<Refund> json;

    @Before
    public void setup() {

        ObjectMapper objectMapper = getObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void testDoesNotSerializeTransactionNumber() throws Exception {

        Refund refund = new Refund();
        refund.setTransactionNumber("000001");

        assertThat(this.json.write(refund)).doesNotHaveJsonPathValue("@.transactionNumber");
    }

    @Test
    public void testSerializesDatesToIsoFormat() throws Exception {

        String stringDate = "2018-01-01";
        LocalDate date = LocalDate.parse(stringDate);

        Refund refund = new Refund();
        refund.setDateOfAirlineAction(date);
        refund.setDateOfIssueRelatedDocument(date);
        refund.setDateOfIssue(date);

        assertThat(this.json.write(refund))
                .extractingJsonPathStringValue("@.dateOfAirlineAction")
                .isEqualTo(stringDate);

        assertThat(this.json.write(refund))
                .extractingJsonPathStringValue("@.dateOfIssue")
                .isEqualTo(stringDate);

        assertThat(this.json.write(refund))
                .extractingJsonPathStringValue("@.dateOfIssueRelatedDocument")
                .isEqualTo(stringDate);
    }

    @Test
    public void testPartialRefundDefaultValueIsFalse() {

        Refund refund = new Refund();
        assertFalse(refund.getPartialRefund());
    }

}
