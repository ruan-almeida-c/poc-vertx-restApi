package com.vertx.vertxpoc.vertx_stock_broker.watchlist;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class DeleteWatchListHandler implements Handler <RoutingContext>{
  final HashMap<UUID, WatchList> watchListPerAccount;

  private static final Logger LOG = LoggerFactory.getLogger(DeleteWatchListHandler.class);

  public DeleteWatchListHandler(HashMap<UUID, WatchList> watchListPerAccount) {
    this.watchListPerAccount = watchListPerAccount;
  }

  @Override
  public void handle(RoutingContext context) {
    String accountId = WatchListRestApi.getString(context);
    final WatchList removed = watchListPerAccount.remove(UUID.fromString(accountId));
    LOG.info("Deleted: {}, ramaining: {}", removed, watchListPerAccount.values());
    context.response().end(removed.toJsonObject().toBuffer());
  }
}
