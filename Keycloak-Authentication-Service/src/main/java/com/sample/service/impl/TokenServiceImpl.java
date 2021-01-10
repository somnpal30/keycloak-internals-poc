package com.sample.service.impl;

import com.sample.service.TokenService;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
/*import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenServiceImpl implements TokenService {
    private final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);
    Vertx vertx;

    public TokenServiceImpl(Vertx vertx) {
        this.vertx = vertx;
        ConfigRetriever retriever = ConfigRetriever.create(vertx);

    }

    @Override
    public void invalidateSession(JsonObject jsonObject, Handler<AsyncResult<Void>> resultHandler) {
        log.debug(">>>" + jsonObject);

        resultHandler.handle(Future.succeededFuture());
    }
}
