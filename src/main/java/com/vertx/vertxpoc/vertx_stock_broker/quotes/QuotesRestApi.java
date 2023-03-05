package com.vertx.vertxpoc.vertx_stock_broker.quotes;

import com.vertx.vertxpoc.vertx_stock_broker.assets.Asset;
import com.vertx.vertxpoc.vertx_stock_broker.assets.AssetsRestApi;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesRestApi {
  private static final Logger LOG = LoggerFactory.getLogger(QuotesRestApi.class);

  public static void attach(Router restApi){

    final Map<String, Quote> cachedQuotes = new HashMap<>();
    AssetsRestApi.ASSETS.forEach(ass -> {
      cachedQuotes.put(ass, initRandomQuote(ass));
    });

    restApi.get("/quotes/:asset").handler(new GetQuoteHandler(cachedQuotes));
  }



  private static Quote initRandomQuote(String assetParam) {
    return Quote.builder()
      .asset(new Asset(assetParam))
      .volume(randomValue())
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));

  }


}
