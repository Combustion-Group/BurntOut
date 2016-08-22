package com.combustiongroup.burntout.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Functions
{

    public static void closeSoftKeyboard(Activity a)
    {

        View view = a.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }//close soft keyboard

    public static Point getScreenSize(Activity a)
    {
        Display display = a.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }//get screen size
}//Functions
