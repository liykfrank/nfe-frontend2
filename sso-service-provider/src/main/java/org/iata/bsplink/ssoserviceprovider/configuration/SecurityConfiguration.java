package org.iata.bsplink.ssoserviceprovider.configuration;

import static org.springframework.security.extensions.saml2.config.SAMLConfigurer.saml;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Value("${security.saml2.metadata-url}")
    private String metadataUrl;

    @Value("${server.ssl.key-alias}")
    private String keyAlias;

    @Value("${server.ssl.key-store-password}")
    private String password;

    @Value("${server.port}")
    private String port;

    @Value("${server.ssl.key-store}")
    private String keyStoreFilePath;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/saml*").permitAll()
                .anyRequest().authenticated()
                .and()
            .apply(saml())
                .serviceProvider()
                    .keyStore()
                        .storeFilePath(this.keyStoreFilePath)
                        .password(this.password)
                        .keyname(this.keyAlias)
                        .keyPassword(this.password)
                        .and()
                    .protocol("http")
                    .hostname(String.format("%s:%s", "localhost", this.port))
                    .basePath("/")
                    .and()
                .identityProvider()
                .metadataFilePath(this.metadataUrl);
    }
}
