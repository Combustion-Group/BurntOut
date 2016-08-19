
package com.combustiongroup.burntout.network.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Fname {

    private Filename filename;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The filename
     */
    public Filename getFilename() {
        return filename;
    }

    /**
     * 
     * @param filename
     *     The filename
     */
    public void setFilename(Filename filename) {
        this.filename = filename;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
