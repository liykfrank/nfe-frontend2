package org.iata.bsplink.user.model.repository;

import java.util.List;

import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BsplinkOptionRepository extends JpaRepository<BsplinkOption, String> {

    public List<BsplinkOption> findByUserTypes(UserType userType);
}
