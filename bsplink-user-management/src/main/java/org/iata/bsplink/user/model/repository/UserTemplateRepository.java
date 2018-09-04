package org.iata.bsplink.user.model.repository;

import java.util.List;

import org.iata.bsplink.user.model.entity.UserTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTemplateRepository extends JpaRepository<UserTemplate, Long> {

    public void deleteByTemplate(String template);


    @Modifying
    @Query(nativeQuery = true, value =
        "DELETE FROM bsplink_user.user_template_iso_country_codes WHERE user_template_id IN ("
        + "SELECT ut.id FROM bsplink_user.user_template ut, bsplink_user.user u"
        + " WHERE ut.template = :template AND u.id = ut.user_id AND u.user_type IN :userTypes)")
    public void removeIsoCountryCodesByTemplateForUserTypes(
            @Param("template") String template,
            @Param("userTypes") List<String> userTypes);


    @Modifying
    @Query("DELETE FROM UserTemplate ut WHERE ut.template = :template AND EXISTS"
            + " (SELECT u FROM User u WHERE u.id = ut.userId AND u.userType IN :userTypes)")
    public void deleteByTemplateForUserTypes(
            @Param("template") String template,
            @Param("userTypes") List<UserType> userTypes);
}
