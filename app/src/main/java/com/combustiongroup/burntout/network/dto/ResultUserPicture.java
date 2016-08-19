
package com.combustiongroup.burntout.network.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ResultUserPicture {

    private Fname fname;
    private String isgood;
    private Object foo;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The fname
     */
    public Fname getFname() {
        return fname;
    }

    /**
     * 
     * @param fname
     *     The fname
     */
    public void setFname(Fname fname) {
        this.fname = fname;
    }

    /**
     * 
     * @return
     *     The isgood
     */
    public String getIsgood() {
        return isgood;
    }

    /**
     * 
     * @param isgood
     *     The isgood
     */
    public void setIsgood(String isgood) {
        this.isgood = isgood;
    }

    /**
     * 
     * @return
     *     The foo
     */
    public Object getFoo() {
        return foo;
    }

    /**
     * 
     * @param foo
     *     The foo
     */
    public void setFoo(Object foo) {
        this.foo = foo;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
