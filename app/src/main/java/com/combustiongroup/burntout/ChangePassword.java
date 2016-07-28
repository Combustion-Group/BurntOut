package com.combustiongroup.burntout;

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

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    EditText oldpass, newpass, confirmpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldpass = getElementForId(R.id.password_old);
        newpass = getElementForId(R.id.password);
        confirmpass = getElementForId(R.id.password_confirm);

        View prev = findViewById(R.id.prev);
        if(prev != null)
        {
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }

        View submit = findViewById(R.id.submit);
        if(submit != null)
        {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Functions.closeSoftKeyboard(ChangePassword.this);
                    if(preSubmit())
                    {
                        updatePassword();
                    }
                }
            });
        }
    }//on create

    //short experiment to see if certain warnings can be bypassed
    EditText getElementForId(int id)
    {
        return (EditText) findViewById(id);
    }//get element for id

    boolean preSubmit()
    {

        if(oldpass.getText().length() < 6)
        {
            Toast.makeText(ChangePassword.this, getString(R.string.invalid_old_password), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(newpass.getText().length() < 6)
        {
            Toast.makeText(ChangePassword.this, getString(R.string.invalid_new_password), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(newpass.getText().toString().equals(confirmpass.getText().toString()) == false)
        {
            Toast.makeText(ChangePassword.this, getString(R.string.empty_passwords_dont_match), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }//pre submit

    void updatePassword()
    {
        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.ChangePassword.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String status = Main.getStatusFromSimple(response);
                        if(status.equals("one"))
                        {
                            Toast.makeText(ChangePassword.this, getString(R.string.password_changed), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(ChangePassword.this, getString(R.string.error_changing_password), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ChangePassword.this, getString(R.string.error_while_chaning_password), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("email", Main.userInfo.email);
                params.put("oldpassword", oldpass.getText().toString());
                params.put("newpassword", newpass.getText().toString());

                return params;
            }
        };
        Net.singleton.addRequest(ChangePassword.this, req);
    }//update password
}//ChangePassword
