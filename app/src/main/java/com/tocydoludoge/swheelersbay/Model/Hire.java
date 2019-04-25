package com.tocydoludoge.swheelersbay.Model;

public class Hire {
    private String PlateId;
    private String CarName;
    private String Rate;
    private String Discount;
    private String Duration;


    public Hire() {
    }

    public Hire(String plateId, String carName, String rate, String discount, String duration) {
        PlateId = plateId;
        CarName = carName;
        Rate = rate;
        Discount = discount;
        Duration = duration;
    }

    public String getPlateId() {
        return PlateId;
    }

    public void setPlateId(String plateId) {
        PlateId = plateId;
    }

    public String getCarName() {
        return CarName;
    }

    public void setCarName(String carName) {
        CarName = carName;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }
}
