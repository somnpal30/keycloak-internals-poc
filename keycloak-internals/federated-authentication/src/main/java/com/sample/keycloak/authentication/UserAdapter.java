package com.sample.keycloak.authentication;

import com.sample.keycloak.rest.FederatedUserModel;
import org.jboss.logging.Logger;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserAdapter  extends AbstractUserAdapterFederatedStorage {

    private static final Logger logger = Logger.getLogger(UserAdapter.class);
    protected String keycloakId;
    private FederatedUserModel federatedUserModel;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, FederatedUserModel federatedUserModel) {
        super(session, realm, model);
        this.federatedUserModel = federatedUserModel;
        keycloakId = StorageId.keycloakId(model, federatedUserModel.getUsername());



    }
    public String getPassword() {
        return federatedUserModel.getPassword();
    }

    public void setPassword(String password) {
        federatedUserModel.setPassword(password);
    }

    @Override
    public String getUsername() {
        return federatedUserModel.getUsername();
    }

    @Override
    public void setUsername(String username) {
        federatedUserModel.setUsername(username);

    }

   

    @Override
    public void setEmail(String email) {
        federatedUserModel.setEmail(email);
    }

    @Override
    public String getEmail() {
        return federatedUserModel.getEmail();
    }

    @Override
    public String getId() {
        return keycloakId;
    }

    @Override
    public void setSingleAttribute(String name, String value) {
       /* if (name.equals("phone")) {
            federatedUserModel.setPhone(value);
        } else {
            super.setSingleAttribute(name, value);
        }*/
        super.setSingleAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
       /* if (name.equals("phone")) {
            federatedUserModel.setPhone(null);
        } else {
            super.removeAttribute(name);
        }*/
        super.removeAttribute(name);
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        logger.info("*********************************************");
        logger.info("attribute name : " + name);
        logger.info("values  : " +  values );
        logger.info("*********************************************");
//        if (name.equals("phone")) {
//            federatedUserModel.setPhone(values.get(0));
//        } else {
//            super.setAttribute(name, values);
//        }
        super.setAttribute(name, values);
    }

    @Override
    public String getFirstAttribute(String name) {
       /* if (name.equals("phone")) {
            return federatedUserModel.getPhone();
        } else {
            return super.getFirstAttribute(name);
        }*/
        return super.getFirstAttribute(name);
    }

    @Override
    public Map<String, List<String>> getAttributes() {
       /* Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        all.add("phone", federatedUserModel.getPhone());
        return all;*/
        return  super.getAttributes();
    }

    @Override
    public List<String> getAttribute(String name) {
//        if (name.equals("phone")) {
//            List<String> phone = new LinkedList<>();
//            phone.add(federatedUserModel.getPhone());
//            return phone;
//        } else {
//            return super.getAttribute(name);
//        }
        return super.getAttribute(name);
    }
}
