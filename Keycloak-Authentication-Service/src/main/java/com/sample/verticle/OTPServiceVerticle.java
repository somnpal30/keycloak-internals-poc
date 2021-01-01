package com.sample.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OTPServiceVerticle extends AbstractVerticle {
  private final Logger log = LoggerFactory.getLogger(OTPServiceVerticle.class);

  @Override
  public void start() throws Exception {
    ConfigRetriever retriever = ConfigRetriever.create(vertx);
    vertx.eventBus().consumer("OTP_GENERATE", this::generateOTP);
    vertx.eventBus().consumer("OTP_VALIDATE", this::validateOTP);
  }

  private void generateOTP(Message msg) {
    JsonObject otpEntries = new JsonObject();
    otpEntries.put("otp", getOtp());
    log.info("otp generated ::" + otpEntries.encodePrettily());
    msg.reply(otpEntries);
  }

  private void validateOTP(Message msg) {
    JsonObject jsonObject = (JsonObject) msg.body();
    log.info(jsonObject.encodePrettily());
    String otp = jsonObject.getString("otp");

    if(otp.endsWith("00")){
      msg.fail(404, "invalid OTP !");
    }else if (otp.endsWith("0")){
      msg.reply("retry");
    }else {
      msg.reply("success");
    }


  }

  private String getOtp() {
    int randomPin = (int) (Math.random() * 9000) + 1000;
    return String.valueOf(randomPin);
  }
}
