package com.vertx.vertxpoc.vertx_stock_broker.watchlist;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class WatchListRestApi {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListRestApi.class);

  public static void attach(final Router restApi) {
    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();
    final String path = "/account/watchlist/:accountId";

    restApi.get(path).handler(new GetWatchListHandler(watchListPerAccount));

    restApi.put(path).handler(new PutWatchListHandler(watchListPerAccount));

    restApi.delete(path).handler(new DeleteWatchListHandler(watchListPerAccount));
  }

  static String getString(RoutingContext context) {
    var accountId = context.pathParam("accountId");
    LOG.debug("{} for account {}", context.normalizedPath(), accountId);
    return accountId;
  }
}
