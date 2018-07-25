package org.iata.bsplink.agencymemo.service;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionType;
import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionTypePk;
import org.iata.bsplink.agencymemo.model.repository.TaxOnCommissionTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class TaxOnCommissionTypeService {
    TaxOnCommissionTypeRepository taxOnCommissionTypeRepository;

    public TaxOnCommissionTypeService(TaxOnCommissionTypeRepository taxOnCommissionTypeRepository) {
        this.taxOnCommissionTypeRepository = taxOnCommissionTypeRepository;
    }

    public List<TaxOnCommissionType> findByIsoCountryCode(String isoc) {
        return taxOnCommissionTypeRepository.findByPkIsoCountryCode(isoc);
    }


    public TaxOnCommissionType save(TaxOnCommissionType taxOnCommissionType) {
        return taxOnCommissionTypeRepository.save(taxOnCommissionType);
    }

    public void delete(TaxOnCommissionType taxOnCommissionType) {
        taxOnCommissionTypeRepository.delete(taxOnCommissionType);
    }

    public Optional<TaxOnCommissionType> find(TaxOnCommissionTypePk pk) {
        return taxOnCommissionTypeRepository.findById(pk);
    }
}
