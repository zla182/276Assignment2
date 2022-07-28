package com.example.myapplication.Model;

public class Lens {
    private  String make;
    private double maximum_aperture;
    private int focal_length;

    public Lens(String make, double maximum_aperture, int focal_length) {
        this.make = make;
        this.maximum_aperture = maximum_aperture;
        this.focal_length = focal_length;
    }

    public void setMake(String make)
    {
        this.make = make;
    }

    public void setMaximum_aperture(double maximum_aperture)
    {
        this.maximum_aperture = maximum_aperture;
    }

    public void setFocal_length(int focal_length)
    {
        this.focal_length = focal_length;
    }

    public double getMaximum_aperture() {
        return maximum_aperture;
    }

    public String getMake() {
        return make;
    }

    public int getFocal_length() {
        return focal_length;
    }

    @Override
    public String toString() {
        return  make + ' ' +
                focal_length + "mm " +
                "F" + maximum_aperture;
    }
}
