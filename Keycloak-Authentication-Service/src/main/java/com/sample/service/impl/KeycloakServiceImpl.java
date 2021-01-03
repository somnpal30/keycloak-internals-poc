package com.sample.service.impl;

import com.sample.service.KeycloakService;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeycloakServiceImpl implements KeycloakService {
    private final Logger log = LoggerFactory.getLogger(KeycloakServiceImpl.class);
    Vertx vertx;
    Keycloak keycloak = null;
    public KeycloakServiceImpl(Vertx vertx) {
        this.vertx = vertx;
        ConfigRetriever retriever = ConfigRetriever.create(vertx);
        retriever.getConfig(json -> {
            JsonObject config = json.result().getJsonObject("keycloak-config");

            log.debug("keycloak config : {} ", config);
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(config.getString("auth-server-url"))
                    .realm(config.getString("realm"))
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(config.getString("resource"))
                    .clientSecret(config.getJsonObject("credentials").getString("secret"))
                    .username("superadmin").password("password")
                    .build();

        });
    }

    @Override
    public void invalidateSession(JsonObject jsonObject, Handler<AsyncResult<Void>> resultHandler) {
        log.debug(">>>" + jsonObject);
        keycloak.realm("angular").deleteSession(jsonObject.getString("session_state"));
        resultHandler.handle(Future.succeededFuture());
    }
}
