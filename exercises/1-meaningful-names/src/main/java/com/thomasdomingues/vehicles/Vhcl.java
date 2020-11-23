package com.thomasdomingues.vehicles;

public class Vhcl {
    protected String license_number;
    protected String make;
    protected String vehicleModel;
    protected double priceInfo;

    public Vhcl(String l_n, String v_make, String v_model, double v_price) {
        license_number = l_n;
        make = v_make;
        vehicleModel = v_model;
        priceInfo = v_price;
    }

    public String getVhclName() {
        return make + " " + vehicleModel;
    }

    public double price() {
        return priceInfo;
    }

    public String toString() {
        String result;

        result = "License # : " + license_number + "\nVehicle Name : " + getVhclName() + "\nPrice : " + price;
        return result;
    }
}
