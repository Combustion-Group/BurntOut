
package com.combustiongroup.burntout.network.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class UserInfo {

    private String userId;
    private String username;
    private String email;
    private String picture;
    private String userFname;
    private String userLname;
    private String userFBID;
    private String userIsFB;
    private String firstLogin;
    private String lastLogin;
    private String loginLng;
    private String loginLat;
    private String theranking;
    private String created;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     *     The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return
     *     The picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 
     * @param picture
     *     The picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * 
     * @return
     *     The userFname
     */
    public String getUserFname() {
        return userFname;
    }

    /**
     * 
     * @param userFname
     *     The user_fname
     */
    public void setUserFname(String userFname) {
        this.userFname = userFname;
    }

    /**
     * 
     * @return
     *     The userLname
     */
    public String getUserLname() {
        return userLname;
    }

    /**
     * 
     * @param userLname
     *     The user_lname
     */
    public void setUserLname(String userLname) {
        this.userLname = userLname;
    }

    /**
     * 
     * @return
     *     The userFBID
     */
    public String getUserFBID() {
        return userFBID;
    }

    /**
     * 
     * @param userFBID
     *     The user_FBID
     */
    public void setUserFBID(String userFBID) {
        this.userFBID = userFBID;
    }

    /**
     * 
     * @return
     *     The userIsFB
     */
    public String getUserIsFB() {
        return userIsFB;
    }

    /**
     * 
     * @param userIsFB
     *     The user_isFB
     */
    public void setUserIsFB(String userIsFB) {
        this.userIsFB = userIsFB;
    }

    /**
     * 
     * @return
     *     The firstLogin
     */
    public String getFirstLogin() {
        return firstLogin;
    }

    /**
     * 
     * @param firstLogin
     *     The first_login
     */
    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }

    /**
     * 
     * @return
     *     The lastLogin
     */
    public String getLastLogin() {
        return lastLogin;
    }

    /**
     * 
     * @param lastLogin
     *     The last_login
     */
    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * 
     * @return
     *     The loginLng
     */
    public String getLoginLng() {
        return loginLng;
    }

    /**
     * 
     * @param loginLng
     *     The login_lng
     */
    public void setLoginLng(String loginLng) {
        this.loginLng = loginLng;
    }

    /**
     * 
     * @return
     *     The loginLat
     */
    public String getLoginLat() {
        return loginLat;
    }

    /**
     * 
     * @param loginLat
     *     The login_lat
     */
    public void setLoginLat(String loginLat) {
        this.loginLat = loginLat;
    }

    /**
     * 
     * @return
     *     The theranking
     */
    public String getTheranking() {
        return theranking;
    }

    /**
     * 
     * @param theranking
     *     The theranking
     */
    public void setTheranking(String theranking) {
        this.theranking = theranking;
    }

    /**
     * 
     * @return
     *     The created
     */
    public String getCreated() {
        return created;
    }

    /**
     * 
     * @param created
     *     The created
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

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", picture='" + picture + '\'' +
                ", userFname='" + userFname + '\'' +
                ", userLname='" + userLname + '\'' +
                ", userFBID='" + userFBID + '\'' +
                ", userIsFB='" + userIsFB + '\'' +
                ", firstLogin='" + firstLogin + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", loginLng='" + loginLng + '\'' +
                ", loginLat='" + loginLat + '\'' +
                ", theranking='" + theranking + '\'' +
                ", created='" + created + '\'' +
                '}';
    }

}
