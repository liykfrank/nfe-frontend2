package org.iata.bsplink.agentmgmt.service;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.agentmgmt.model.entity.Agent;
import org.iata.bsplink.agentmgmt.model.repository.AgentRepository;
import org.junit.Before;
import org.junit.Test;

public class AgentServiceTest {

    private AgentRepository agentRepository;
    private AgentService agentService;

    @Before
    public void setUp() {
        agentRepository = mock(AgentRepository.class);
        agentService = new AgentService(agentRepository);
    }

    @Test
    public void testSaveAll() {
        List<Agent> agents = new ArrayList<>();
        when(agentRepository.saveAll(any())).thenReturn(agents);
        List<Agent> actual = agentService.saveAll(agents);
        assertThat(actual, sameInstance(agents));
    }

    @Test
    public void testFindAll() throws Exception {
        List<Agent> agents = new ArrayList<>();
        when(agentRepository.findAll()).thenReturn(agents);
        List<Agent> actual = agentService.findAll();
        assertThat(actual, sameInstance(agents));
    }

    @Test
    public void testDelete() {

        Agent agent = new Agent();

        agentService.delete(agent);

        verify(agentRepository).delete(agent);
    }
}
