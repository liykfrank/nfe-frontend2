package org.iata.bsplink.user.model.repository;

import java.util.List;

import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BsplinkTemplateRepository extends JpaRepository<BsplinkTemplate, String> {

    public List<BsplinkTemplate> findByUserTypes(UserType userType);
}
