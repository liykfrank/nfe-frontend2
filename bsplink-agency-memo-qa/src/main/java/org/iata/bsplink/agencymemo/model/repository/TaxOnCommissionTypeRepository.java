package org.iata.bsplink.agencymemo.model.repository;

import java.util.List;

import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionType;
import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionTypePk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxOnCommissionTypeRepository
    extends JpaRepository<TaxOnCommissionType, TaxOnCommissionTypePk> {

    List<TaxOnCommissionType> findByPkIsoCountryCode(String isoCountryCode);
}
