package org.iata.bsplink.user.service;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.repository.BsplinkOptionRepository;
import org.springframework.stereotype.Service;

@Service
public class BsplinkOptionService {

    private BsplinkOptionRepository repository;

    public BsplinkOptionService(BsplinkOptionRepository repository) {
        this.repository = repository;
    }

    public List<BsplinkOption> findAll() {
        return repository.findAll();
    }

    public Optional<BsplinkOption> findById(String id) {
        return repository.findById(id);
    }

    public List<BsplinkOption> findByUserType(UserType userType) {
        return repository.findByUserTypes(userType);
    }
}
