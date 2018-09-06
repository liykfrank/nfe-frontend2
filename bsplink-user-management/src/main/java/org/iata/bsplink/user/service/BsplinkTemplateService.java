package org.iata.bsplink.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.repository.BsplinkTemplateRepository;
import org.iata.bsplink.user.model.repository.UserTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BsplinkTemplateService {

    private BsplinkTemplateRepository repository;
    private UserTemplateRepository userTemplateRepository;


    /**
     * Creates an instance of a BsplinkTemplate.
     */
    public BsplinkTemplateService(BsplinkTemplateRepository repository,
            UserTemplateRepository userTemplateRepository) {

        this.repository = repository;
        this.userTemplateRepository = userTemplateRepository;
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


    /**
     * Updates a BsplinkTemplate.
     */
    @Transactional
    public BsplinkTemplate update(BsplinkTemplate oldTemplate, BsplinkTemplate newTemplate) {

        newTemplate.setId(oldTemplate.getId());

        List<UserType> oldUserTypes = oldTemplate.getUserTypes().stream()
            .filter(userType -> !newTemplate.getUserTypes().contains(userType))
            .collect(Collectors.toList());

        userTemplateRepository.removeIsoCountryCodesByTemplateForUserTypes(
                oldTemplate.getId(), oldUserTypes.stream().map(UserType::toString)
                    .collect(Collectors.toList()));
        userTemplateRepository.deleteByTemplateForUserTypes(
                oldTemplate.getId(), oldUserTypes);
        return repository.save(newTemplate);
    }


    /**
     * Deletes a template.
     */
    @Transactional
    public void delete(BsplinkTemplate template) {

        userTemplateRepository.deleteByTemplate(template.getId());
        repository.delete(template);
    }


    public List<BsplinkTemplate> findByUserType(@NotBlank UserType userType) {

        return repository.findByUserTypes(userType);
    }


    /**
     * Returns a list of all BsplinkTemplates which the user types have in common.
     */
    public List<BsplinkTemplate> findByUserTypes(List<UserType> userTypes) {

        return repository.findTemplatesForUserTypes(userTypes,
                userTypes.stream().distinct().count());
    }


    /**
     * Adds the new user type to the template and saves the changes.
     */
    public UserType saveUserType(BsplinkTemplate template, UserType userType) {

        template.getUserTypes().add(userType);
        save(template);
        return userType;
    }


    /**
     * Removes the user type from the template and saves the changes.
     */
    @Transactional
    public void deleteUserType(BsplinkTemplate template, UserType userType) {

        template.getUserTypes().remove(userType);
        userTemplateRepository.removeIsoCountryCodesByTemplateForUserTypes(
                template.getId(),
                Arrays.asList(userType.toString()));
        userTemplateRepository.deleteByTemplateForUserTypes(
                template.getId(),
                Arrays.asList(userType));
        save(template);
    }


    /**
     * Adds the new option to the template and saves the changes.
     */
    public BsplinkOption saveOption(BsplinkTemplate template, BsplinkOption option) {

        template.getOptions().add(option);
        save(template);
        return option;
    }


    /**
     * Removes the option from the template and saves the changes.
     */
    public void deleteOption(BsplinkTemplate template, BsplinkOption option) {

        template.getOptions().remove(option);
        save(template);
    }
}
