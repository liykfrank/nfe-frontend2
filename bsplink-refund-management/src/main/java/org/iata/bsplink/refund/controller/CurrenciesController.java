package org.iata.bsplink.refund.controller;

import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.iata.bsplink.refund.fake.IsocCurrencies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/currencies")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class CurrenciesController {

    /**
     * Get currency by ISOC.
     */
    @ApiOperation(value = "Returns all isoc's currencies.")
    @GetMapping("/{isoc}")
    public ResponseEntity<List<IsocCurrencies>> getCurrencies(@PathVariable("isoc") String isoc) {

        IsocCurrencies isocCurrencies = new IsocCurrencies();

        if (isocCurrencies.getCurrenciesByIsoc1(isoc) == null
                || isocCurrencies.getCurrenciesByIsoc1(isoc).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(isocCurrencies.getCurrenciesByIsoc1(isoc));
    }

}
