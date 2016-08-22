package com.combustiongroup.burntout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.response.StatusResponse;
import com.combustiongroup.burntout.util.Functions;
import com.combustiongroup.burntout.util.SpinnerAlert;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    @BindView(R.id.password_old)
    EditText oldpass;
    @BindView(R.id.password)
    EditText newpass;
    @BindView(R.id.password_confirm)
    EditText confirmpass;
    @BindView(R.id.submit)
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);


        View prev = findViewById(R.id.prev);
        if (prev != null) {
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }

        View submit = findViewById(R.id.submit);
        if (submit != null) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Functions.closeSoftKeyboard(ChangePassword.this);
                    if (preSubmit()) {
                        updatePassword();
                    }
                }
            });
        }
    }//on create

    //short experiment to see if certain warnings can be bypassed
    EditText getElementForId(int id) {
        return (EditText) findViewById(id);
    }//get element for id

    boolean preSubmit() {

        if (oldpass.getText().length() < 6) {
            Toast.makeText(ChangePassword.this, getString(R.string.invalid_old_password), Toast.LENGTH_LONG).show();
            return false;
        } else if (newpass.getText().length() < 6) {
            Toast.makeText(ChangePassword.this, getString(R.string.invalid_new_password), Toast.LENGTH_LONG).show();
            return false;
        } else if (newpass.getText().toString().equals(confirmpass.getText().toString()) == false) {
            Toast.makeText(ChangePassword.this, getString(R.string.empty_passwords_dont_match), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }//pre submit

    void updatePassword() {
        SpinnerAlert.show(ChangePassword.this);

        BOAPI.service.changePassword(
                BOAPI.gUserInfo.getEmail(),
                oldpass.getText().toString(),
                newpass.getText().toString()).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.body().getStatus().equals("one")) {
                    Toast.makeText(ChangePassword.this, getString(R.string.password_changed), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ChangePassword.this, getString(R.string.error_changing_password), Toast.LENGTH_LONG).show();
                }
                SpinnerAlert.dismiss(ChangePassword.this);
                finish();

            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                SpinnerAlert.dismiss(ChangePassword.this);
                Toast.makeText(ChangePassword.this, getString(R.string.error_while_chaning_password), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                finish();

            }
        });


    }//update password
}//ChangePassword
