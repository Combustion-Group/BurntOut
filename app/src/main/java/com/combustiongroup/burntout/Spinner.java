package com.combustiongroup.burntout;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Spinner extends AppCompatActivity {

    public static Activity refAct;
    public static boolean open = false;
    View spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        refAct = Spinner.this;

        setFinishOnTouchOutside(false);

        spinner = findViewById(R.id.icon);
        setAnimation();
    }//on create

    @Override
    public void onBackPressed() {
        //do nothing
    }//on back pressed

    public void setAnimation()
    {

        //rare multiple spinner bug
        if(Spinner.this != refAct)
        {
            finish();
            return;
        }

        if(spinner != null)
        {
            final Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.spin);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    setAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            spinner.setAnimation(anim);
            anim.start();
        }
    }//set animation

    //add delay to prevent certain bugs
    public static void close()
    {

        CountDownTimer cdt = new CountDownTimer(300, 300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if(refAct != null)
                {
                    refAct.finish();
                }
            }
        }.start();
    }//close
}//Spinner
