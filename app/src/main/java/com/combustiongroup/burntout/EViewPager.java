package com.combustiongroup.burntout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Candy on 6/15/16.
 */
//allow scrollable elements inside view pager
class EViewPager extends ViewPager
{

    public EViewPager(Context c, AttributeSet attrs)
    {
        super(c, attrs);
    }//Constructor

    public EViewPager(Context c)
    {
        super(c);
    }//Constructor

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return false;
    }//on itnercept touch events

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return false;
    }//on touch event
}//EViewPager