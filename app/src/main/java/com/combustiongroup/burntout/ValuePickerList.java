package com.combustiongroup.burntout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ValuePickerList extends AppCompatActivity {

    int result = Activity.RESULT_CANCELED;
    Intent rdata = new Intent();

    String[] states = new String[]{
            "AK","AL","AR","AZ","CA",
            "CO","CT","DC","DE","FL",
            "GA","GU","HI","IA","ID",
            "IL","IN","KS","KY","LA",
            "MA","MD","ME","MH","MI",
            "MN","MO","MS","MT","NC",
            "ND","NE","NH","NJ","NM",
            "NV","NY","OH","OK","OR",
            "PA","PR","PW","RI","SC",
            "SD","TN","TX","UT","VA",
            "VI","VT","WA","WI","WV",
            "WY"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_picker_list);

        ListView lv = (ListView) findViewById(R.id.list);
        assert lv != null;
        lv.setAdapter(new ArrayAdapter<>(
                ValuePickerList.this,
                R.layout.el_list_item,
                states
        ));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                result = Activity.RESULT_OK;
                rdata.putExtra("choice", states[position]);
                finish();
            }
        });
    }//on create

    @Override
    public void finish() {

        setResult(result, rdata);
        super.finish();
    }//finish
}//ValuePickerList
