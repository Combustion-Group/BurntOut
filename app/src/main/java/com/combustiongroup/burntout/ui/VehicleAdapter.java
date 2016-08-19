package com.combustiongroup.burntout.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.combustiongroup.burntout.Main;
import com.combustiongroup.burntout.ProfileVehicleEditPrompt;
import com.combustiongroup.burntout.R;

import static com.combustiongroup.burntout.network.BOAPI.userVehicles;

/**
 * Created by WarMachine on 8/19/16.
 */

public class VehicleAdapter extends PagerAdapter {
    Activity activity;

    public VehicleAdapter(Activity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return userVehicles.size();
    }//get count

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }//is view from object

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final int n = position;
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View rootView = inflater.inflate(R.layout.pager_vehicle, container, false);

        View edit = rootView.findViewById(R.id.edit);
        assert edit != null;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        /*userInfo.vehicles.remove(n);
                        vehiclePager.getAdapter().notifyDataSetChanged(); */
                Intent menu = new Intent(activity, ProfileVehicleEditPrompt.class);
                menu.putExtra("forItem", n);
                activity.startActivityForResult(menu, Main.IntentEdit);
            }
        });

        TextView plate = (TextView) rootView.findViewById(R.id.plate);
        TextView model = (TextView) rootView.findViewById(R.id.model);
        ImageView image = (ImageView) rootView.findViewById(R.id.vehicle);

        assert plate != null && model != null && image != null;

        plate.setText(userVehicles.get(position).getPlateNumber());
        model.setText(userVehicles.get(position).getCarModel());
        image.setImageResource(userVehicles.get(position).getResource());

        (container).addView(rootView);
        return rootView;
    }//instantiate item

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        (container).removeView((View) object);
    }//destroy item

    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }//get item position
}
