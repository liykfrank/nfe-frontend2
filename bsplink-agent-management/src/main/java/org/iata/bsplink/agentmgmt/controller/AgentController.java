package org.iata.bsplink.agentmgmt.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.agentmgmt.model.entity.Agent;
import org.iata.bsplink.agentmgmt.model.entity.AgentsWrapper;
import org.iata.bsplink.agentmgmt.service.AgentService;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/agents")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AgentController {

    @Autowired
    AgentService agentService;

    @Autowired
    private Validator validator;

    /**
     * Get an Agent.
     */
    @ApiOperation(value = "Agent by IATA Code")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Agent")})
    @GetMapping("/{iataCode}")
    public ResponseEntity<Agent> getAgent(@PathVariable("iataCode") Agent agent) {
        if (agent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(agent);
    }

    @ApiOperation(value = "List of Agents")
    @GetMapping()
    public ResponseEntity<List<Agent>> getAgents() {
        List<Agent> agents = agentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(agents);
    }

    /**
     * Load Agents.
     */
    @ApiOperation(value = "Load an Array of Agents")
    @PostMapping()
    @ApiImplicitParams({@ApiImplicitParam(name = "body", value = "The agents to load.",
            allowMultiple = true, paramType = "body", required = true, dataType = "Agent")})
    public ResponseEntity<String> load(@RequestBody List<Agent> agents) {

        Errors errors = validateAgents(agents);

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }

        agentService.saveAll(agents);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private Errors validateAgents(List<Agent> agents) {

        AgentsWrapper wrapper = new AgentsWrapper(agents);

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(wrapper, "Agent");

        validator.validate(wrapper, errors);

        return errors;
    }

    /**
     * Deletes an Agent.
     */
    @ApiOperation(value = "Deletes an Agent")
    @DeleteMapping("/{iataCode}")
    public ResponseEntity<Agent> deleteAgent(
            @PathVariable("iataCode") Optional<Agent> optionalAgent) {

        if (!optionalAgent.isPresent()) {

            return ResponseEntity.notFound().build();
        }

        agentService.delete(optionalAgent.get());

        return ResponseEntity.status(HttpStatus.OK).body(optionalAgent.get());
    }
}
