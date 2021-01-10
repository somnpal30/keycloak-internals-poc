package com.sample.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface TokenService {
    static TokenService create(Vertx vertx, String address) {
        return new TokenServiceVertxEBProxy(vertx, address);
    }

    void invalidateSession(JsonObject jsonObject, Handler<AsyncResult<Void>> resultHandler);
}
