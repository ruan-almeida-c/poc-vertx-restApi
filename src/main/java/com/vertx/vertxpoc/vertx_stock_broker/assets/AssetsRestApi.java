package com.vertx.vertxpoc.vertx_stock_broker.assets;

import io.vertx.ext.web.Router;

import java.util.Arrays;
import java.util.List;

public class AssetsRestApi {
  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA");

  public static void attach(Router parent) {
    parent.get("/assets").handler(new GetAssetsHandler());
  }

}
