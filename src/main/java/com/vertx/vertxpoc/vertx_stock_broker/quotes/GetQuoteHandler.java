package com.vertx.vertxpoc.vertx_stock_broker.quotes;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class GetQuoteHandler implements Handler<RoutingContext> {
  final Map<String, Quote> cachedQuotes;
  private static final Logger LOG = LoggerFactory.getLogger(GetQuoteHandler.class);

  public GetQuoteHandler(Map<String, Quote> cachedQuotes) {
    this.cachedQuotes = cachedQuotes;
  }

  @Override
  public void handle(RoutingContext context) {

    final String assetParam = context.pathParam("asset");
    LOG.info("Asset parameter: {}", assetParam);

    var quote = Optional.ofNullable(cachedQuotes.get(assetParam));

    if (quote.isEmpty()) {
      context.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(
          new JsonObject()
            .put("message", "Quote for asset " + assetParam + " not available")
            .put("path", context.normalizedPath())
            .toBuffer()
        );
      return;
    }

    final JsonObject response = quote.get().toJsonObject();
    LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
    context.response().end(response.toBuffer());

  }
}
