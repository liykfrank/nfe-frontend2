package org.iata.bsplink.user.model.repository;

import org.iata.bsplink.user.model.entity.UserTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTemplateRepository extends JpaRepository<UserTemplate, Long> {

}
