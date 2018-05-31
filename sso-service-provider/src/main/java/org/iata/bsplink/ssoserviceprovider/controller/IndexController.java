package org.iata.bsplink.ssoserviceprovider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Value("${app.redirect.page}")
    private String appRedirectPage;

    @RequestMapping("/")
    public String index() {
        return appRedirectPage;
    }
}
