package com.comviva;

import com.comviva.verticle.KeycloakServiceVerticle;
import com.comviva.verticle.RestServiceVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MainVerticle extends AbstractVerticle {
  private final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
  List<AbstractVerticle> verticleList = Arrays.asList(new RestServiceVerticle(), new KeycloakServiceVerticle());

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    verticleList.stream().forEach(verticle -> {
      vertx.deployVerticle(verticle, response -> {
        if (response.failed()) {
          logger.info("unable to deploy verticle :: " + verticle.getClass().getSimpleName() + " : " + response.cause());
        } else {
          logger.info("All verticles deployed ::" + verticle.deploymentID());
        }
      });
    });
  }

  public static void main(final String... args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }
}
