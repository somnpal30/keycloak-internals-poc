package com.sample.keycloak.rest;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;

public class FedUserModel extends AbstractUserAdapter {

    public FedUserModel(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel) {
        super(session, realm, storageProviderModel);
    }

    @Override
    public String getUsername() {
        return storageProviderModel.getName();
    }
}
