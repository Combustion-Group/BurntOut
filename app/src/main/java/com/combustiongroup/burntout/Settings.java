package com.combustiongroup.burntout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        View signout = findViewById(R.id.signout);
        Switch pushEnabledSwitch = (Switch) findViewById(R.id.setting_push_notification);
        View contact = findViewById(R.id.contact);
        View tos = findViewById(R.id.tos);
        View privacy = findViewById(R.id.privacy);
//        View vehicles = findViewById(R.id.vehicles);
        View editInfo = findViewById(R.id.info);

        assert      signout != null
                &&  pushEnabledSwitch != null
                &&  contact != null
                &&  tos != null
                &&  privacy != null
//                &&  vehicles != null
                &&  editInfo != null;

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });//sign out button

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@burntoutapp.com"});
                email.setType("plain/text");
                startActivity(email);
            }
        });

        tos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.w("#app", "VIEW TOS");

                Uri uri = Uri.parse("http://www.burntoutapp.com/terms");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.w("#app", "VIEW PRIVACY");

                Uri uri = Uri.parse("http://www.burntoutapp.com/privacy");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

//        vehicles.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(Settings.this, SettingsEditVehicles.class);
//                startActivity(i);
//            }
//        });

        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Settings.this, SettingsProfileEdit.class);
                startActivity(i);
            }
        });

        pushEnabledSwitch.setChecked(Main.userInfo.pushEnabled);
        pushEnabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Main.userInfo.pushEnabled = isChecked;
                final String enabled = (isChecked) ? "1" : "0";
                Log.w("#app", "switch: "+enabled);
                StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.EditPreferences.value,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.w("#app", response);
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

                        Map<String, String> params = new HashMap<String, String>();

                        params.put("email", Main.userInfo.email);
                        params.put("push_notifications", enabled);

                        return params;
                    }
                };
                Net.singleton.addRequest(Settings.this, req);
            }
        });//push notifications switch
    }//on create

    //clear stack & present login screen
    void logout()
    {
        Intent i = new Intent(Settings.this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }//logout
}//Settings
