package com.combustiongroup.burntout;

import android.os.Bundle;
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

public class Signup extends AppCompatActivity {

    EditText fname, lname, email, password, passwordc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        View prev = findViewById(R.id.prev);
        assert prev != null;
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        View submit = findViewById(R.id.submit);
        assert submit != null;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkFields())
                {
                    register();
                }
            }
        });

        fname = (EditText) findViewById(R.id.first_name);
        lname = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        passwordc = (EditText) findViewById(R.id.password_confirm);
    }//on create

    boolean checkFields()
    {
        if(!(fname.getText().length() > 0))
        {
            Toast.makeText(Signup.this, getString(R.string.empty_firstname), Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(lname.getText().length() > 0))
        {
            Toast.makeText(Signup.this, getString(R.string.empty_lastname), Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(email.getText().length() > 0))
        {
            Toast.makeText(Signup.this, getString(R.string.error_empty_email), Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(password.getText().length() > 5))
        {
            Toast.makeText(Signup.this, getString(R.string.empty_password), Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(password.getText().toString().equals(passwordc.getText().toString())))
        {
            Toast.makeText(Signup.this, getString(R.string.empty_passwords_dont_match), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }//check fields

    void register()
    {
        //show spinner
//        Intent i = new Intent(Signup.this, Spinner.class);
//        startActivity(i);
        //launch request
        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.Signup.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
//                        Log.w("#app", response);

//                        Spinner.refAct.finish();
                        SignupResponse r = new SignupResponse(response);
                        Log.w("#app", response);
                        if(r.status.equals("one"))
                        {
                            Toast.makeText(Signup.this, getString(R.string.sign_up_successful), Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else if(r.status.equals("three"))
                        {
                            Toast.makeText(Signup.this, getString(R.string.email_has_been_used), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(Signup.this, getString(R.string.error_registrating_account) + "\nError: "+r.status, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
//                        Spinner.refAct.finish();
                        Toast.makeText(Signup.this, getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("email", email.getText().toString());
                params.put("fname", fname.getText().toString());
                params.put("lname", lname.getText().toString());
                params.put("password", password.getText().toString());
                params.put("username", "##");

                return params;
            }
        };
//        Net.singleton.requestQueue.add(req);
        Net.singleton.addRequest(Signup.this, req);
    }//register

    class SignupResponse
    {
        String status;
        public SignupResponse(String raw)
        {
            try
            {
                JSONObject root = new JSONObject(raw);
                status = root.getString("loginstatus");
            }
            catch(Exception c)
            {
                status = "server error";
            }
        }//Constructor
    }//SignupResponse
}//Signup
