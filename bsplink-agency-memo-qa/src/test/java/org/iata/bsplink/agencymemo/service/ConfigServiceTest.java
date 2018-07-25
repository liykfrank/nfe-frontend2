package org.iata.bsplink.agencymemo.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.iata.bsplink.agencymemo.test.fixtures.ConfigFixtures.getConfigs;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.model.repository.ConfigRepository;
import org.junit.Before;
import org.junit.Test;

public class ConfigServiceTest {

    private ConfigRepository configRepository;
    private ConfigService configService;

    @Before
    public void setUp() {
        configRepository = mock(ConfigRepository.class);
        configService = new ConfigService(configRepository);
    }


    @Test
    public void testSave() {
        Config cfg = getConfigs().get(0);
        when(configRepository.save(any())).thenReturn(cfg);
        Config savedCfg = configService.save(cfg);
        assertThat(savedCfg, equalTo(cfg));
        verify(configRepository).save(cfg);
    }

    @Test
    public void testFindAll() throws Exception {
        List<Config> cfgs = getConfigs();
        when(configRepository.findAll()).thenReturn(cfgs);
        List<Config> foundAll = configService.findAll();
        assertThat(foundAll, sameInstance(cfgs));
    }

    @Test
    public void testFindAllIsoCountryCodes() throws Exception {
        List<Config> cfgs = getConfigs();
        List<String> isocs = cfgs.stream().map(c -> c.getIsoCountryCode())
                .collect(Collectors.toList());

        when(configRepository.findAll()).thenReturn(cfgs);
        List<String> foundIsocs = configService.findAllIsoCountryCodes();
        assertThat(foundIsocs, equalTo(isocs));
    }

    @Test
    public void testFind() throws Exception {
        Config cfg = getConfigs().get(0);
        when(configRepository.findById(cfg.getIsoCountryCode())).thenReturn(Optional.of(cfg));
        Config found = configService.find(cfg.getIsoCountryCode());
        assertThat(found, sameInstance(cfg));
    }

    @Test
    public void testFindDefault() throws Exception {
        String isoc = "NX";
        when(configRepository.findById(isoc)).thenReturn(Optional.empty());
        Config found = configService.find(isoc);
        assertThat(found, equalTo(new Config(isoc)));
    }
}

