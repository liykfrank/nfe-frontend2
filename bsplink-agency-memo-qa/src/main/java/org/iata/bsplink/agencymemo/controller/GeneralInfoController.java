package org.iata.bsplink.agencymemo.controller;

import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.iata.bsplink.agencymemo.fake.IsocCurrencies;
import org.iata.bsplink.agencymemo.fake.Period;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/general-info")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GeneralInfoController {

    /**
     * Get all Periods.
     */
    @ApiOperation(value = "Returns all periods.")
    @GetMapping("/periods")
    public ResponseEntity<List<Period>> getAllPeriods() {

        return ResponseEntity.status(HttpStatus.OK).body(Period.getPeriodsList());
    }

    /**
     * Get period by ISOC.
     */
    @ApiOperation(value = "Returns all isoc's periods.")
    @GetMapping("/periods/{isoc}")
    public ResponseEntity<Period> getPeriods(@PathVariable("isoc") String isoc) {

        Period period = new Period();
        period.setPeriod(isoc);

        if (period.getValues() == null || period.getValues().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(period);
    }

    /**
     * Get all currencies.
     */
    @ApiOperation(value = "Returns all currencies.")
    @GetMapping("/currencies")
    public ResponseEntity<List<IsocCurrencies>> getAllCurrencies() {
        return ResponseEntity.status(HttpStatus.OK).body(IsocCurrencies.getAllCurrencies());
    }


    /**
     * Get currency by ISOC.
     */
    @ApiOperation(value = "Returns all isoc's currencies.")
    @GetMapping("/currencies/{isoc}")
    public ResponseEntity<List<IsocCurrencies>> getCurrencies(@PathVariable("isoc") String isoc) {

        IsocCurrencies isocCurrencies = new IsocCurrencies();

        if (isocCurrencies.getCurrenciesByIsoc1(isoc) == null
                || isocCurrencies.getCurrenciesByIsoc1(isoc).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(isocCurrencies.getCurrenciesByIsoc1(isoc));
    }

}
