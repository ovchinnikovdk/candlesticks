package com.demo.candlesticks.model;

/*
 *
 * Created by mi on 16.05.2019
 *
 */

public class Trade implements Comparable<Trade> {
    private String trade_id;
    private long timestamp;
    private String price;
    private String quantity;
    private String cost;

    public Trade() {

    }

    public Trade(String trade_id, long timestamp, String price, String quantity, String cost) {
        this.trade_id = trade_id;
        this.timestamp = timestamp;
        this.price = price;
        this.quantity = quantity;
        this.cost = cost;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public int compareTo(Trade o) {
        return Long.compare(this.getTimestamp(), o.timestamp);
    }
}

