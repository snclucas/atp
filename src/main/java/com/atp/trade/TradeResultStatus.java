package com.atp.trade;

public enum TradeResultStatus {
  SUCCSESFUL_TRADE("SUCCSESFUL_TRADE"),
  NO_POSITION_TO_CLOSE("NO_POSITION_TO_CLOSE"),
  NOT_ENOUGH_CAPITAL("NOT_ENOUGH_CAPITAL"),
  NO_MORE_POSITIONS_ALLOWED("NO_MORE_POSITIONS_ALLOWED"),
  CANNOT_SATISFY_TRADE_RULE("CANNOT_SATISFY_TRADE_RULE"),
  INVALID_TRADE("INVALID_TRADE"),
  NO_SHORT_SELLING("NO_SHORT_SELLING");


  private String tag;
  TradeResultStatus(String tag) { this.tag = tag;}
  public String get() { return tag; }
};
