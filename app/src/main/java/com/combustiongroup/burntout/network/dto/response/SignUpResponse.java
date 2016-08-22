
package com.combustiongroup.burntout.network.dto.response;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SignUpResponse {

    @SerializedName("logintype")
    @Expose
    private String logintype;
    @SerializedName("loginstatus")
    @Expose
    private String loginstatus;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;

    /**
     * 
     * @return
     *     The logintype
     */
    public String getLogintype() {
        return logintype;
    }

    /**
     * 
     * @param logintype
     *     The logintype
     */
    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    /**
     * 
     * @return
     *     The loginstatus
     */
    public String getLoginstatus() {
        return loginstatus;
    }

    /**
     * 
     * @param loginstatus
     *     The loginstatus
     */
    public void setLoginstatus(String loginstatus) {
        this.loginstatus = loginstatus;
    }

    /**
     * 
     * @return
     *     The fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * 
     * @param fname
     *     The fname
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * 
     * @return
     *     The lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * 
     * @param lname
     *     The lname
     */
    public void setLname(String lname) {
        this.lname = lname;
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

}
