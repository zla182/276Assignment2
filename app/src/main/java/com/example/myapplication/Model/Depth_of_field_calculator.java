package com.example.myapplication.Model;

public class Depth_of_field_calculator {
    private static double HyperFocalDistance;
    private static double NearFocalPoint;
    private static double FarFocalPoint;
    private static double DepthOfField;

    public static double getHyperFocalDistance() {
        return HyperFocalDistance;
    }

    public static double getNearFocalPoint() {
        return NearFocalPoint;
    }

    public static double getFarFocalPoint() {
        return FarFocalPoint;
    }

    public static double getDepthOfField() {
        return DepthOfField;
    }

    // calculate
    public Depth_of_field_calculator(Lens lens, double F_number, double distance, double Circle_of_Confusion) {
        //error case
        if(F_number < lens.getMaximum_aperture()){
            HyperFocalDistance = 0;
            NearFocalPoint = 0;
            FarFocalPoint = 0;
        }
        else {//normal case
            //Hyper focal distance [mm]
            //= (lens focal length [mm])2
            // / (selected aperture * camera’s circle of confusion [mm])
            HyperFocalDistance = ((double) lens.getFocal_length()) * ((double) lens.getFocal_length()) / (F_number * Circle_of_Confusion) / 1000;

            //Near Focal Point [mm]
            //= (hyper focal point [mm] * distance to subject [mm])
            // /
            // (hyper focal point [mm] + (distance to subject [mm] – lens focal length [mm])
            NearFocalPoint = (HyperFocalDistance * distance) / (HyperFocalDistance + (distance - (double) lens.getFocal_length() / 1000));

            //Far Focal Point [mm]
            //= (hyper focal point [mm] * distance to subject [mm])
            // /
            // (hyper focal point [mm] – (distance to subject [mm] – lens focal length [mm])
            //If the subject is beyond the hyperfocal distance, then the far focal point is +Infinity
            if (distance > HyperFocalDistance)
                FarFocalPoint = Double.POSITIVE_INFINITY;
            else{
                FarFocalPoint = (HyperFocalDistance * distance) / (HyperFocalDistance - (distance - (double) lens.getFocal_length() / 1000));
            }
        }

        //Depth of field [mm]
        //= (far focal point [mm]) - (near focal point [mm])
        DepthOfField = FarFocalPoint - NearFocalPoint;
    }
}
