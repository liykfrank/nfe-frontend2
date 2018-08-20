# BSPlink commons SECURITY

Provides common functionality to secure microservices in the `BSPlink NFE project`.

In order to secure applications with this library the following dependencies have to be included in your project.

```json
{
	compile('org.springframework.boot:spring-boot-starter-security')
	compile('org.keycloak:keycloak-spring-boot-starter:4.2.1.Final')
	compile('org.iata.bsplink:bsplink-commons-security:0.0.1')
}
```

And configure the following keycloak auth server application properties:

```yml
keycloak:
  auth-server-url: http://host/auth
  realm: realmName
  public-client: true
  resource: appName
  principal-attribute: preferred_username 
``` 