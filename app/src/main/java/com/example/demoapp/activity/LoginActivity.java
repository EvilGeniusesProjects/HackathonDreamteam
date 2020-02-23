package com.example.demoapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.demoapp.R;
import com.example.demoapp.fragments.LoginFragment;
import com.example.demoapp.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnUserLoginListener, RegisterFragment.ChangeToLoginListener, LoginFragment.ChangeToRegisterListener {

    private FragmentManager fragmentManager;
    public static final String USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, new LoginFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void login(String userId) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(USER_ID, userId);
        startActivity(mainIntent);
    }

    @Override
    public void changeToLogin() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, new LoginFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void changeToRegister() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, new RegisterFragment());
        fragmentTransaction.commit();
    }
}
