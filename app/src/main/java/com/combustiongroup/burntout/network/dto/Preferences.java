
package com.combustiongroup.burntout.network.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Preferences {

    private String pushNotifications;
    private String facebookNotifications;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The pushNotifications
     */
    public String getPushNotifications() {
        return pushNotifications;
    }

    /**
     * 
     * @param pushNotifications
     *     The push_notifications
     */
    public void setPushNotifications(String pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    /**
     * 
     * @return
     *     The facebookNotifications
     */
    public String getFacebookNotifications() {
        return facebookNotifications;
    }

    /**
     * 
     * @param facebookNotifications
     *     The facebook_notifications
     */
    public void setFacebookNotifications(String facebookNotifications) {
        this.facebookNotifications = facebookNotifications;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
