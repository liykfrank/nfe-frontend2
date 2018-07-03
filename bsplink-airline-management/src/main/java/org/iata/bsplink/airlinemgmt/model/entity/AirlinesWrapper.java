package org.iata.bsplink.airlinemgmt.model.entity;

import java.util.List;

import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;

/**
 * Wrapper for an array of airlines aimed to validation.
 *
 *<p>
 * Validation only works on beans so this wrapper transforms the airlines list to a bean that can
 * be validated.
 *</p>
 */
@Setter
@Getter
public class AirlinesWrapper {

    @Valid
    private final List<Airline> airlines;

    public AirlinesWrapper(List<Airline> airlines) {
        this.airlines = airlines;
    }
}
