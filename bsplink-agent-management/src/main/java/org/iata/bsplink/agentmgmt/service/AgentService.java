package org.iata.bsplink.agentmgmt.service;

import java.util.List;

import org.iata.bsplink.agentmgmt.model.entity.Agent;
import org.iata.bsplink.agentmgmt.model.repository.AgentRepository;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    public List<Agent> saveAll(List<Agent> agents) {
        return agentRepository.saveAll(agents);
    }

    public void delete(Agent agent) {
        agentRepository.delete(agent);
    }
}
