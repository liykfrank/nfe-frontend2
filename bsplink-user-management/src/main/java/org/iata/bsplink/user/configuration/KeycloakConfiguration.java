package org.iata.bsplink.user.configuration;

import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration {

    @Value("${keycloak-admin.master-realm}")
    private String masterRealm;

    @Value("${keycloak-admin.admin-username}")
    private String adminUsername;

    @Value("${keycloak-admin.admin-pass}")
    private String adminPass;

    @Value("${keycloak-admin.admin-client-id}")
    private String adminClientId;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Bean
    public Keycloak getKeycloak() {
        return Keycloak.getInstance(authServerUrl, masterRealm, adminUsername, adminPass,
                adminClientId);
    }
}
