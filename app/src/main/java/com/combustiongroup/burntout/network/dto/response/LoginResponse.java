package com.combustiongroup.burntout.network.dto.response;


/**
 * Created by William Matias on 8/18/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class LoginResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("picture")
    @Expose
    private String picture;

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

    /**
     * @return The fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname The fname
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return The lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname The lname
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture The picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "status='" + status + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
