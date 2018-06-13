package org.iata.bsplink.agencymemo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.model.repository.ConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {
    ConfigRepository configRepository;

    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public List<Config> findAll() {
        return configRepository.findAll();
    }

    /**
     * Returns Configuration for country.
     * In case there is no persisted Configuration, default Configuration is returned.
     */
    public Config find(String isoc) {
        return configRepository.findById(isoc).orElse(new Config(isoc));
    }

    public Config save(Config config) {
        return configRepository.save(config);
    }

    public List<String> findAllIsoCountryCodes() {
        return findAll().stream().map(Config::getIsoCountryCode).collect(Collectors.toList());
    }
}
