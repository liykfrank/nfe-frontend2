package org.iata.bsplink.user.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.repository.BsplinkTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BsplinkTemplateService {

    @Autowired
    private BsplinkTemplateRepository repository;

    public BsplinkTemplateService(BsplinkTemplateRepository repository) {
        this.repository = repository;
    }

    public List<BsplinkTemplate> findAll() {
        return repository.findAll();
    }

    public Optional<BsplinkTemplate> findById(String id) {
        return repository.findById(id);
    }

    public BsplinkTemplate save(BsplinkTemplate template) {
        return repository.save(template);
    }

    public void delete(BsplinkTemplate template) {
        repository.delete(template);
    }

    public List<BsplinkTemplate> findByUserType(@NotBlank UserType userType) {
        return repository.findByUserTypes(userType);
    }
}
