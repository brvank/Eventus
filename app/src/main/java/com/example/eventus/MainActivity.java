package com.example.eventus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventus.DashBoard.Dashboard;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    Button btnLogin;
    EditText etEmail, etPassword;
    TextView tvForgot, tvGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing and setting onclicklisteners on views
        init();
    }

    private void init(){
        //Initializing
        btnLogin    = findViewById(R.id.btn_login_main);
        etEmail     = findViewById(R.id.et_email_main);
        etPassword  = findViewById(R.id.et_password_main);
        tvForgot    = findViewById(R.id.tv_forgot_main);
        tvGuest     = findViewById(R.id.tv_guest_main);

        //setting onclicklistener
        btnLogin.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
        tvGuest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_main:
                login(v);
                break;
            case R.id.tv_forgot_main:
                forgotPassword(v);
                break;
            case R.id.tv_guest_main:
                guest(v);
                break;
        }
    }

    private void login(View v) {
        Snackbar.make(v,"use guest",Snackbar.LENGTH_LONG).show();
    }

    private void forgotPassword(View v) {
        Snackbar.make(v,"use guest",Snackbar.LENGTH_LONG).show();
    }

    private void guest(View v) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}