package com.vertx.vertxpoc.vertx_stock_broker.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class GetWatchListHandler implements Handler<RoutingContext> {
  final HashMap<UUID, WatchList> watchListPerAccount;

  public GetWatchListHandler(HashMap<UUID, WatchList> watchListPerAccount) {
    this.watchListPerAccount = watchListPerAccount;
  }

  @Override
  public void handle(RoutingContext context) {
    String accountId = WatchListRestApi.getString(context);
    var watchList = Optional.ofNullable(watchListPerAccount.get(UUID.fromString(accountId)));
    if (watchList.isEmpty()) {
      context.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(
          new JsonObject()
            .put("message", "Watchlist for account " + accountId + " not available")
            .put("path", context.normalizedPath())
            .toBuffer()
        );
      return;
    }
    context.response().end(watchList.get().toJsonObject().toBuffer());
  }
}
