package org.iata.bsplink.agencymemo.test.fixtures;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.agencymemo.model.ConcernsIndicator;
import org.iata.bsplink.agencymemo.model.TransactionCode;
import org.iata.bsplink.agencymemo.model.entity.Acdm;
import org.iata.bsplink.agencymemo.model.entity.AcdmCurrency;
import org.iata.bsplink.agencymemo.model.entity.RelatedTicketDocument;
import org.iata.bsplink.agencymemo.model.entity.TaxMiscellaneousFee;

public class AcdmFixtures {

    /**
     * Returns a list of ACDMs.
     */
    public static List<Acdm> getAcdms() {
        Acdm acdm1 = new Acdm();
        acdm1.setAgentCode("78200102");
        acdm1.setAirlineCode("220");

        acdm1.getAirlineContact().setContactName("Pepe");
        acdm1.getAirlineContact().setEmail("pepe@email.com");
        acdm1.getAirlineContact().setPhoneFaxNumber("123");

        acdm1.setBillingPeriod(2010121);
        acdm1.setConcernsIndicator(ConcernsIndicator.I);

        AcdmCurrency currency = new AcdmCurrency();
        currency.setCode("ABC");
        currency.setDecimals(2);
        acdm1.setCurrency(currency);

        acdm1.setAmountPaidByCustomer(BigDecimal.valueOf(10D));
        acdm1.setDateOfIssue(LocalDate.of(2010, 10, 27));
        acdm1.getAirlineCalculations().setFare(BigDecimal.valueOf(10D));
        acdm1.getAirlineCalculations().setTax(BigDecimal.valueOf(10D));
        acdm1.getAgentCalculations().setTax(BigDecimal.valueOf(10D));
        acdm1.setIsoCountryCode("AA");
        acdm1.setStatisticalCode("I");
        acdm1.setTransactionCode(TransactionCode.ADMA);

        RelatedTicketDocument relatedTicketDocument1 = new RelatedTicketDocument();
        relatedTicketDocument1.setRelatedTicketDocumentNumber("2201234567890");
        acdm1.getRelatedTicketDocuments().add(relatedTicketDocument1);

        RelatedTicketDocument relatedTicketDocument2 = new RelatedTicketDocument();
        relatedTicketDocument2.setRelatedTicketDocumentNumber("2201234567891");
        acdm1.getRelatedTicketDocuments().add(relatedTicketDocument2);

        TaxMiscellaneousFee taxMiscellaneousFee = new TaxMiscellaneousFee();
        taxMiscellaneousFee.setType("QZ");
        taxMiscellaneousFee.setAgentAmount(BigDecimal.valueOf(10D));
        taxMiscellaneousFee.setAirlineAmount(BigDecimal.valueOf(10D));

        acdm1.getTaxMiscellaneousFees().add(taxMiscellaneousFee);

        Acdm acdm2 = new Acdm();
        acdm2.setAgentCode("78200011");
        acdm2.setAirlineCode("220");

        acdm2.getAirlineContact().setContactName("Mary");
        acdm2.getAirlineContact().setEmail("mary@email.com");
        acdm2.getAirlineContact().setPhoneFaxNumber("32168");

        acdm2.setBillingPeriod(2010122);
        acdm2.setConcernsIndicator(ConcernsIndicator.R);

        AcdmCurrency currency2 = new AcdmCurrency();
        currency2.setCode("EUR");
        currency2.setDecimals(2);
        acdm2.setCurrency(currency2);

        acdm2.setAmountPaidByCustomer(BigDecimal.valueOf(15D));
        acdm2.setDateOfIssue(LocalDate.of(2010, 10, 27));
        acdm2.getAirlineCalculations().setFare(BigDecimal.valueOf(15D));
        acdm2.setIsoCountryCode("AA");
        acdm2.setStatisticalCode("D");
        acdm2.setTransactionCode(TransactionCode.ACMA);

        return Arrays.asList(acdm1, acdm2);
    }

}
