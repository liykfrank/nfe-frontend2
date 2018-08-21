package org.iata.bsplink.agencymemo.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.iata.bsplink.agencymemo.model.entity.Reason;
import org.iata.bsplink.agencymemo.service.ReasonService;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reasons")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class ReasonController {

    @Autowired
    private ReasonService reasonService;

    /**
     * Creates a new reason and returns it.
     */
    @PostMapping
    public ResponseEntity<Reason> createReason(@Valid @RequestBody Reason reason, Errors errors) {

        if (errors.hasErrors()) {

            throw new ApplicationValidationException(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(reasonService.save(reason));
    }

    @GetMapping
    public ResponseEntity<List<Reason>> getReasons() {

        return ResponseEntity.status(HttpStatus.OK).body(reasonService.findAll());
    }

    /**
     * Returns Reasons defined in the indicated country.
     */
    @GetMapping(params = "isoCountryCode")
    public ResponseEntity<List<Reason>> getReasonsByIsoc(@RequestParam String isoCountryCode) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(reasonService.findByIsoCountryCode(isoCountryCode));
    }

    /**
     * Deletes a Reason by id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Reason> deleteReason(
            @PathVariable("id") Optional<Reason> optionalReason) {

        if (!optionalReason.isPresent()) {

            return ResponseEntity.notFound().build();
        }

        reasonService.delete(optionalReason.get());

        return ResponseEntity.status(HttpStatus.OK).body(optionalReason.get());
    }

    /**
     * Updates a reason.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reason> updateReason(@PathVariable("id") Long id,
            @RequestBody Reason reason) {

        reason.setId(id);

        Optional<Reason> optionalReason = reasonService.update(reason);

        if (!optionalReason.isPresent()) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(reasonService.save(optionalReason.get()));
    }

    /**
     * Retrieves a Reason by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reason> getReason(@PathVariable("id") Optional<Reason> optionalReason) {

        if (!optionalReason.isPresent()) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(optionalReason.get());
    }

}
