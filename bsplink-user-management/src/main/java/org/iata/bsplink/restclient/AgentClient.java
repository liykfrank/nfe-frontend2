package org.iata.bsplink.restclient;

import java.util.List;

import org.iata.bsplink.pojo.Agent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "agents", url = "${feign.agents.url}", decode404 = true)
public interface AgentClient {

    @GetMapping("/{code}")
    ResponseEntity<Agent> findAgent(@PathVariable("code") String code);

    @GetMapping()
    ResponseEntity<List<Agent>> findAgents();
}