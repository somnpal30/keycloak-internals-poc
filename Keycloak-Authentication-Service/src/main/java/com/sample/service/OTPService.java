package com.sample.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface OTPService {

    static OTPService create(Vertx vertx, String address) {
        return new OTPServiceVertxEBProxy(vertx, address);
    }


    void generateOTP(Handler<AsyncResult<JsonObject>> resultHandler);
}
