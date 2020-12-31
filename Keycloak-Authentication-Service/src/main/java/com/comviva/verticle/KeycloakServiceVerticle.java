package com.comviva.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeycloakServiceVerticle extends AbstractVerticle {
  private final Logger log = LoggerFactory.getLogger(KeycloakServiceVerticle.class);

  Keycloak keycloak = null;

  @Override
  public void start() throws Exception {

    //keycloak = KeycloakBuilder.builder().build();

    ConfigRetriever retriever = ConfigRetriever.create(vertx);
    retriever.getConfig(json -> {
      JsonObject config = json.result().getJsonObject("keycloak-config");

      log.debug("keycloak config : {} ",config);
      keycloak = KeycloakBuilder.builder()
        .serverUrl(config.getString("auth-server-url"))
        .realm(config.getString("realm"))
        .grantType(OAuth2Constants.PASSWORD)
        .clientId(config.getString("resource"))
        .clientSecret(config.getJsonObject("credentials").getString("secret"))
        .username("user1").password("password")
        .build();
      vertx.eventBus().consumer("GET_TOKEN", this::getToken);
    });
  }

  private void getToken(Message msg) {

    JsonObject jsonObject = (JsonObject) msg.body();
    log.info(">>>" + jsonObject);
    log.info("<<<" + keycloak.tokenManager().getAccessToken().getTokenType());

    JsonObject responseObject = new JsonObject();
    responseObject.put("access-token", keycloak.tokenManager().getAccessToken().getToken());
    responseObject.put("refresh_token", keycloak.tokenManager().getAccessToken().getRefreshToken());
    responseObject.put("expires_in" , keycloak.tokenManager().getAccessToken().getExpiresIn());

    msg.reply(responseObject);
    //msg.reply(keycloak.tokenManager().getAccessToken());
  }
}
