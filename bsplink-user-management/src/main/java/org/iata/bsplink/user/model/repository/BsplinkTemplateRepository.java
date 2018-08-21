package org.iata.bsplink.user.model.repository;

import java.util.List;

import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BsplinkTemplateRepository extends JpaRepository<BsplinkTemplate, String> {

    public List<BsplinkTemplate> findByUserTypes(UserType userType);

    @Query("SELECT t1 FROM BsplinkTemplate t1"
            + " WHERE EXISTS (SELECT COUNT(t2.id) FROM BsplinkTemplate t2 JOIN t2.userTypes ut"
            + " WHERE t2.id=t1.id AND ut IN :userTypeList"
            + " GROUP BY t2.id HAVING COUNT(t2.id) = :nr)")
    public List<BsplinkTemplate> findTemplatesForUserTypes(
            @Param("userTypeList") List<UserType> userTypes,
            @Param("nr") long numberOfUserTypesToHaveTemplateInCommon);
}
