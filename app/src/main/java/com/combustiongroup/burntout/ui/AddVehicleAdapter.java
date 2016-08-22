package com.combustiongroup.burntout.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.combustiongroup.burntout.Main;
import com.combustiongroup.burntout.R;

import static com.combustiongroup.burntout.network.BOAPI.gUserVehicles;

/**
 * Created by WarMachine on 8/19/16.
 */

public class AddVehicleAdapter extends PagerAdapter {
    Activity activity;

    int[] images = new int[]{
            R.drawable.image_car_front,
            R.drawable.image_bike_front,
            R.drawable.image_truck_front,
            R.drawable.image_bus_front};

    public AddVehicleAdapter(Activity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return images.length;
    }//get count

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }//is view from object

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String[] names = new String[]{activity.getResources().getString(R.string.car),
                activity.getResources().getString(R.string.bike), activity.getResources().getString(R.string.truck),
                activity.getResources().getString(R.string.bus)};
        LayoutInflater inflater = LayoutInflater.from(container.getContext());

        View rootView = inflater.inflate(R.layout.pager_image_label_bottom, container, false);

        TextView name = (TextView) rootView.findViewById(R.id.name);
        ImageView image = (ImageView) rootView.findViewById(R.id.image);

        assert name != null && image != null;

        name.setText(names[position]);
        image.setImageResource(images[position]);

        ((ViewPager) container).addView(rootView);
        return rootView;
    }//instantiate item

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView((View) object);
    }//destroy item

    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }//get item position
}
