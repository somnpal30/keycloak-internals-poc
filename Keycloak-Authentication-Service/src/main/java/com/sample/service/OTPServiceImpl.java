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

    @Override
    public void validateOTP(JsonObject otpParam, Handler<AsyncResult<JsonObject>> resultHandler) {
        log.debug("validation process started : " + otpParam.encodePrettily());

        JsonObject respObject = new JsonObject();
        String otp = otpParam.getString("otp");

        if (otp.endsWith("00")) {
            resultHandler.handle(Future.failedFuture("invalid OTP !"));
        } else if (otp.endsWith("0")) {
            resultHandler.handle(Future.succeededFuture(new JsonObject().put("message", "retry")));
        } else {
            resultHandler.handle(Future.succeededFuture(new JsonObject().put("message", "success")));
        }

    }


    private String getOtp() {
        int randomPin = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(randomPin);
    }
}
