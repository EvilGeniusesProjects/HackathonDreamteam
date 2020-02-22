package com.example.demoapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.demoapp.R;
import com.example.demoapp.api.ShowFragmentListener;
import com.example.demoapp.api.chat.Message;
import com.example.demoapp.fragments.FragmentTime;
import com.example.demoapp.fragments.MailFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ShowFragmentListener, MailFragment.OnButtonClickListener, MailFragment.OnAudioUploaded {

    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra(LoginActivity.USER_ID);
        }
        reference = FirebaseDatabase.getInstance().getReference().child("Chat");
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_time);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_nfc:
                    switchTo(new MailFragment());
                    break;
                case R.id.nav_chat:
                    switchTo(new MailFragment());
                    break;
                case R.id.nav_time:
                    switchTo(new FragmentTime());
                    break;
                case R.id.nav_search:
                    switchTo(new MailFragment());
                    break;
                case R.id.nav_profile:
                    switchTo(new MailFragment());
                    break;

            }
            return true;
        });
        switchTo(new FragmentTime());
    }

    private void switchTo(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContent, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void showFragment(Fragment fragment) {
        switchTo(fragment);
    }

    @Override
    public void onButtonClick(MailFragment chatFragment) {
        String text = chatFragment.getText();
        if (text.trim().length() > 0) {
            SimpleDateFormat date = new SimpleDateFormat("HH:mm dd/MM/yy", Locale.getDefault());
            Message m = new Message(text, date.format(new Date()), userID, null);
            reference.push().setValue(m);
            chatFragment.setText("");
        }
    }

    @Override
    public void audioAttachComplete(String audio) {
        SimpleDateFormat date = new SimpleDateFormat("HH:mm dd/MM/yy", Locale.getDefault());
        Message m = new Message("none", date.format(new Date()), userID, audio);
        reference.push().setValue(m);
    }

}
