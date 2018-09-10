package org.iata.bsplink.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Set;

import org.iata.bsplink.user.preferences.TimeZones;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users/timezones")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@Api("Possible user time zones")
public class TimeZonesController {

    /**
     * Returns a list of possible user time zones.
     */
    @GetMapping
    @ApiOperation("List of possible user time zones")
    public Set<String> getTimeZones() {

        return TimeZones.timeZones();
    }

}
