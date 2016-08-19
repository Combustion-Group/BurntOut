package com.combustiongroup.burntout.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import com.combustiongroup.burntout.R;

/**
 * Created by WarMachine on 8/19/16.
 */

public class SpinnerAlert {


    private static boolean showing;
    private static Dialog dialog;
    private static CountDownTimer timer;

    /**
     * Shows spinner that blocks the UI. Use for network calls. Use {@link #dismiss(Context) dismiss} method to finish it.
     *
     * @param context
     */
    public static void show(Context context) {

        if (timer != null) {
            timer.cancel();
        }

        if (showing) {
            return;
        }

        View view = View.inflate(context, R.layout.dialog_spinner_alert, null);
        View spinner = view.findViewById(R.id.spinner);

        try {

            spinner.startAnimation(AnimationUtils.loadAnimation(context, R.anim.spin));

            dialog = new AlertDialog.Builder(context).setView(view).setCancelable(false).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.show();

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int px = Math.round(100 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            dialog.getWindow().setLayout(px, px);
            showing = true;
        } catch (Exception e) {
            Log.e("spinner-alert", "SpinnerAlert failed.");
        }
    }

    /**
     * Dismisses the spinner
     *
     * @param context
     */
    public static void dismiss(Context context) {

        timer = new CountDownTimer(250, 250) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                showing = false;
                timer = null;
            }
        };
        timer.start();
    }
}
