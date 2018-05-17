package org.iata.bsplink.test.dummy;

import javax.validation.Valid;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dummy controller with test purposes.
 */
@RestController
public class DummyValidationController {

    /**
     * Dummy validation method.
     */
    @PostMapping("/model")
    public ResponseEntity<String> add(@Valid @RequestBody DummyModel dummyModel, Errors errors) {

        if (errors.hasErrors()) {

            throw new ApplicationValidationException(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
