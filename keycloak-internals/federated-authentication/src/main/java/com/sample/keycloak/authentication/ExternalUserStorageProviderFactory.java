package com.sample.keycloak.authentication;

import com.sample.keycloak.rest.FederatedUserService;
import com.sample.keycloak.rest.RemoteUserFederationProvider;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class ExternalUserStorageProviderFactory implements UserStorageProviderFactory<ExternalUserStorageProvider> {
    private static final Logger logger = Logger.getLogger(ExternalUserStorageProviderFactory.class);
    public static final String PROVIDER_NAME = "external-user-authentication";
    public static final String PROP_USER_VALIDATION_URL = "http://localhost:3000";
    ExternalUserService externalUserService;
    @Override
    public ExternalUserStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        logger.info("create .........................");
        return new ExternalUserStorageProvider(keycloakSession,componentModel,externalUserService);
    }

    @Override
    public String getId() {
        return PROVIDER_NAME;
    }




    @Override
    public void init(Config.Scope config) {
        logger.info("<<<<<<<<<<<<<<<<<<<<< init >>>>>>>>>>>>>>>>>>>>>>>>");
        externalUserService = buildClient(PROP_USER_VALIDATION_URL);
    }


    private static ExternalUserService buildClient(String uri) {
        logger.info("URL : " + uri);
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget resteasyWebTarget = client.target(uri);

        return resteasyWebTarget
                .proxyBuilder(ExternalUserService.class)
                .classloader(ExternalUserService.class.getClassLoader())
                .build();
    }
}
