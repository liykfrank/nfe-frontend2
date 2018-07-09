package org.iata.bsplink.refund.loader.restclient.mock;

import lombok.extern.java.Log;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.restclient.RefundClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * This is a mock aimed to facilitate development.
 */
@Component
@Profile("mock")
@Log
public class MockRefundClient implements RefundClient {

    @Override
    public ResponseEntity<Refund> findRefund(String isoCountryCode, String airlineCode,
            String ticketDocumentNumber) {

        log.info(String.format("calling: findRefund(%s, %s, %s);", isoCountryCode, airlineCode,
                ticketDocumentNumber));

        return mockResponse(isoCountryCode, airlineCode,ticketDocumentNumber);
    }

    private ResponseEntity<Refund> mockResponse(String isoCountryCode, String airlineCode,
            String ticketDocumentNumber) {

        Refund refund = new Refund();

        refund.setId((long)1);
        refund.setIsoCountryCode(isoCountryCode);
        refund.setAirlineCode(airlineCode);
        refund.setTicketDocumentNumber(ticketDocumentNumber);

        return ResponseEntity.ok().body(refund);
    }

    @Override
    public void updateRefund(Long refundId, Refund refund) {

        log.info(String.format("calling: updateRefund(%s, %s);", refundId, refund));
    }

}