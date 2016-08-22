
package com.combustiongroup.burntout.network.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Preferences {

    private String push_notifications;
    private String facebook_notifications;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The push_notifications
     */
    public String getPush_notifications() {
        return push_notifications;
    }

    /**
     * 
     * @param push_notifications
     *     The push_notifications
     */
    public void setPush_notifications(String push_notifications) {
        this.push_notifications = push_notifications;
    }

    /**
     * 
     * @return
     *     The facebook_notifications
     */
    public String getFacebook_notifications() {
        return facebook_notifications;
    }

    /**
     * 
     * @param facebook_notifications
     *     The facebook_notifications
     */
    public void setFacebook_notifications(String facebook_notifications) {
        this.facebook_notifications = facebook_notifications;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
