package com.combustiongroup.burntout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;


import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.response.StatusResponse;
import com.combustiongroup.burntout.util.SpinnerAlert;

import retrofit2.Call;
import retrofit2.Callback;

import static com.combustiongroup.burntout.network.BOAPI.gUserPreferences;

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

        assert signout != null
                && pushEnabledSwitch != null
                && contact != null
                && tos != null
                && privacy != null
//                &&  vehicles != null
                && editInfo != null;

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

        pushEnabledSwitch.setChecked(gUserPreferences.getPush_notifications().equals("1"));
        pushEnabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    gUserPreferences.setPush_notifications("1");
                final String enabled = (isChecked) ? "1" : "0";
                Log.w("#app", "switch: " + enabled);
                SpinnerAlert.show(Settings.this);
                BOAPI.service.pushSwitch(
                        BOAPI.gUserInfo.getEmail(),
                        enabled).enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, retrofit2.Response<StatusResponse> response) {
                        SpinnerAlert.dismiss(Settings.this);

                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        SpinnerAlert.dismiss(Settings.this);
                    }
                });
            }
        });//push notifications switch
    }//on create

    //clear stack & present login screen
    void logout() {
        Intent i = new Intent(Settings.this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }//logout
}//Settings
