package org.iata.bsplink.refund.loader.restclient;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Profile("!mock")
@FeignClient(name = "refund", url = "${feign.refund.url}", decode404 = true)
public interface RefundClient {

    @GetMapping("/indirects/?isoCountryCode={isoCountryCode}"
            + "&airlineCode={airlineCode}"
            + "&ticketDocumentNumber={ticketDocumentNumber}")
    ResponseEntity<Refund> findRefund(
            @RequestParam(value = "isoCountryCode") String isoCountryCode,
            @RequestParam(value = "airlineCode") String airlineCode,
            @RequestParam(value = "ticketDocumentNumber") String ticketDocumentNumber);

    @PutMapping("/indirects/{refundId}")
    void updateRefund(@PathVariable("refundId") Long refundId, Refund refund);

}