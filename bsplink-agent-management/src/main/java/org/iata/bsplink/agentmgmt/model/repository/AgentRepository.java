package org.iata.bsplink.agentmgmt.model.repository;

import org.iata.bsplink.agentmgmt.model.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, String> {
}
