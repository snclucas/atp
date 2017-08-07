package com.atp.trade;


public enum TradeType {
  BUY("Buy", 1), SELL("Sell", -1); // Buying costs you money therefore -1

  private String tag;
  private int value;

  TradeType(String tag, int value) {
    this.tag = tag; this.value = value;
  }

  public String getTag() {
    return tag;
  }

  public int getValue() {
    return value;
  }

};
