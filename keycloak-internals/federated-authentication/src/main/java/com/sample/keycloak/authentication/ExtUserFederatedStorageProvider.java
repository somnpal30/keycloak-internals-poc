package com.sample.keycloak.authentication;

import org.jboss.logging.Logger;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.keycloak.storage.federated.UserFederatedStorageProvider;
import org.slf4j.ILoggerFactory;

import java.util.List;
import java.util.Set;

public class ExtUserFederatedStorageProvider implements UserFederatedStorageProvider {
    private static final Logger logger = Logger.getLogger(ExtUserFederatedStorageProvider.class);
    KeycloakSession keycloakSession;

    public ExtUserFederatedStorageProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }

    @Override
    public List<String> getStoredUsers(RealmModel realmModel, int i, int i1) {

        return null;
    }

    @Override
    public int getStoredUsersCount(RealmModel realmModel) {
        return 0;
    }

    @Override
    public void preRemove(RealmModel realmModel) {

    }

    @Override
    public void preRemove(RealmModel realmModel, GroupModel groupModel) {

    }

    @Override
    public void preRemove(RealmModel realmModel, RoleModel roleModel) {

    }

    @Override
    public void preRemove(RealmModel realmModel, ClientModel clientModel) {

    }

    @Override
    public void preRemove(ProtocolMapperModel protocolMapperModel) {

    }

    @Override
    public void preRemove(ClientScopeModel clientScopeModel) {

    }

    @Override
    public void preRemove(RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void preRemove(RealmModel realmModel, ComponentModel componentModel) {

    }

    @Override
    public void close() {

    }

    @Override
    public void setSingleAttribute(RealmModel realmModel, String id, String name, String value) {

    }

    @Override
    public void setAttribute(RealmModel realmModel, String id, String name, List<String> list) {

    }

    @Override
    public void removeAttribute(RealmModel realmModel, String s, String s1) {

    }

    @Override
    public MultivaluedHashMap<String, String> getAttributes(RealmModel realmModel, String s) {
        logger.info("getAttributes");
        return null;
    }

    @Override
    public List<String> getUsersByUserAttribute(RealmModel realmModel, String s, String s1) {
        logger.info("getUsersByUserAttribute");
        return null;
    }

    @Override
    public String getUserByFederatedIdentity(FederatedIdentityModel federatedIdentityModel, RealmModel realmModel) {
        logger.info("getUserByFederatedIdentity");
        return null;
    }

    @Override
    public void addFederatedIdentity(RealmModel realmModel, String s, FederatedIdentityModel federatedIdentityModel) {
        logger.info("addFederatedIdentity");
    }

    @Override
    public boolean removeFederatedIdentity(RealmModel realmModel, String s, String s1) {
        return false;
    }

    @Override
    public void preRemove(RealmModel realmModel, IdentityProviderModel identityProviderModel) {

    }

    @Override
    public void updateFederatedIdentity(RealmModel realmModel, String s, FederatedIdentityModel federatedIdentityModel) {

    }

    @Override
    public Set<FederatedIdentityModel> getFederatedIdentities(String s, RealmModel realmModel) {
        return null;
    }

    @Override
    public FederatedIdentityModel getFederatedIdentity(String s, String s1, RealmModel realmModel) {
        return null;
    }

    @Override
    public void addConsent(RealmModel realmModel, String s, UserConsentModel userConsentModel) {

    }

    @Override
    public UserConsentModel getConsentByClient(RealmModel realmModel, String s, String s1) {
        return null;
    }

    @Override
    public List<UserConsentModel> getConsents(RealmModel realmModel, String s) {
        return null;
    }

    @Override
    public void updateConsent(RealmModel realmModel, String s, UserConsentModel userConsentModel) {

    }

    @Override
    public boolean revokeConsentForClient(RealmModel realmModel, String s, String s1) {
        return false;
    }

    @Override
    public void updateCredential(RealmModel realmModel, String s, CredentialModel credentialModel) {

    }

    @Override
    public CredentialModel createCredential(RealmModel realmModel, String s, CredentialModel credentialModel) {
        return null;
    }

    @Override
    public boolean removeStoredCredential(RealmModel realmModel, String s, String s1) {
        return false;
    }

    @Override
    public CredentialModel getStoredCredentialById(RealmModel realmModel, String s, String s1) {
        return null;
    }

    @Override
    public List<CredentialModel> getStoredCredentials(RealmModel realmModel, String s) {
        return null;
    }

    @Override
    public List<CredentialModel> getStoredCredentialsByType(RealmModel realmModel, String s, String s1) {
        return null;
    }

    @Override
    public CredentialModel getStoredCredentialByNameAndType(RealmModel realmModel, String s, String s1, String s2) {
        return null;
    }

    @Override
    public Set<GroupModel> getGroups(RealmModel realmModel, String s) {
        return null;
    }

    @Override
    public void joinGroup(RealmModel realmModel, String s, GroupModel groupModel) {

    }

    @Override
    public void leaveGroup(RealmModel realmModel, String s, GroupModel groupModel) {

    }

    @Override
    public List<String> getMembership(RealmModel realmModel, GroupModel groupModel, int i, int i1) {
        return null;
    }

    @Override
    public void setNotBeforeForUser(RealmModel realmModel, String s, int i) {

    }

    @Override
    public int getNotBeforeOfUser(RealmModel realmModel, String s) {
        logger.info("getNotBeforeOfUser  :::: " + s);
        return 0;
    }

    @Override
    public Set<String> getRequiredActions(RealmModel realmModel, String s) {
        return null;
    }

    @Override
    public void addRequiredAction(RealmModel realmModel, String s, String s1) {

    }

    @Override
    public void removeRequiredAction(RealmModel realmModel, String s, String s1) {

    }

    @Override
    public void grantRole(RealmModel realmModel, String s, RoleModel roleModel) {

    }

    @Override
    public Set<RoleModel> getRoleMappings(RealmModel realmModel, String s) {
        return null;
    }

    @Override
    public void deleteRoleMapping(RealmModel realmModel, String s, RoleModel roleModel) {

    }
}
