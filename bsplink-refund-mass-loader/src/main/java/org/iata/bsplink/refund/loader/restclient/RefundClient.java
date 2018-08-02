package org.iata.bsplink.refund.loader.restclient;

import feign.Response;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.dto.RefundStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Profile("!mock")
@FeignClient(name = "refund", url = "${feign.refund.url}", decode404 = true)
public interface RefundClient {

    @GetMapping("/indirects")
    ResponseEntity<Refund> findRefund(
            @RequestParam(value = "isoCountryCode") String isoCountryCode,
            @RequestParam(value = "airlineCode") String airlineCode,
            @RequestParam(value = "ticketDocumentNumber") String ticketDocumentNumber);

    @PutMapping(value = "/indirects/{refundId}", params = "fileName")
    Response updateRefund(
            @PathVariable("refundId") Long refundId,
            @RequestParam(value = "fileName") String fileName,
            Refund refund);

    @PostMapping(value = "/indirects/{refundId}/status", params = "fileName")
    Response updateStatus(
            @PathVariable("refundId") Long refundId,
            @RequestParam(value = "fileName") String fileName,
            RefundStatusRequest refundStatusRequest);
}