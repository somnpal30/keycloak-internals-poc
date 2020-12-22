package com.sample.keycloak.rest;


import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;

import java.util.*;

public class RemoteUserFederationProvider implements
        UserStorageProvider,
        UserLookupProvider,
        CredentialInputValidator,
        CredentialInputUpdater {
    private static final Logger logger = Logger.getLogger(RemoteUserFederationProvider.class);

    protected KeycloakSession keycloakSession;
    protected ComponentModel componentModel;
    protected final FederatedUserService federatedUserService;

    protected Map<String, UserModel> loadedUsers = new HashMap<>();

    public RemoteUserFederationProvider(KeycloakSession session,
                                        ComponentModel model,
                                        FederatedUserService federatedUserService) {
        this.federatedUserService = federatedUserService;
        this.keycloakSession = session;
        this.componentModel = model;
    }


    @Override
    public boolean updateCredential(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (credentialInput.getType().equals(PasswordCredentialModel.TYPE)) {
            throw new ReadOnlyException("user is read only for this update");
        }

        return false;
    }

    @Override
    public void disableCredentialType(RealmModel realmModel, UserModel userModel, String s) {

    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realmModel, UserModel userModel) {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(PasswordCredentialModel.TYPE);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        //String password = properties.getProperty(user.getUsername());
        //return credentialType.equals(PasswordCredentialModel.TYPE) && password != null;
        logger.info("isConfigureFor : " + user.getUsername());
        return true;
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        UserCredentialModel cred = (UserCredentialModel) credentialInput;
        logger.info(userModel.getAttributes().containsKey("ssn"));
        logger.info(userModel.getAttributes().containsKey("phone"));

        StorageId storageId = new StorageId(userModel.getId());
        String username = storageId.getExternalId();

        logger.info("_______________________________________________");
        logger.info("credentialInput :: " + cred.getValue());
        logger.info("user id : " + userModel.getId() + "------" + username);
        logger.info("user name : " + userModel.getUsername());
        logger.info("_______________________________________________");

        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setPassword(cred.getValue());
        Boolean isValid = federatedUserService.validateLogin(username, userCredentialsDto);
        logger.info("user " + isValid);
        return isValid;
    }

    @Override
    public void close() {

    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();


        logger.info("get user by id : " + id + " : " + username);
        logger.info("user locale : " + realm.getDefaultLocale());

        /*Map<String, String> header = realm.getBrowserSecurityHeaders();
        for(String key : header.keySet()){
            logger.info(" keys : {}  " +  key + ", value : {}" + header.get(key) );
        }*/

        //logger.info(Thread.currentThread().getId());
        return getUserModel(username, realm);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        logger.info("get user by name : " + username);
        logger.info(Thread.currentThread().getId());
        return getUserModel(username, realm);
    }

    private UserModel getUserModel(String username, RealmModel realm) {

        if (!loadedUsers.containsKey(username)) {
            FederatedUserModel federatedUserModel = federatedUserService.getUserDetails(username);
            logger.info("federatedUserModel " + federatedUserModel.getUsername());
            UserModel adapter = createAdapter(realm, federatedUserModel);
            loadedUsers.put(username, adapter);
            logger.info("User not cached !" + adapter.getUsername());
            return adapter;

        } else {
            logger.info("User returning from cached !");
            return loadedUsers.get(username);
        }

    }


    @Override
    public UserModel getUserByEmail(String s, RealmModel realmModel) {
        return null;
    }

    protected UserModel createAdapter(RealmModel realm, FederatedUserModel federatedUserModel) {

        return new AbstractUserAdapter(keycloakSession, realm, componentModel) {
            @Override
            public String getUsername() {
                return federatedUserModel.getUsername();
            }

            @Override
            public Set<RoleModel> getRoleMappings() {
                logger.info("setting roles...");
                Set<RoleModel> roleModels = super.getRoleMappings();
                return roleModels;
            }

            @Override
            public Map<String, List<String>> getAttributes() {
                //super.getAttributes().putAll(federatedUserModel.getAttributes());
                for (String attrKey : federatedUserModel.getAttributes().keySet()) {
                    logger.info("Atrribute key :" + attrKey);
                }
                return federatedUserModel.getAttributes();
            }


        };

    }

}
