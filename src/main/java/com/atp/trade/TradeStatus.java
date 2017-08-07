package com.atp.trade;


public enum TradeStatus {
  ACTIVE("Active"), SUCCESSFULL("Successful"), INACTIVE("Inactive"), CLOSED("Closed"), ANY("Any");
  private String tag;
  TradeStatus(String tag) { this.tag = tag;}
  public String getTag() { return tag; }
}
