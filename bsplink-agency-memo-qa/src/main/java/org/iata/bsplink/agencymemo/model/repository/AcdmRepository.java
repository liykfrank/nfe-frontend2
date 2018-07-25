package org.iata.bsplink.agencymemo.model.repository;

import org.iata.bsplink.agencymemo.model.entity.Acdm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcdmRepository extends JpaRepository<Acdm, Long> {
}
