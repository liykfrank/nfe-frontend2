package org.iata.bsplink.agencymemo.model.repository;

import org.iata.bsplink.agencymemo.model.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, String> {
}
