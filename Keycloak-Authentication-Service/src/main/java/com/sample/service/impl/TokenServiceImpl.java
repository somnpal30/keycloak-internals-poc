package com.sample.service.impl;

import com.sample.service.TokenService;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;

import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenServiceImpl implements TokenService {
    private final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);
    Vertx vertx;
    AuthenticationProvider provider;

    public TokenServiceImpl(Vertx vertx) {
        this.vertx = vertx;
        ConfigRetriever retriever = ConfigRetriever.create(vertx);

        JWTAuthOptions config = new JWTAuthOptions()
                .setKeyStore(new KeyStoreOptions()
                        .setPath("keystore.jceks")
                        .setPassword("secret"));

        provider = JWTAuth.create(vertx, config);
    }

    @Override
    public void getToken(JsonObject jsonObject, Handler<AsyncResult<Void>> resultHandler) {
        log.debug(">>>" + jsonObject);

       // if ("paulo".equals(jsonObject.getString("username")) && "super_secret".equals(jsonObject.getString("password"))) {

        resultHandler.handle(Future.succeededFuture());
    }
}
