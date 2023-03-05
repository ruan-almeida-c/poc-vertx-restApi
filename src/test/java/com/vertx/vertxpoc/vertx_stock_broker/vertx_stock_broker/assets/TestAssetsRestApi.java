package com.vertx.vertxpoc.vertx_stock_broker.vertx_stock_broker.assets;

import com.vertx.vertxpoc.vertx_stock_broker.MainVerticle;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
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
public class TestAssetsRestApi {
  private static final Logger LOG = LoggerFactory.getLogger(TestAssetsRestApi.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void returns_all_assets(Vertx vertx, VertxTestContext testContext) throws Throwable{
    WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT))
      .get("/assets")
      .send()
      .onComplete(testContext.succeeding( response -> {
        var json = response.bodyAsJsonArray();
        LOG.info("Response: {}", json);
        assertEquals("[{\"name\":\"AAPL\"},{\"name\":\"AMZN\"},{\"name\":\"FB\"},{\"name\":\"GOOG\"},{\"name\":\"MSFT\"},{\"name\":\"NFLX\"},{\"name\":\"TSLA\"}]", json.encode());
        assertEquals(200, response.statusCode());
        assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
        testContext.completeNow();
      }));
  }
}
