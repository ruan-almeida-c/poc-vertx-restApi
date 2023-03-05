package com.vertx.vertxpoc.vertx_stock_broker;

import com.vertx.vertxpoc.vertx_stock_broker.assets.AssetsRestApi;
import com.vertx.vertxpoc.vertx_stock_broker.quotes.QuotesRestApi;
import com.vertx.vertxpoc.vertx_stock_broker.watchlist.WatchListRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestApiVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(RestApiVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startHttpServerAndAttachRoutes(startPromise);
  }

  private void startHttpServerAndAttachRoutes(Promise<Void> startPromise) {
    final Router restApi = Router.router(vertx);

    restApi.route()
      .handler(BodyHandler.create())
      .failureHandler(getFailureHandler());

    AssetsRestApi.attach(restApi);
    QuotesRestApi.attach(restApi);
    WatchListRestApi.attach(restApi);

    vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> LOG.error("HTTP Server error: ", error))
      .listen(MainVerticle.PORT, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.info("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private static Handler<RoutingContext> getFailureHandler() {
    return error -> {
      if (error.response().ended()) {
        return;
      }

      LOG.info("This is the error: ", error.failure());
      error.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "Something is wrong went wrong: (").toBuffer());
    };
  }
}
