package com.vertx.vertxpoc.vertx_stock_broker.vertx_stock_broker.quotes;

import com.vertx.vertxpoc.vertx_stock_broker.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestQuotesRestApi {
  private static final Logger LOG = LoggerFactory.getLogger(TestQuotesRestApi.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void returns_quotes_for_assets(Vertx vertx, VertxTestContext testContext) throws Throwable{
    WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT))
      .get("/quotes/AMZN")
      .send()
      .onComplete(testContext.succeeding( response -> {
        var json = response.bodyAsJsonObject();
        LOG.info("Response: {}", json);
        assertEquals("{\"name\":\"AMZN\"}", json.getJsonObject("asset").encode());
        assertEquals(200, response.statusCode());
        testContext.completeNow();
      }));
  }

  @Test
  void returns_not_found_for_unknown(Vertx vertx, VertxTestContext testContext) throws Throwable{
    WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT))
      .get("/quotes/AVC")
      .send()
      .onComplete(testContext.succeeding( response -> {
        var json = response.bodyAsJsonObject();
        LOG.info("Response: {}", json);
        assertEquals("{\"message\":\"Quote for asset AVC not available\",\"path\":\"/quotes/AVC\"}", json.encode());
        assertEquals(404, response.statusCode());
        testContext.completeNow();
      }));
  }
}
