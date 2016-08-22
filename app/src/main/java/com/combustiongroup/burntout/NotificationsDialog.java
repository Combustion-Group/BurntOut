package com.combustiongroup.burntout;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.response.StatusResponse;
import com.combustiongroup.burntout.ui.NotificationAdapter;
import com.combustiongroup.burntout.util.SpinnerAlert;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NotificationsDialog extends AppCompatActivity {

    private static final String TAG = "NotificationsDialog";
    static ViewPager pager;
    ImageView photo;
    View alerts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_dialog);

        alerts = findViewById(R.id.alert_icon);
        assert alerts != null;
        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        pager = (ViewPager) findViewById(R.id.view_pager);
        assert pager != null;

        pager.setAdapter(new NotificationAdapter(NotificationsDialog.this));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                View rootView = pager.findViewWithTag("n" + position);

                View yes = rootView.findViewById(R.id.yes),
                        no = rootView.findViewById(R.id.no),
                        line = rootView.findViewById(R.id.line),
                        dismiss = rootView.findViewById(R.id.dismiss),
                        question = rootView.findViewById(R.id.question);
                assert yes != null && no != null && line != null && dismiss != null && question != null;
                yes.setVisibility(View.INVISIBLE);
                no.setVisibility(View.INVISIBLE);
                line.setVisibility(View.INVISIBLE);
                question.setVisibility(View.INVISIBLE);
                dismiss.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }//on create

    public static void deleteNotification(final int positions) {

        if (positions == -1) {
            return;
        }
        SpinnerAlert.show(pager.getContext());

        BOAPI.service.deleteNotification(BOAPI.gUserNotifications.get(positions).getNotification_id()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Toast.makeText(pager.getContext(), pager.getContext().getResources().getString(R.string.notifiation_deleted), Toast.LENGTH_LONG).show();
                BOAPI.gUserNotifications.remove(positions);
                pager.getAdapter().notifyDataSetChanged();
                if (BOAPI.gUserNotifications.size() == 0) {
                    Main.showAlert = false;
                    Main.dataSetModified = true;
                    NotificationAdapter.activity.finish();
                }
                SpinnerAlert.dismiss(pager.getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(pager.getContext(), pager.getContext().getResources().getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();
                SpinnerAlert.dismiss(pager.getContext());

            }
        });
    }//delete Notification


}//NotificationsDialog
