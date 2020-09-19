package com.sample.keycloak.authentication;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.storage.federated.UserFederatedStorageProvider;
import org.keycloak.storage.federated.UserFederatedStorageProviderFactory;

public class ExtUserFederatedStorageProviderFactory implements UserFederatedStorageProviderFactory {
    public static final String PROVIDER_ID = "external-federate-storage-provider";
    private static final Logger logger = Logger.getLogger(ExtUserFederatedStorageProviderFactory.class);

    @Override
    public UserFederatedStorageProvider create(KeycloakSession keycloakSession) {
        logger.info("create ExtUserFederatedStorageProviderFactory ..............");
        return new ExtUserFederatedStorageProvider(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
