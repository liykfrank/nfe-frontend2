package org.iata.bsplink.sftpaccountmanager.service.fake;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.fake")
public class FakeAccountDetailsServiceConfiguration {

    private List<Details> accounts;


    @Setter
    @Getter
    public static class Details {

        private String login;
        private String directory;
        private List<String> groups;
    }
}
