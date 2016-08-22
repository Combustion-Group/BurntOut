package com.combustiongroup.burntout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.response.StatusResponse;
import com.combustiongroup.burntout.util.Functions;
import com.combustiongroup.burntout.util.SpinnerAlert;

import retrofit2.Call;
import retrofit2.Callback;

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

        fname.setText(BOAPI.gUserInfo.getUser_fname());
        lname.setText(BOAPI.gUserInfo.getUser_lname());
        email.setText(BOAPI.gUserInfo.getEmail());

        //test
//        Main.gUserInfo.isFB = true;

        if (BOAPI.gUserInfo.getUser_FBID().equals("1")) {
            email.setVisibility(View.GONE);
            changePassword.setVisibility(View.GONE);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (preSubmit()) {
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

    boolean preSubmit() {

        if (fname.getText().length() < 1) {
            Toast.makeText(SettingsProfileEdit.this, getString(R.string.empty_firstname), Toast.LENGTH_LONG).show();
            return false;
        } else if (lname.getText().length() < 1) {
            Toast.makeText(SettingsProfileEdit.this, getString(R.string.empty_lastname), Toast.LENGTH_LONG).show();
            return false;
        } else if (email.getText().length() < 1) {
            Toast.makeText(SettingsProfileEdit.this, getString(R.string.error_empty_email), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }//pre submit

    void updateUserInfo() {
        SpinnerAlert.show(SettingsProfileEdit.this);

        BOAPI.service.editProfileInformation(
                fname.getText().toString(),
                lname.getText().toString(),
                BOAPI.gUserInfo.getEmail(),
                email.getText().toString()).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, retrofit2.Response<StatusResponse> response) {
                if (response.body().getStatus().equals("one")) {
                    Toast.makeText(SettingsProfileEdit.this, getString(R.string.updating_information), Toast.LENGTH_LONG).show();
                    BOAPI.gUserInfo.setUser_fname(fname.getText().toString());
                    BOAPI.gUserInfo.setUser_lname(lname.getText().toString());
                    BOAPI.gUserInfo.setEmail(email.getText().toString());

                    Main.dataSetModified = true;
                    finish();

                } else {
                    Toast.makeText(SettingsProfileEdit.this, getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();
                    finish();
                }

                SpinnerAlert.dismiss(SettingsProfileEdit.this);
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                SpinnerAlert.dismiss(SettingsProfileEdit.this);
                finish();
            }
        });

    }//update user info
}//SettingsProfileEdit
