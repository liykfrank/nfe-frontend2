# SSO-SERVICE-PROVIDER

`SSO Service Provider` allows users to access to New Front-End application from external system such us Salesforce.


## Application Configuration

In order to run the Single Sign On Service Provider it is necessary to configure the following application properties:

- server.port = 8081 #Port in which the application will be run. 
- server.ssl.enabled = false #HTTPS protocol is disabled to establish internal communications in the cluster.
- server.ssl.key-alias = spring #The alias of the key stored in the keystore.
- server.ssl.key-store = classpath:saml/keystore.jks #Path to keystore.jks file.
- server.ssl.key-store-password = secret #Keystore's password to generate new certificates.
- security.saml2.metadata-url: {path}/sso/saml/metadata #Path to identity provider metadata file.
- app.redirect.page: redirect: http://www.nfedev.accelya.com/ #Web page where the user will be redirect if logged successful.


### Generate new certificates

`SSO Service Provider` runs with HTTP protocol but the communications with the IDP are establish with HTTPS protocol.
Because of this it is necessary to generate a new certificate and store it in the keystore.jks file.

In a terminal window:

- Navigate to the src/main/resources/saml directory and run the following command. Use “secret” when prompted for a keystore password

    keytool -genkey -v -keystore keystore.jks -alias spring -keyalg RSA -keysize 2048 -validity 10000

- The values for the rest of the questions don’t matter since you’re not generating a real certificate. However, you will need to answer “yes” to the following question

    Is CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown correct?
    [no]:


## IDP configuration

To make it possible for a user to access New front End application, in the IDP it is necessary to configure the following URLs:

- Single sign on URL: http://www.nfedev.accelya.com:8081/saml/SSO
- Service Provider Entity ID: http://www.nfedev.accelya.com:8081/saml/metadata
