package com.comviva.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestServiceVerticle extends AbstractVerticle {

  private final Logger log = LoggerFactory.getLogger(RestServiceVerticle.class);

  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);

    router.route("/auth-api/*").handler(BodyHandler.create());
    router.get("/auth-api/token").handler(this::getToken);

    ConfigRetriever retriever = ConfigRetriever.create(vertx);
    retriever.getConfig(json -> {
      JsonObject config = json.result();
      vertx.createHttpServer()
        .requestHandler(router)
        .listen(config.getInteger("http.port"), httpServerAsyncResult -> {
          if (httpServerAsyncResult.succeeded()) {
            log.info("server running at {}", config.getInteger("http.port"));
          } else {
            log.warn("", httpServerAsyncResult.cause());
          }
        });
    });
  }

  private void getToken(RoutingContext routingContext) {
    log.info("Request Receive...");
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("token", "eeeeeee");
    DeliveryOptions options = new DeliveryOptions();


    vertx.eventBus().request("GET_TOKEN", jsonObject, options, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {
        JsonObject jsonObject1 = (JsonObject) messageAsyncResult.result().body();
        routingContext.response().putHeader("content-type", "application/json").setStatusCode(200)
          .end(jsonObject1.toString());
      } else {
        ReplyException replyException = (ReplyException) messageAsyncResult.cause();
        routingContext.response().putHeader("content-type", "application/json")
          .setStatusCode(replyException.failureCode()).end(replyException.getMessage());
      }
    });


  }
}
