
package com.combustiongroup.burntout.network.dto;

import com.combustiongroup.burntout.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Vehicle implements Serializable {

    private String vehicle_type_id;
    private String car_model;
    private String plate_number;
    private String plate_state;
    private String vehicle_id;
    private String created;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    //    public Vehicle(String plate_number, String model, int resource, String id, String state)
    public Vehicle(String plate, String model,String vehicleTypeId, String state)
    {
        this.plate_number = plate;
        this.car_model = model;
        this.vehicle_type_id = vehicleTypeId;
        this.plate_state = state;
    }//Constructor

    /**
     * @return The vehicle_type_id
     */
    public String getVehicle_type_id() {
        return vehicle_type_id;
    }

    /**
     * @param vehicle_type_id The vehicle_type_id
     */
    public void setVehicle_type_id(String vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    /**
     * @return The car_model
     */
    public String getCar_model() {
        return car_model;
    }

    /**
     * @param car_model The car_model
     */
    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    /**
     * @return The plate_number
     */
    public String getPlate_number() {
        return plate_number;
    }

    /**
     * @param plate_number The plate_number
     */
    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }


    /**
     * @return The plate_state
     */
    public String getPlate_state() {
        return plate_state;
    }

    /**
     * @param plate_state The plate_state
     */
    public void setPlate_state(String plate_state) {
        this.plate_state = plate_state;
    }

    /**
     * @return The vehicle_id
     */
    public String getVehicle_id() {
        return vehicle_id;
    }

    /**
     * @param vehicle_id The vehicle_id
     */
    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    /**
     * @return The created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public int getResourceForVehicleType(String aVehicleTypeId) {
        switch (aVehicleTypeId) {
            case "0":
                return R.drawable.image_car_front;
            case "1":
                return R.drawable.image_bike_front;
            case "2":
                return R.drawable.image_bus_front;
            case "3":
                return R.drawable.image_truck_front;
        }

        return R.drawable.image_car_front;
    }//get resource for vehicle type

    public int getTypeForResource(int aResource) {
        switch (aResource) {
            case R.drawable.image_car_front:
                return 0;
            case R.drawable.image_bike_front:
                return 1;
            case R.drawable.image_bus_front:
                return 2;
            case R.drawable.image_truck_front:
                return 3;
        }

        return 0;
    }//get type for resrouce

}
