package com.combustiongroup.burntout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ProfileVehicleEditPrompt extends AppCompatActivity {

    int result = Activity.RESULT_CANCELED;
    Intent data = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_vehicle_edit_prompt);

        View edit = findViewById(R.id.edit);
        View delete = findViewById(R.id.delete);
        View cancel = findViewById(R.id.cancel);

        assert edit != null && delete != null && cancel != null;

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //edit
                data.putExtra("action", "edit");
                result = Activity.RESULT_OK;
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //delete
                data.putExtra("action", "delete");
                result = Activity.RESULT_OK;
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }//on create

    @Override
    public void finish() {

        //pass along target
        data.putExtra("forItem", getIntent().getIntExtra("forItem", 0));
        setResult(result, data);

        super.finish();
    }//finish
}//ProfileVehicleEditPrompt
