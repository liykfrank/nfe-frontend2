package org.iata.bsplink.user.controller;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.view.BsplinkOptionTemplateView;
import org.iata.bsplink.user.service.BsplinkOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/bsplinkOptions")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class BsplinkOptionController {

    @Autowired
    private BsplinkOptionService optionService;

    /**
     *  Returns the option for the ID.
     */
    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Get a BSPlink Option")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "The option")})
    @ApiImplicitParam(name = "id", value = "The name of the BSPlink Option", required = true,
        type = "string")
    public ResponseEntity<BsplinkOption> getOption(
            @NotBlank @PathVariable("id") Optional<BsplinkOption> option) {

        if (!option.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(option.get());
    }


    /**
     *  Returns the options for the indicated user type.
     */
    @JsonView(BsplinkOptionTemplateView.class)
    @GetMapping(params = "userType")
    public ResponseEntity<List<BsplinkOption>> getOptionsByUserType(
            @NotBlank @RequestParam(required = true) UserType userType) {

        List<BsplinkOption> options = optionService.findByUserType(userType);

        return ResponseEntity.status(HttpStatus.OK).body(options);
    }

    /**
     *  Returns all options.
     */
    @GetMapping
    public ResponseEntity<List<BsplinkOption>> getOptions() {

        List<BsplinkOption> options = optionService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(options);
    }
}
