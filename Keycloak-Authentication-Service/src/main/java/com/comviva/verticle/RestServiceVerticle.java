package com.comviva.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class RestServiceVerticle extends AbstractVerticle {

  private final Logger log = LoggerFactory.getLogger(RestServiceVerticle.class);
  SessionStore store = null;

  String _SESSION = "session_state";
  String _UID = "prng";

  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    store = LocalSessionStore.create(vertx, "keycloak-session-store", 30000);
    router.route().handler(CorsHandler.create(".*."));
     /* .allowedMethod(io.vertx.core.http.HttpMethod.GET)
      .allowedMethod(io.vertx.core.http.HttpMethod.POST)
      .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
      .allowCredentials(true)
      .allowedHeader("Access-Control-Request-Method")
      .allowedHeader("Access-Control-Allow-Credentials")
      .allowedHeader("Access-Control-Allow-Origin")
      .allowedHeader("Access-Control-Allow-Headers")
      .allowedHeader("Content-Type"));*/

    router.route("/auth-api/*").handler(BodyHandler.create());
    router.get("/auth-api/token").handler(this::getToken);
    router.get("/auth-api/redirect").handler(this::redirect);
    router.post("/auth-api/otp-validate").handler(this::otpValidator);

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

  private void otpValidator(RoutingContext routingContext) {
   /* log.info("session :"+routingContext.request().getParam(_SESSION));
    log.info("prng : "+routingContext.request().getParam(_UID));
    log.info("otp : "+routingContext.request().getParam(_UID));*/
    JsonObject jsonObject = routingContext.getBodyAsJson();
    log.info("session :" + jsonObject.getString(_SESSION));
    log.info("prng : " + jsonObject.getString(_UID));
    log.info("otp : " + jsonObject.getString("otp"));


    store.get(jsonObject.getString(_SESSION), sessionAsyncResult -> {
      if (sessionAsyncResult.succeeded()) {
        Session session = sessionAsyncResult.result();
        log.info(session.get(jsonObject.getString(_UID)));
        log.info("http://localhost:4200?" + session.get(jsonObject.getString(_UID)));

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.put("location","http://localhost:4200?" + session.get(jsonObject.getString(_UID)));
        routingContext.response().setStatusCode(200).putHeader("content-type", "application/json").end(jsonObject1.toString());

      } else {
        log.error("no session available for id " + jsonObject.getString(_SESSION));
        routingContext.response().setStatusCode(500);
      }
    });
    //routingContext.response().setStatusCode(200).end(new JsonObject().put("message","hello").toString());
  }

  private void redirect(RoutingContext routingContext) {
    log.info("url hit....");
    log.info(routingContext.request().query());

    if(routingContext.request().query()==null){
      routingContext.redirect("http://localhost:4200");
    }else{
      JsonObject jsonObject = new JsonObject();
      StringBuilder sb = new StringBuilder("");

      String url = "http://localhost:4300/otp?%s=%s&%s=%s";

      String uuid = UUID.randomUUID().toString();
      Session session = store.createSession(600000);
      session.put(uuid, routingContext.request().query());

      store.put(session);
      log.info(String.format(url, _SESSION, session.id(), _UID, uuid));
      //routingContext.redirect("http://localhost:4200?" + routingContext.request().query());
      routingContext.redirect(String.format(url, _SESSION, session.id(), _UID, uuid));
    }

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
