package com.combustiongroup.burntout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.response.SignUpResponse;
import com.combustiongroup.burntout.util.SpinnerAlert;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {


    @BindView(R.id.first_name)
    EditText firstName;
    @BindView(R.id.last_name)
    EditText lastName;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.password_confirm)
    EditText passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        View prev = findViewById(R.id.prev);
        assert prev != null;
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        View submit = findViewById(R.id.submit);
        assert submit != null;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFields()) {
                    register();
                }
            }
        });


    }//on create

    boolean checkFields() {
        if (!(firstName.getText().length() > 0)) {
            Toast.makeText(Signup.this, getString(R.string.empty_firstname), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!(lastName.getText().length() > 0)) {
            Toast.makeText(Signup.this, getString(R.string.empty_lastname), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!(email.getText().length() > 0)) {
            Toast.makeText(Signup.this, getString(R.string.error_empty_email), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!(password.getText().length() > 5)) {
            Toast.makeText(Signup.this, getString(R.string.empty_password), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!(password.getText().toString().equals(passwordConfirm.getText().toString()))) {
            Toast.makeText(Signup.this, getString(R.string.empty_passwords_dont_match), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }//check fields

    void register() {
        SpinnerAlert.show(Signup.this);

        BOAPI.service.signUp(
                firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString(),
                password.getText().toString()).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.body().getLoginstatus().equals("one")) {
                    Toast.makeText(Signup.this, getString(R.string.sign_up_successful), Toast.LENGTH_LONG).show();
                    finish();
                } else if (response.body().getLoginstatus().equals("three")) {
                    Toast.makeText(Signup.this, getString(R.string.email_has_been_used), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Signup.this, getString(R.string.error_registrating_account) + "\nError: " + response.body().getLoginstatus(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                t.printStackTrace();
                SpinnerAlert.dismiss(Signup.this);
                Toast.makeText(Signup.this, getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();

            }
        });

    }//register

}//Signup
