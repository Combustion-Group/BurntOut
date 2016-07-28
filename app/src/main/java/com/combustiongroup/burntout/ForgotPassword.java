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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
                if(emailS.length() > 0)
                {
                    int internetPermissionCheck = ContextCompat.checkSelfPermission(ForgotPassword.this, Manifest.permission.INTERNET);
                    if(internetPermissionCheck != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(ForgotPassword.this, new String[]{Manifest.permission.INTERNET}, PermissionRequestInternet);
                    }
                    else
                    {
                        resetPassword();
                    }
                }
                else
                {
                    Toast.makeText(ForgotPassword.this, getString(R.string.error_empty_email), Toast.LENGTH_LONG).show();
                }
            }
        });//submit forgot password
    }//on create

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        if(requestCode == PermissionRequestInternet)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                resetPassword();
            }
            else
            {
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

    void resetPassword()
    {

//        Intent spinner = new Intent(ForgotPassword.this, Spinner.class);
//        startActivity(spinner);

        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.ForgotPassword.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        ParsedResponse pr = new ParsedResponse(response);
                        Log.w("#app", pr.status);
                        //Spinner.refAct.finish();
                        if(pr.status.equals("one"))
                        {
                            Toast.makeText(ForgotPassword.this, getString(R.string.password_emailed), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(ForgotPassword.this, getString(R.string.error_password_emailed), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
//                        Log.w("#app", new String(error.networkResponse.data));
                        //Spinner.refAct.finish();
                        Toast.makeText(ForgotPassword.this, getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();

                params.put("email", email.getText().toString());

                return params;
            }
        };
//        Net.singleton.requestQueue.add(req);
        Net.singleton.addRequest(ForgotPassword.this, req);
    }//reset password

    class ParsedResponse
    {
        public String status;
        public ParsedResponse(String raw)
        {
            try
            {
                JSONObject jsRoot = new JSONObject(raw);
                status = jsRoot.getString("status");
            }
            catch(Exception c)
            {
                status = "four";//unable to parse response
                c.printStackTrace();
            }
        }//Constructor
    }//ParsedResponse
}//ForgotPassword
