package org.iata.bsplink.refund.service;

import java.util.List;
import java.util.stream.Collectors;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.repository.ConfigRepository;
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
     *
     * <p>
     * In case there is no persisted Configuration, default Configuration is returned.
     * </p>
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
