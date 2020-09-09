package com.sample.keycloak.authentication;

import com.sample.keycloak.rest.FederatedUserModel;
import com.sample.keycloak.rest.FederatedUserService;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;

import java.util.*;

public class ExternalUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        UserRegistrationProvider,
        UserQueryProvider,
        CredentialInputUpdater,
        CredentialInputValidator,
        OnUserCache {
    private static final Logger logger = Logger.getLogger(ExternalUserStorageProvider.class);
    public static final String PASSWORD_CACHE_KEY = UserAdapter.class.getName() + ".password";
    protected KeycloakSession keycloakSession;
    protected ComponentModel componentModel;
    protected final FederatedUserService federatedUserService;

    protected Map<String, UserModel> loadedUsers = new HashMap<>();

    public ExternalUserStorageProvider(KeycloakSession session,
                                       ComponentModel model,
                                       FederatedUserService federatedUserService) {
        this.federatedUserService = federatedUserService;
        this.keycloakSession = session;
        this.componentModel = model;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType) {
        return supportsCredentialType(credentialType) && getPassword(userModel) != null;
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType()) || !(credentialInput instanceof UserCredentialModel))
            return false;
        UserCredentialModel cred = (UserCredentialModel) credentialInput;
        String password = getPassword(userModel);
        return password != null && password.equals(cred.getValue());

    }

    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType())
                || !(input instanceof UserCredentialModel)) {
            return false;
        }
        UserCredentialModel cred = (UserCredentialModel) input;
        UserAdapter adapter = getUserAdapter(user);
        adapter.setPassword(cred.getValue());

        return true;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
        if (!supportsCredentialType(credentialType)) return;

        getUserAdapter(user).setPassword(null);

    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
        if (getUserAdapter(user).getPassword() != null) {
            Set<String> set = new HashSet<>();
            set.add(PasswordCredentialModel.TYPE);
            return set;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public void onCache(RealmModel realmModel, CachedUserModel cachedUserModel, UserModel userModel) {
        String password = ((UserAdapter) userModel).getPassword();
        if (password != null) {
            cachedUserModel.getCachedWith().put(PASSWORD_CACHE_KEY, password);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public UserModel getUserById(String id, RealmModel realmModel) {
        logger.info("getUserById: " + id);
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        FederatedUserModel federatedUserModel = federatedUserService.getUserDetails(username);
        return new UserAdapter(keycloakSession, realmModel, componentModel, federatedUserModel);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realmModel) {
        FederatedUserModel federatedUserModel = federatedUserService.getUserDetails(username);
        return new UserAdapter(keycloakSession, realmModel, componentModel, federatedUserModel);
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realmModel) {
        return null;
        //TODO
    }

    @Override
    public int getUsersCount(RealmModel realmModel) {
        return 0;
        //TODO
    }

    @Override
    public List<UserModel> getUsers(RealmModel realmModel) {
        return getUsers(realmModel, -1, -1);
    }

    @Override
    public List<UserModel> getUsers(RealmModel realmModel, int i, int i1) {
        return null;
        //TODO
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realmModel) {
        return searchForUser(search, realmModel, -1, -1);
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        return null;
        //TODO
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel, int i, int i1) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel, int i, int i1) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String s, String s1, RealmModel realmModel) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public UserModel addUser(RealmModel realmModel, String s) {
        return null;
        //TODO
    }

    @Override
    public boolean removeUser(RealmModel realmModel, UserModel userModel) {
        return false;
        // TODO
    }

    public String getPassword(UserModel user) {
        String password = null;
        if (user instanceof CachedUserModel) {
            password = (String) ((CachedUserModel) user).getCachedWith().get(PASSWORD_CACHE_KEY);
        } else if (user instanceof UserAdapter) {
            password = ((UserAdapter) user).getPassword();
        }
        return password;
    }

    public UserAdapter getUserAdapter(UserModel user) {
        UserAdapter adapter = null;
        if (user instanceof CachedUserModel) {
            adapter = (UserAdapter) ((CachedUserModel) user).getDelegateForUpdate();
        } else {
            adapter = (UserAdapter) user;
        }
        return adapter;
    }

}
