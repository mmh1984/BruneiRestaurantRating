package com.example.maynard.bruneirestaurantrating;

/**
 * Created by Maynard on 4/7/2017.
 */

public class Resto {

    String name;
    String address;
    String district;
    String type;
    String coordinate;
    String year;

    public Resto(String name, String address, String district, String type, String coordinate, String year) {
        this.name = name;
        this.address = address;
        this.district = district;
        this.type = type;
        this.coordinate = coordinate;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
