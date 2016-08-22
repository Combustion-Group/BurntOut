package com.combustiongroup.burntout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.response.StatusResponse;
import com.combustiongroup.burntout.util.SpinnerAlert;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPassword extends AppCompatActivity {

    final int PermissionRequestInternet = 0;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        View prev = findViewById(R.id.prev);
        assert prev != null;
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        email = (EditText) findViewById(R.id.email);
        View forgot = findViewById(R.id.submit);
        assert email != null;
        assert forgot != null;
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailS = email.getText().toString();
                if (emailS.length() > 0) {
                    int internetPermissionCheck = ContextCompat.checkSelfPermission(ForgotPassword.this, Manifest.permission.INTERNET);
                    if (internetPermissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ForgotPassword.this, new String[]{Manifest.permission.INTERNET}, PermissionRequestInternet);
                    } else {
                        resetPassword();
                    }
                } else {
                    Toast.makeText(ForgotPassword.this, getString(R.string.error_empty_email), Toast.LENGTH_LONG).show();
                }
            }
        });//submit forgot password
    }//on create

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PermissionRequestInternet) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                resetPassword();
            } else {
                AlertDialog.Builder adb = new AlertDialog.Builder(ForgotPassword.this);
                adb.setMessage("Internet Permission is required to run this application")
                        .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                System.exit(0);
                            }
                        });
                adb.setCancelable(false);
                AlertDialog alert = adb.create();
                alert.show();
            }
        }
    }//on request permissions result

    void resetPassword() {
        SpinnerAlert.show(ForgotPassword.this);

        BOAPI.service.resetPassword(email.getText().toString()).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, retrofit2.Response<StatusResponse> response) {
                if (response.body().getStatus().equals("one")) {
                    Toast.makeText(ForgotPassword.this, getString(R.string.password_emailed), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPassword.this, getString(R.string.error_password_emailed), Toast.LENGTH_LONG).show();
                }
                SpinnerAlert.dismiss(ForgotPassword.this);

            }
            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                SpinnerAlert.dismiss(ForgotPassword.this);

                Toast.makeText(ForgotPassword.this, getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();
            }
        });


    }//reset password

}//ForgotPassword
