package org.iata.bsplink.agencymemo.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.iata.bsplink.agencymemo.dto.Agent;
import org.iata.bsplink.agencymemo.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/agents")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class AgentController {

    @Autowired
    AgentService agentService;

    /**
     * Get basic Agent Data.
     */
    @ApiOperation(value = "Get basic Agent Data")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Agent Data")})
    @GetMapping("/{code}")
    public ResponseEntity<Agent> getAgent(@PathVariable("code") String code) {

        return agentService.findAgentResponse(code);
    }

    /**
     * Get basic Agent Data.
     */
    @ApiOperation(value = "Get List of all Agent's Data")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List of Agent Data")})
    @GetMapping()
    public ResponseEntity<List<Agent>> getAgents() {

        return agentService.findAllAgentResponse();
    }
}
