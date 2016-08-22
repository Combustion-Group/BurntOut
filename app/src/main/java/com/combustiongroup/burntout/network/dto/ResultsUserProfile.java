
package com.combustiongroup.burntout.network.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ResultsUserProfile {

    private List<UserInfo> userinfo = new ArrayList<UserInfo>();
    private Stats stats;
    private Preferences preferences;
    private List<Vehicle> vehicles = new ArrayList<Vehicle>();
    private List<Notifications> notifications = new ArrayList<Notifications>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The userinfo
     */
    public List<UserInfo> getUserinfo() {
        return userinfo;
    }

    /**
     * 
     * @param userinfo
     *     The userinfo
     */
    public void setUserinfo(List<UserInfo> userinfo) {
        this.userinfo = userinfo;
    }

    /**
     * 
     * @return
     *     The stats
     */
    public Stats getStats() {
        return stats;
    }

    /**
     * 
     * @param stats
     *     The stats
     */
    public void setStats(Stats stats) {
        this.stats = stats;
    }


    /**
     * 
     * @return
     *     The preferences
     */
    public Preferences getPreferences() {
        return preferences;
    }

    /**
     * 
     * @param preferences
     *     The preferences
     */
    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    /**
     * 
     * @return
     *     The vehicles
     */
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * 
     * @param vehicles
     *     The vehicles
     */
    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    /**
     * 
     * @return
     *     The notifications
     */
    public List<Notifications> getNotifications() {
        return notifications;
    }

    /**
     * 
     * @param notifications
     *     The notifications
     */
    public void setNotifications(List<Notifications> notifications) {
        this.notifications = notifications;
    }

    /**
     * 
     * @return
     *     The userinfotwo
     */


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "ResultsUserProfile{" +
                "userinfo=" + userinfo +
                ", stats=" + stats +
                ", preferences=" + preferences +
                ", vehicles=" + vehicles +
                ", notifications=" + notifications +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
