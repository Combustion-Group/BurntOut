
package com.combustiongroup.burntout.network.dto;

import com.combustiongroup.burntout.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Vehicle implements Serializable {

    private String vehicleTypeId;
    private String carModel;
    private String plateNumber;
    private String plateState;
    private String vehicleId;
    private String created;
    private int resource;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    //    public Vehicle(String plate, String model, int resource, String id, String state)
    public Vehicle(String plate, String model, int resource, String id, String state)

    {
        this.plateNumber = plate;
        this.carModel = model;
        this.resource = resource;
        this.vehicleId = id;
        this.plateState = state;
    }//Constructor

    /**
     * @return The vehicleTypeId
     */
    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    /**
     * @param vehicleTypeId The vehicle_type_id
     */
    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    /**
     * @return The carModel
     */
    public String getCarModel() {
        return carModel;
    }

    /**
     * @param carModel The car_model
     */
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    /**
     * @return The plateNumber
     */
    public String getPlateNumber() {
        return plateNumber;
    }

    /**
     * @param plateNumber The plate_number
     */
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getResource() {
        return resource;
    }

    /**
     * @return The plateState
     */
    public String getPlateState() {
        return plateState;
    }

    /**
     * @param plateState The plate_state
     */
    public void setPlateState(String plateState) {
        this.plateState = plateState;
    }

    /**
     * @return The vehicleId
     */
    public String getVehicleId() {
        return vehicleId;
    }

    /**
     * @param vehicleId The vehicle_id
     */
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
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

    public static int getResourceForVehicleType(final String vehicleType) {
        switch (vehicleType) {
            case "0":
                return R.drawable.image_car_front;
            case "1":
                return R.drawable.image_bike_front;
            case "2":
                return R.drawable.image_truck_front;
            case "3":
                return R.drawable.image_bus_front;
        }

        return R.drawable.image_car_front;
    }//get resource for vehicle type

    public static int getTypeForResource(int resource) {
        switch (resource) {
            case R.drawable.image_car_front:
                return 0;
            case R.drawable.image_bike_front:
                return 1;
            case R.drawable.image_truck_front:
                return 2;
            case R.drawable.image_bus_front:
                return 3;
        }

        return 0;
    }//get type for resrouce

}
