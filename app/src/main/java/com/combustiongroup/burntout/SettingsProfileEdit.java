package com.combustiongroup.burntout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.combustiongroup.burntout.network.BOAPI;

import java.util.HashMap;
import java.util.Map;

public class SettingsProfileEdit extends AppCompatActivity {

    EditText fname;
    EditText lname;
    EditText email;
    View submit;
    View changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile_edit);

        fname = (EditText) findViewById(R.id.first_name);
        lname = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        submit = findViewById(R.id.submit);
        changePassword = findViewById(R.id.change_password);

        assert fname != null && lname != null && email != null && submit != null && changePassword != null;

        fname.setText(BOAPI.userInfo.getUserFname());
        lname.setText(BOAPI.userInfo.getUserLname());
        email.setText(BOAPI.userInfo.getEmail());

        //test
//        Main.userInfo.isFB = true;

        if(BOAPI.userInfo.getUserFBID().equals("1"))
        {
            email.setVisibility(View.GONE);
            changePassword.setVisibility(View.GONE);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(preSubmit())
                {
                    Functions.closeSoftKeyboard(SettingsProfileEdit.this);
                    updateUserInfo();
                }
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingsProfileEdit.this, ChangePassword.class);
                startActivity(i);
            }
        });
    }//on create

    boolean preSubmit()
    {

        if(fname.getText().length() < 1)
        {
            Toast.makeText(SettingsProfileEdit.this, getString(R.string.empty_firstname), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(lname.getText().length() < 1)
        {
            Toast.makeText(SettingsProfileEdit.this, getString(R.string.empty_lastname), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(email.getText().length() < 1)
        {
            Toast.makeText(SettingsProfileEdit.this, getString(R.string.error_empty_email), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }//pre submit

    void updateUserInfo()
    {

        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.EditUserInfo.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String status = Main.getStatusFromSimple(response);
                        if(status.equals("one"))
                        {
//                            Toast.makeText(SettingsProfileEdit.this, getString(R.string.updating_information), Toast.LENGTH_LONG).show();
//                            Main.userInfo.fname = fname.getText().toString();
//                            Main.userInfo.lname = lname.getText().toString();
//                            Main.userInfo.email = email.getText().toString();
//                            Main.userInfo.dataSetModified = true;
                        }
                        else
                        {
                            Toast.makeText(SettingsProfileEdit.this, getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("f_name", fname.getText().toString());
                params.put("l_name", lname.getText().toString());
//                params.put("oldEmail", Main.userInfo.email);
                params.put("newEmail", email.getText().toString());

                return params;
            }
        };
        Net.singleton.addRequest(SettingsProfileEdit.this, req);
    }//update user info
}//SettingsProfileEdit
