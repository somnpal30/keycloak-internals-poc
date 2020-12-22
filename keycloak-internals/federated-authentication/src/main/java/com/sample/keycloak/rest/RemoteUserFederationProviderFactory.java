/*
 * Copyright 2015 Smartling, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sample.keycloak.rest;

import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

/**
 * Remote user federation provider factory.
 *
 * @author Scott Rossillo
 */
public class RemoteUserFederationProviderFactory implements UserStorageProviderFactory<RemoteUserFederationProvider> {

    public static final String PROVIDER_NAME = "Rest API Provider";
    public static final String PROP_USER_VALIDATION_URL = "http://localhost:8000";

    private static final Logger LOG = Logger.getLogger(RemoteUserFederationProviderFactory.class);

    FederatedUserService federatedUserService;

    @Override
    public void init(Config.Scope config) {
        LOG.info("<<<<<<<<<<<<<<<<<<<<< init >>>>>>>>>>>>>>>>>>>>>>>>");
        federatedUserService = buildClient(PROP_USER_VALIDATION_URL);
    }

    @Override
    public RemoteUserFederationProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        LOG.info("create .........................");
        return new RemoteUserFederationProvider(keycloakSession,componentModel,federatedUserService);
    }



    @Override
    public String getId() {
        return PROVIDER_NAME;
    }


    private static FederatedUserService buildClient(String uri) {
        LOG.info("URL : " + uri);
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget resteasyWebTarget = client.target(uri);

        return resteasyWebTarget
                .proxyBuilder(FederatedUserService.class)
                .classloader(FederatedUserService.class.getClassLoader())
                .build();
    }
}
