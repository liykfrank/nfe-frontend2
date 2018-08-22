package org.iata.bsplink.user.model.repository;

import java.util.List;

import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BsplinkOptionRepository extends JpaRepository<BsplinkOption, String> {

    public List<BsplinkOption> findByUserTypes(UserType userType);

    @Query("SELECT bo1 FROM BsplinkOption bo1"
            + " WHERE EXISTS (SELECT COUNT(bo2.id) FROM BsplinkOption bo2 JOIN bo2.userTypes ut"
            + " WHERE bo2.id=bo1.id AND ut IN :userTypeList"
            + " GROUP BY bo2.id HAVING COUNT(bo2.id) = :nr)")
    public List<BsplinkOption> findOptionsForUserTypes(
            @Param("userTypeList") List<UserType> userTypes,
            @Param("nr") long numberOfUserTypesToHaveOptionInCommon);
}
