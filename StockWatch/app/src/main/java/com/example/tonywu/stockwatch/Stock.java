package com.example.tonywu.stockwatch;

public class Stock {

    private String symbol;
    private String name;
    private double price;
    private double pchange;
    private double changePercent;


    public Stock(String symbol, String name, double price, double pchange, double changePercent){
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.pchange = pchange;
        this.changePercent = changePercent;
    }

    public Stock(String symbol, String name){
        this.symbol = symbol;
        this.name = name;
    }

    public String getSymbol(){
        return symbol;
    }
    public String getName(){
        return name;
    }
    public double getPrice(){
        return price;
    }
    public double getPchange(){
        return pchange;
    }
    public double getChangePercent(){
        return changePercent;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void setPchange(double pchange){
        this.pchange = pchange;
    }
    public void setChangePercent(double changePercent){
        this.changePercent = changePercent;
    }

}
