package com.sample.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OTPServiceImpl implements OTPService {
    private final Logger log = LoggerFactory.getLogger(OTPServiceImpl.class);
    Vertx vertx;

    public OTPServiceImpl(Vertx vertx) {
        this.vertx = vertx;
    }


    @Override
    public void generateOTP(Handler<AsyncResult<JsonObject>> resultHandler) {
        JsonObject otpEntries = new JsonObject();
        otpEntries.put("otp", getOtp());
        log.debug("otp generated ::" + otpEntries.encodePrettily());
        resultHandler.handle(Future.succeededFuture(otpEntries));
    }


    private String getOtp() {
        int randomPin = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(randomPin);
    }
}
