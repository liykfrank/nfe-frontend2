package org.iata.bsplink.user.configuration;

import static org.junit.Assert.assertEquals;

import feign.RequestTemplate;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityFeignRequestInterceptorTest {

    @MockBean
    private SecurityContext securityContext;

    @MockBean
    private SimpleKeycloakAccount simpleKeycloakAccount;

    private SecurityFeignRequestInterceptor requestInterceptor;

    private User user;

    private SimpleKeycloakAccount details;

    private UsernamePasswordAuthenticationToken auth;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer ";

    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI4eks"
            + "zUGt4YnY0UmtzTEtwNVlwR2FvVGloblFJNUNBbWVZQm4zbXVMTThzIn0."
            + "eyJqdGkiOiIzZmI1ODE4MC0yZjRkLTRhNjgtODEzYi0wNjIzNWY1NjA"
            + "wNDgiLCJleHAiOjE2NDgwMjYyNjYsIm5iZiI6MCwiaWF0IjoxNTM1NzA"
            + "2MjY2LCJpc3MiOiJodHRwOi8va2V5Y2xvYWstZGV2Lm5mZWRldi5hY2Nl"
            + "bHlhLmNvbS9hdXRoL3JlYWxtcy9ORkUiLCJhdWQiOiJic3BsaW5rLWxv"
            + "Z2luLW1hbmFnZW1lbnQiLCJzdWIiOiJmODYzNWQzNi0xMzJhLTQ0ZTItY"
            + "jYyNS05NWE5MjY5MjMzMjAiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJic"
            + "3BsaW5rLWxvZ2luLW1hbmFnZW1lbnQiLCJhdXRoX3RpbWUiOjAsInNlc3"
            + "Npb25fc3RhdGUiOiJiODg2N2EzMy1lY2YwLTRiMzAtODBjYy1hM2NiZTN"
            + "jN2Q1NjIiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhb"
            + "G1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F"
            + "1dGhvcml6YXRpb24iLCJBRE1JTiIsIlVTRVIiXX0sInJlc291cmNlX2FjY"
            + "2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiw"
            + "ibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY"
            + "29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2"
            + "UsInByZWZlcnJlZF91c2VybmFtZSI6InRlc3QiLCJlbWFpbCI6ImRhbml"
            + "sby5yb2Rhc0BhY2NlbHlhLmNvbSJ9.GHK5mi_RGkCka9C3RMoR54seY5RR"
            + "eeeIApAQPSDmQDQJwxF22-Nxkydicv7UatTluA9k6NfONKs8R1R-0JNbBO"
            + "PLENSvUH9JX68fTTTX4RKa7-u3V6lq2PwwuOfF_4ATQN29WPrkwfmyYpuV"
            + "bB0NL-04jaS-ol7i_DyihCz_K5fYrde1Mk85-Jnq7UxhfMheDgIg7LnfmXi"
            + "YHY_TDDQoKScKBTiGL4YRYa4llf7nuo0m0ltsB8vvtNqKmx7ZaPWBIIsjr1"
            + "qRlMVLCC2tO-VkIK0DS37Ua841MqxQdc-6OQVUgYCNt1kc0HcB6MhQ319CM"
            + "1wxic5ku6ZQFHVIM_h9IA";

    @Before
    public void setUp() {

        user = new User("username", "password", true, false, false, false,
                new ArrayList<GrantedAuthority>());
        auth = new UsernamePasswordAuthenticationToken(user, details);
        RefreshableKeycloakSecurityContext token =
                new RefreshableKeycloakSecurityContext(null, null, TOKEN, null, null, null, null);
        details = new SimpleKeycloakAccount(null, null, token);
        auth.setDetails(details);

    }


    @Test
    public void testApply() {

        RequestTemplate requestTemplate = new RequestTemplate();
        SecurityContextHolder.getContext().setAuthentication(auth);
        requestInterceptor = new SecurityFeignRequestInterceptor();
        requestInterceptor.apply(requestTemplate);

        ArrayList<String> collection =
                (ArrayList<String>) requestTemplate.headers().get(AUTHORIZATION_HEADER);
        String bearerToken = collection.get(0);

        assertEquals(bearerToken, BEARER_TOKEN_TYPE + TOKEN);
    }
}
