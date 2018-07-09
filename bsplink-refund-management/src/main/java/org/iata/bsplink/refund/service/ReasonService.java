package org.iata.bsplink.refund.service;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.model.entity.Reason;
import org.iata.bsplink.refund.model.entity.ReasonType;
import org.iata.bsplink.refund.model.repository.ReasonRepository;
import org.springframework.stereotype.Service;

@Service
public class ReasonService {

    private ReasonRepository reasonRepository;

    public ReasonService(ReasonRepository reasonRepository) {

        this.reasonRepository = reasonRepository;
    }

    public Reason save(Reason reason) {

        return reasonRepository.save(reason);
    }

    public List<Reason> findAll() {

        return reasonRepository.findAll();
    }

    public List<Reason> findByIsoCountryCode(String isoCountryCode) {

        return reasonRepository.findByIsoCountryCode(isoCountryCode);
    }

    public List<Reason> findByIsoCountryCodeAndType(String isoCountryCode, ReasonType type) {

        return reasonRepository.findByIsoCountryCodeAndType(isoCountryCode, type);
    }

    public void delete(Reason reason) {

        reasonRepository.delete(reason);
    }

    /**
     * Updates a reason.
     */
    public Optional<Reason> update(Reason reason) {

        if (!reasonRepository.existsById(reason.getId())) {

            return Optional.empty();
        }

        return Optional.of(reasonRepository.save(reason));
    }

}
