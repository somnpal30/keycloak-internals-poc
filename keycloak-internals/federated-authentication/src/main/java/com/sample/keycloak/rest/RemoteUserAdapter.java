package com.sample.keycloak.rest;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import javax.ws.rs.core.MultivaluedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RemoteUserAdapter extends AbstractUserAdapterFederatedStorage {
    private static final Logger logger = Logger.getLogger(RemoteUserAdapter.class);
    protected FederatedUserModel federatedUserModel;
    protected String keycloakId;

    public RemoteUserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, FederatedUserModel user) {
        super(session, realm, model);
        this.federatedUserModel = user;
        keycloakId = StorageId.keycloakId(model, "" + federatedUserModel.getUsername());
    }

    @Override
    public String getUsername() {
        return federatedUserModel.getUsername();
    }

    @Override
    public void setUsername(String s) {
        federatedUserModel.setUsername(s);
    }

    public String getPassword() {
        return federatedUserModel.getPassword();
    }

    public void setPassword(String password) {
        federatedUserModel.setPassword(password);
    }

    @Override
    public String getId() {
        return this.keycloakId;
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        if (name.equals("phone")) {
            federatedUserModel.getAttributes().get("phone").add(value);
        } else {
            super.setSingleAttribute(name, value);
        }
    }

    @Override
    public void removeAttribute(String name) {
        if (name.equals("phone")) {
            federatedUserModel.getAttributes().remove("phone");
        } else {
            super.removeAttribute(name);
        }
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        if (name.equals("phone")) {
            federatedUserModel.getAttributes().get("phone").add(values.get(0));
        } else {
            super.setAttribute(name, values);
        }
    }

    @Override
    public String getFirstAttribute(String name) {
        if (name.equals("phone")) {
            return federatedUserModel.getAttributes().get("phone").get(0);
        } else {
            return super.getFirstAttribute(name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        //all.add("phone", federatedUserModel.getAttributes().get("phone"));
        return all;
    }

    @Override
    public List<String> getAttribute(String name) {
        if (name.equals("phone")) {
            List<String> phone = new LinkedList<>();
            if (federatedUserModel.getAttributes() != null) {
                phone.addAll(federatedUserModel.getAttributes().get("phone"));
            }

            return phone;
        } else {
            return super.getAttribute(name);
        }
    }

    @Override
    public Set<RoleModel> getRoleMappings() {
        Set<RoleModel> roleModels = super.getRoleMappings();


        for (String role : federatedUserModel.getRoles()) {
            RemoteRoleModel remoteRoleModel = new RemoteRoleModel();
            remoteRoleModel.setName(role);
            roleModels.add(remoteRoleModel);
        }

        for (RoleModel r : roleModels) {
            logger.info(">>" + r.getName());
        }

        return roleModels;
    }
}
