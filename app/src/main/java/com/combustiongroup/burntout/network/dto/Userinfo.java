
package com.combustiongroup.burntout.network.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class UserInfo {

    private String user_id;
    private String username;
    private String email;
    private String picture;
    private String user_fname;
    private String user_lname;
    private String user_FBID;
    private String user_isFB;
    private String first_login;
    private String last_login;
    private String login_lng;
    private String login_lat;
    private String theranking;
    private String created;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The user_id
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * 
     * @param user_id
     *     The user_id
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
     *     The user_fname
     */
    public String getUser_fname() {
        return user_fname;
    }

    /**
     * 
     * @param user_fname
     *     The user_fname
     */
    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    /**
     * 
     * @return
     *     The user_lname
     */
    public String getUser_lname() {
        return user_lname;
    }

    /**
     * 
     * @param user_lname
     *     The user_lname
     */
    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    /**
     * 
     * @return
     *     The user_FBID
     */
    public String getUser_FBID() {
        return user_FBID;
    }

    /**
     * 
     * @param user_FBID
     *     The user_FBID
     */
    public void setUser_FBID(String user_FBID) {
        this.user_FBID = user_FBID;
    }

    /**
     * 
     * @return
     *     The user_isFB
     */
    public String getUser_isFB() {
        return user_isFB;
    }

    /**
     * 
     * @param user_isFB
     *     The user_isFB
     */
    public void setUser_isFB(String user_isFB) {
        this.user_isFB = user_isFB;
    }

    /**
     * 
     * @return
     *     The first_login
     */
    public String getFirst_login() {
        return first_login;
    }

    /**
     * 
     * @param first_login
     *     The first_login
     */
    public void setFirst_login(String first_login) {
        this.first_login = first_login;
    }

    /**
     * 
     * @return
     *     The last_login
     */
    public String getLast_login() {
        return last_login;
    }

    /**
     * 
     * @param last_login
     *     The last_login
     */
    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    /**
     * 
     * @return
     *     The login_lng
     */
    public String getLogin_lng() {
        return login_lng;
    }

    /**
     * 
     * @param login_lng
     *     The login_lng
     */
    public void setLogin_lng(String login_lng) {
        this.login_lng = login_lng;
    }

    /**
     * 
     * @return
     *     The login_lat
     */
    public String getLogin_lat() {
        return login_lat;
    }

    /**
     * 
     * @param login_lat
     *     The login_lat
     */
    public void setLogin_lat(String login_lat) {
        this.login_lat = login_lat;
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
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", picture='" + picture + '\'' +
                ", user_fname='" + user_fname + '\'' +
                ", user_lname='" + user_lname + '\'' +
                ", user_FBID='" + user_FBID + '\'' +
                ", user_isFB='" + user_isFB + '\'' +
                ", first_login='" + first_login + '\'' +
                ", last_login='" + last_login + '\'' +
                ", login_lng='" + login_lng + '\'' +
                ", login_lat='" + login_lat + '\'' +
                ", theranking='" + theranking + '\'' +
                ", created='" + created + '\'' +
                '}';
    }

}
