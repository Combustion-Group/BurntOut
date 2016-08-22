package com.combustiongroup.burntout.network.dto.response;

/**
 * Created by qualixium-imac on 8/20/16.
 */

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class StatusResponse {


    @SerializedName("status")
    @Expose
    private String status;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
