package com.vertx.vertxpoc.vertx_stock_broker.watchlist;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.UUID;

public class PutWatchListHandler implements Handler<RoutingContext> {
  final HashMap<UUID, WatchList> watchListPerAccount;

  public PutWatchListHandler(HashMap<UUID, WatchList> watchListPerAccount) {
    this.watchListPerAccount = watchListPerAccount;
  }

  @Override
  public void handle(RoutingContext context) {
    String accountId = WatchListRestApi.getString(context);

    var json = context.getBodyAsJson();
    var watchlist = json.mapTo(WatchList.class);
    watchListPerAccount.put(UUID.fromString(accountId), watchlist);
    context.response().end(json.toBuffer());
  }
}
