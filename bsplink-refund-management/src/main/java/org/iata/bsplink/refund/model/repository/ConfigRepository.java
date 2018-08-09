package org.iata.bsplink.refund.model.repository;

import org.iata.bsplink.refund.model.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, String> {
}
