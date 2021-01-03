package com.sample;

import com.sample.service.ServiceProxyRegisterVerticle;
import com.sample.verticle.RestServiceVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MainVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
    List<AbstractVerticle> verticleList = Arrays.asList(new ServiceProxyRegisterVerticle(), new RestServiceVerticle());

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        verticleList.stream().forEach(verticle -> {
            vertx.deployVerticle(verticle, response -> {
                if (response.failed()) {
                    logger.error("unable to deploy verticle :: " + verticle.getClass().getSimpleName() + " : " + response.cause());
                } else {
                    logger.info("Verticle deployed ::" + verticle.getClass().getSimpleName());
                }
            });
        });
    }

    public static void main(final String... args) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }
}
