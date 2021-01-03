package com.sample.service;


import com.sample.service.impl.KeycloakServiceImpl;
import com.sample.service.impl.OTPServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceProxyRegisterVerticle extends AbstractVerticle {
    private final Logger log = LoggerFactory.getLogger(ServiceProxyRegisterVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        log.info("***************** ServiceProxyRegisterVerticle *******************************");
        OTPService otpService = new OTPServiceImpl(vertx);
        // Register the handler
        new ServiceBinder(vertx)
                .setAddress("otp.service")
                .register(OTPService.class, otpService);


        KeycloakService keycloakService = new KeycloakServiceImpl(vertx);
        new ServiceBinder(vertx)
                .setAddress("keycloak.service")
                .register(KeycloakService.class, keycloakService);


    }
}
