package com.example.apple.cryptogid;

public class Coin {
    private String name;
    private int price;
    private boolean up;
    private Integer imgNumber;

    public Coin(String name, int price, boolean up, int imgNumber) {
        this.name = name;
        this.price = price;
        this.up = up;
        this.imgNumber = imgNumber;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public boolean isUp() {
        return up;
    }

    public Integer getImgNumber() {
        return imgNumber;
    }

    public void setUp(boolean status){
        this.up = status;
    }
}
