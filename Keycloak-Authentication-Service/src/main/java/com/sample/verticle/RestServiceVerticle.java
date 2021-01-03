package com.sample.verticle;

import com.sample.service.KeycloakService;
import com.sample.service.OTPService;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
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

import java.util.Arrays;
import java.util.UUID;

public class RestServiceVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(RestServiceVerticle.class);
    SessionStore store = null;
    OTPService otpService;
    KeycloakService keycloakService;
    String _SESSION = "session_state";
    String _UID = "prng";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        otpService = OTPService.create(vertx, "otp.service");
        keycloakService = KeycloakService.create(vertx, "keycloak.service");

        Router router = Router.router(vertx);

        store = LocalSessionStore.create(vertx, "keycloak-session-store", 30000);
        router.route().handler(CorsHandler.create(".*.")).failureHandler(routingContext -> {
            log.error("", routingContext.failure());
            routingContext.response().setStatusCode(500).end("Server processing failure");
        });

        router.route("/auth-api/*").handler(BodyHandler.create());

        router.get("/auth-api/redirect").handler(this::redirect);
        router.post("/auth-api/otp-validate").handler(this::otpValidator);

        ConfigRetriever retriever = ConfigRetriever.create(vertx);
        retriever.getConfig(json -> {
            JsonObject config = json.result();
            vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(config.getInteger("http.port"), httpServerAsyncResult -> {
                        if (httpServerAsyncResult.succeeded()) {
                            log.debug("server running at {}", config.getInteger("http.port"));
                        } else {
                            log.warn("", httpServerAsyncResult.cause());
                        }
                    });
        });
    }

    private void otpValidator(RoutingContext routingContext) {

        JsonObject jsonObject = routingContext.getBodyAsJson();

        store.get(jsonObject.getString(_SESSION), sessionAsyncResult -> {
            if (sessionAsyncResult.succeeded()) {
                Session session = sessionAsyncResult.result();
                log.debug(session.get(jsonObject.getString(_UID)));
                log.debug("http://localhost:4200?" + session.get(jsonObject.getString(_UID)));

                JsonObject requestParam = getOTPObject(jsonObject.getString("otp"), session.get(jsonObject.getString(_UID)));

                // ################Validating OTP here ########################################
                validateOTP(routingContext, requestParam, session.get(jsonObject.getString(_UID)));

            } else {
                log.error("no session available for id " + jsonObject.getString(_SESSION));
                routingContext.response().setStatusCode(500);
            }
        });

    }

    private void validateOTP(RoutingContext context, JsonObject requestObject, String queryParam) {

        otpService.validateOTP(requestObject, result -> {
            if (result.succeeded()) {
                log.debug(" otp status : " + result.result().getString("message"));
                if ("retry".equals(result.result().getString("message"))) {
                    context.response().setStatusCode(500).end("Please retry !");
                } else {
                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.put("location", "http://localhost:4200?" + queryParam);
                    context.response().setStatusCode(200).putHeader("content-type", "application/json").end(jsonObject1.toString());
                }
            } else {

                keycloakService.invalidateSession(requestObject, reply -> {
                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.put("location", "http://localhost:4200");
                    context.response().setStatusCode(200).putHeader("content-type", "application/json").end(jsonObject1.toString());
                });
            }
        });


    }


    private void redirect(RoutingContext routingContext) {
        log.info("url hit....");
        log.debug(routingContext.request().query());

        if (routingContext.request().query() == null) {
            routingContext.redirect("http://localhost:4200");
        } else {
            JsonObject requestParam = new JsonObject();
            routingContext.request().params().forEach(param -> {
                requestParam.put(param.getKey(), param.getValue());
            });

            otpService.generateOTP(result -> {
                if (result.succeeded()) {
                    log.info("otp generated " + result.result().toString());
                    String url = "http://localhost:4300/otp?%s=%s&%s=%s";
                    String uuid = UUID.randomUUID().toString();

                    Session session = store.createSession(600000);
                    session.put(uuid, routingContext.request().query());

                    store.put(session);
                    log.info(String.format(url, _SESSION, session.id(), _UID, uuid));

                    routingContext.redirect(String.format(url, _SESSION, session.id(), _UID, uuid));
                }
            });

        }

    }


    private JsonObject getOTPObject(String otp, String queryParam) {
        JsonObject requestParam = new JsonObject();
        requestParam.put("otp", otp);
        Arrays.stream(queryParam.split("&")).forEach(e -> {
            String[] param = e.split("=");
            requestParam.put(param[0], param[1]);
        });

        return requestParam;
    }
}
