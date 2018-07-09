package org.iata.bsplink.refund.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.iata.bsplink.refund.test.fixtures.ConfigFixtures.getConfigs;
import static org.iata.bsplink.refund.test.fixtures.ConfigFixtures.getCountries;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.repository.ConfigRepository;
import org.junit.Before;
import org.junit.Test;

public class ConfigServiceTest {

    private ConfigRepository configRepository;
    private ConfigService configService;
    private List<Config> configs;
    private Config anyConfig;

    @Before
    public void setUp() {

        configRepository = mock(ConfigRepository.class);
        configService = new ConfigService(configRepository);

        configs = getConfigs();
        anyConfig = configs.get(0);
    }

    @Test
    public void testSave() {

        when(configRepository.save(any())).thenReturn(anyConfig);

        Config savedCfg = configService.save(anyConfig);

        assertThat(savedCfg, equalTo(anyConfig));
        verify(configRepository).save(anyConfig);
    }

    @Test
    public void testFindAll() throws Exception {

        when(configRepository.findAll()).thenReturn(configs);

        List<Config> foundAll = configService.findAll();

        assertThat(foundAll, sameInstance(configs));
    }

    @Test
    public void testFindAllIsoCountryCodes() throws Exception {

        when(configRepository.findAll()).thenReturn(configs);
        List<String> foundIsocs = configService.findAllIsoCountryCodes();

        assertThat(foundIsocs, equalTo(getCountries()));
    }

    @Test
    public void testFind() throws Exception {

        when(configRepository.findById(anyConfig.getIsoCountryCode()))
                .thenReturn(Optional.of(anyConfig));

        Config found = configService.find(anyConfig.getIsoCountryCode());

        assertThat(found, sameInstance(anyConfig));
    }

    @Test
    public void testFindDefault() throws Exception {

        String isoc = "NX";
        when(configRepository.findById(isoc)).thenReturn(Optional.empty());

        Config found = configService.find(isoc);

        assertThat(found, equalTo(new Config(isoc)));
    }
}

