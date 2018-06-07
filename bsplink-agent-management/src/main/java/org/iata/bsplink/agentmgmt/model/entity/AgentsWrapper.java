package org.iata.bsplink.agentmgmt.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;

/**
 * Wrapper for an array of agents aimed to validation.
 *
 *<p>
 * Validation only works on beans so this wrapper transforms the agents list to a bean that can
 * be validated.
 *</p>
 */
@Data
public final class AgentsWrapper {

    @Valid
    private final List<Agent> agents;

    @JsonCreator
    public AgentsWrapper(List<Agent> agents) {
        this.agents = agents;
    }
}
