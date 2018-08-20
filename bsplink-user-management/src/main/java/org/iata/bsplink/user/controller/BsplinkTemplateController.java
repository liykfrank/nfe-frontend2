package org.iata.bsplink.user.controller;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiImplicitParam;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.view.BsplinkOptionTemplateView;
import org.iata.bsplink.user.service.BsplinkOptionService;
import org.iata.bsplink.user.service.BsplinkTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/bsplinkTemplates")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class BsplinkTemplateController {

    @Autowired
    private BsplinkTemplateService templateService;

    @Autowired
    private BsplinkOptionService optionService;

    /**
     *  Returns the template for the ID.
     */
    @GetMapping(path = "/{id}")
    @ApiImplicitParam(name = "id", value = "The name of the BSPlink Template", required = true,
        type = "string")
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<BsplinkTemplate> getTemplate(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template) {

        if (!template.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(template.get());
    }


    /**
     *  Returns the template for the ID.
     */
    @GetMapping(path = "/{id}", params = "fullView")
    @ApiImplicitParam(name = "id", value = "The name of the BSPlink Template", required = true,
        type = "string")
    public ResponseEntity<BsplinkTemplate> getTemplateFull(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template) {

        return getTemplate(template);
    }

    /**
     *  Returns the templates for the indicated user type.
     */
    @GetMapping(params = "userType")
    public ResponseEntity<List<BsplinkTemplate>> getTemplatesByUserType(
            @NotBlank @RequestParam(required = true) UserType userType) {

        List<BsplinkTemplate> templates = templateService.findByUserType(userType);

        return ResponseEntity.status(HttpStatus.OK).body(templates);
    }


    /**
     *  Returns all templates.
     */
    @JsonView(BsplinkOptionTemplateView.class)
    @GetMapping
    public ResponseEntity<List<BsplinkTemplate>> getTemplates() {

        List<BsplinkTemplate> templates = templateService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(templates);
    }


    /**
     *  Returns all templates.
     */
    @GetMapping(params = "fullView")
    public ResponseEntity<List<BsplinkTemplate>> getTemplatesFull() {

        return getTemplates();
    }

    /**
     * Creates a new template.
     */
    @PostMapping
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<BsplinkTemplate> createTemplate(
            @NotBlank @RequestBody(required = true) @Valid BsplinkTemplate template) {

        Optional<BsplinkTemplate> templateOpt = templateService.findById(template.getId());

        if (templateOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(templateService.save(template));
    }


    /**
     * Updates the template.
     **/
    @PutMapping(path = "/{id}")
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<BsplinkTemplate> updateTemplate(
            @PathVariable("id") Optional<BsplinkTemplate> templateToUpdate,
            @NotBlank @RequestBody(required = true) @Valid BsplinkTemplate template) {

        if (!templateToUpdate.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        template.setId(templateToUpdate.get().getId());

        return ResponseEntity.status(HttpStatus.OK).body(templateService.save(template));
    }


    /**
     * Deletes the template.
     */
    @DeleteMapping(path = "/{id}")
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<BsplinkTemplate> deleteTemplate(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template) {

        if (!template.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        templateService.delete(template.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * Get options from a template.
     */
    @GetMapping(path = "/{id}/options")
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<List<BsplinkOption>> getOptions(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template) {

        if (!template.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(template.get().getOptions());
    }


    /**
     * Get options from a template.
     */
    @GetMapping(path = "/{id}/options", params = "fullView")
    public ResponseEntity<List<BsplinkOption>> getOptionsFull(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template) {

        return getOptions(template);
    }

    /**
     * Add an option to a template.
     */
    @PostMapping(path = "/{id}/options")
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<BsplinkOption> addOption(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template,
            @NotBlank @RequestBody(required = true) BsplinkOption optionRequest) {

        if (!template.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<BsplinkOption> option = optionService.findById(optionRequest.getId());
        if (!option.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (template.get().getOptions().contains(option.get())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        template.get().getOptions().add(option.get());
        templateService.save(template.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(option.get());
    }


    /**
     * Get option from a template.
     */
    @GetMapping(path = "/{id}/options/{optionId}")
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<BsplinkOption> getOption(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template,
            @NotBlank @PathVariable("optionId") Optional<BsplinkOption> option) {

        if (!template.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (!option.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (!template.get().getOptions().contains(option.get())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(option.get());
    }


    /**
     * Get option from a template.
     */
    @GetMapping(path = "/{id}/options/{optionId}", params = "fullView")
    public ResponseEntity<BsplinkOption> getOptionFull(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template,
            @NotBlank @PathVariable("optionId") Optional<BsplinkOption> option) {

        return getOption(template, option);
    }


    /**
     * Remove an option from a template.
     */
    @DeleteMapping(path = "/{id}/options/{optionId}")
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<BsplinkTemplate> removeOption(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template,
            @NotBlank @PathVariable("optionId") Optional<BsplinkOption> option) {

        if (!template.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (!option.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (template.get().getOptions().remove(option.get())) {
            templateService.save(template.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Get all User Types from a template.
     */
    @GetMapping(path = "/{id}/userTypes")
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<List<UserType>> getUserTypes(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template) {

        if (!template.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(template.get().getUserTypes());
    }


    /**
     * Add a User Type to a template.
     */
    @PostMapping(path = "/{id}/userTypes")
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<@NotBlank UserType> addUserType(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> templateOpt,
            @NotBlank @RequestBody(required = true) UserType userType) {

        if (!templateOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        BsplinkTemplate template = templateOpt.get();

        if (template.getUserTypes().contains(userType)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        template.getUserTypes().add(userType);
        templateService.save(template);
        return ResponseEntity.status(HttpStatus.CREATED).body(userType);
    }


    /**
     * Remove a User Type from a template.
     */
    @DeleteMapping(path = "/{id}/userTypes/{userType}")
    @JsonView(BsplinkOptionTemplateView.class)
    public ResponseEntity<BsplinkTemplate> removeUserType(
            @NotBlank @PathVariable("id") Optional<BsplinkTemplate> template,
            @NotBlank @PathVariable("userType") UserType userType) {

        if (!template.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (template.get().getUserTypes().remove(userType)) {
            templateService.save(template.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
