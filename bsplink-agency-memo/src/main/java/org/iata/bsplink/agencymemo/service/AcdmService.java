package org.iata.bsplink.agencymemo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Acdm;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.model.entity.RelatedTicketDocument;
import org.iata.bsplink.agencymemo.model.entity.TaxMiscellaneousFee;
import org.iata.bsplink.agencymemo.model.repository.AcdmRepository;
import org.iata.bsplink.agencymemo.utils.CalculationsUtility;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class AcdmService {

    private AcdmRepository acdmRepository;
    private ConfigService configService;

    public AcdmService(AcdmRepository acdmRepository, ConfigService configService) {
        this.acdmRepository = acdmRepository;
        this.configService = configService;
    }

    public Optional<Acdm> findById(Long id) {
        return acdmRepository.findById(id);
    }


    public List<Acdm> findAll() {
        return acdmRepository.findAll();
    }

    /**
     * Persists an AcdmRequest as an ACDM.
     */
    public Acdm save(AcdmRequest acdmRequest) {
        Acdm acdm = new Acdm();
        copyPropertiesFromAcdmRequestToAcdm(acdmRequest, acdm);
        acdm.setComments(null);
        acdm.setAttachedFiles(null);
        return acdmRepository.save(acdm);
    }

    /**
     * Copies property values from an AcdmRequest instance to an Acdm instance.
     */
    public void copyPropertiesFromAcdmRequestToAcdm(AcdmRequest acdmRequest, Acdm acdm) {
        BeanUtils.copyProperties(acdmRequest, acdm);
        BeanUtils.copyProperties(acdmRequest.getAgentCalculations(), acdm.getAgentCalculations());
        BeanUtils.copyProperties(acdmRequest.getAirlineCalculations(),
                acdm.getAirlineCalculations());
        BeanUtils.copyProperties(acdmRequest.getAirlineContact(), acdm.getAirlineContact());
        BeanUtils.copyProperties(acdmRequest.getCurrency(), acdm.getCurrency());

        acdm.setRelatedTicketDocuments(
                acdmRequest.getRelatedTicketDocuments().stream().map(rReq -> {
                    RelatedTicketDocument r = new RelatedTicketDocument();
                    BeanUtils.copyProperties(rReq, r);
                    return r;
                }).collect(Collectors.toList()));

        acdm.setTaxMiscellaneousFees(acdmRequest.getTaxMiscellaneousFees().stream().map(tReq -> {
            TaxMiscellaneousFee t = new TaxMiscellaneousFee();
            BeanUtils.copyProperties(tReq, t);
            return t;
        }).collect(Collectors.toList()));
    }


    /**
     * Sets ACDM Request's regularized flag if it is null.
     */
    public void regularization(AcdmRequest acdmRequest) {

        if (acdmRequest.getRegularized() == null && acdmRequest.getIsoCountryCode() != null) {
            Config cfg = configService.find(acdmRequest.getIsoCountryCode());
            acdmRequest.setRegularized(CalculationsUtility.isToRegularize(cfg, acdmRequest));
        }
    }
}
