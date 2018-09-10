package org.iata.bsplink.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.iata.bsplink.user.preferences.Languages;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users/languages")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@Api("Possible user languages")
public class LanguagesController {

    /**
     * Returns a list of possible user languages.
     */
    @GetMapping
    @ApiOperation("List of possible user languages.")
    public List<String> getLanguages() {

        return Arrays.asList(Languages.values()).stream()
                .map(Languages::toString)
                .collect(Collectors.toList());
    }

}
