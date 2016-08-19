package com.combustiongroup.burntout.network.dto.request;


import com.combustiongroup.burntout.MyGcmListenerService;
import com.combustiongroup.burntout.RegistrationIntentService;

/**
 * Created by qualixium-imac on 8/18/16.
 */

public class LoginRequest {

    private String email, password, device = "Android", token = RegistrationIntentService.token;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
