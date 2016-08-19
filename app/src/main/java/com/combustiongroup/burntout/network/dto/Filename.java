
package com.combustiongroup.burntout.network.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Filename {

    private String name;
    private String type;
    private String tmpName;
    private Integer error;
    private Integer size;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The tmpName
     */
    public String getTmpName() {
        return tmpName;
    }

    /**
     * 
     * @param tmpName
     *     The tmp_name
     */
    public void setTmpName(String tmpName) {
        this.tmpName = tmpName;
    }

    /**
     * 
     * @return
     *     The error
     */
    public Integer getError() {
        return error;
    }

    /**
     * 
     * @param error
     *     The error
     */
    public void setError(Integer error) {
        this.error = error;
    }

    /**
     * 
     * @return
     *     The size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 
     * @param size
     *     The size
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
