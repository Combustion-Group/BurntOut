package com.combustiongroup.burntout;

import java.io.Serializable;

/**
 * Created by Candy on 6/7/16.
 */
public class Vehicle implements Serializable
{
    public String plate;
    public String model;
    public int resource;
    public String id;
    public String state;
    public Vehicle(String plate, String model, int resource, String id, String state)
    {
        this.plate = plate;
        this.model = model;
        this.resource = resource;
        this.id = id;
        this.state = state;
    }//Constructor

    public static int getResourceForVehicleType(final String vehicleType)
    {
        switch(vehicleType)
        {
            case "0" :
                return R.drawable.image_car_front;
            case "1" :
                return R.drawable.image_bike_front;
            case "2" :
                return R.drawable.image_truck_front;
            case "3" :
                return R.drawable.image_bus_front;
        }

        return R.drawable.image_car_front;
    }//get resource for vehicle type

    public static int getTypeForResource(int resource)
    {
        switch(resource)
        {
            case R.drawable.image_car_front :
                return 0;
            case R.drawable.image_bike_front :
                return 1;
            case R.drawable.image_truck_front :
                return 2;
            case R.drawable.image_bus_front :
                return 3;
        }

        return 0;
    }//get type for resrouce
}//Vehicle
