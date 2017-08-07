package com.atp.trade;

public enum TradeAction {
  TO_OPEN("To open"), TO_CLOSE("To close"), STOP_LOSS("Stop loss"), TAKE_PROFIT("Take profit"), HOLDING_PERIOD("Holding period"), ANY("Any");
  private String tag;
  TradeAction(String tag) { this.tag = tag;}
  public String getTag() { return tag; }
}
