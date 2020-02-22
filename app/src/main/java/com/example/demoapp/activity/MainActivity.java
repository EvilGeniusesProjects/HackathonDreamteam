package com.example.demoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.demoapp.R;
import com.example.demoapp.api.ShowFragmentListener;
import com.example.demoapp.api.chat.Message;
import com.example.demoapp.fragments.FragmentTime;
import com.example.demoapp.fragments.MailFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity implements ShowFragmentListener, MailFragment.OnButtonClickListener, MailFragment.OnAudioUploaded {

    private static final int mail = 1;
    private static final int status = 2;
    private static final int time = 3;
    private static final int nfc = 4;
    private static final int profile = 5;

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
        MeowBottomNavigation meowBottomNavigation = findViewById(R.id.bottomNav);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(mail, R.drawable.ic_menu));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(status, R.drawable.ic_menu));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(time, R.drawable.ic_menu));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(nfc, R.drawable.ic_menu));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(profile, R.drawable.ic_menu));

        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case mail:

                        switchTo(new MailFragment());
                        break;
                    case status:

                        switchTo(new MailFragment());
                        break;
                    case time:

                        switchTo(new FragmentTime());
                        break;
                    case nfc:

                        switchTo(new MailFragment());
                        break;

                    case profile:
                        switchTo(new MailFragment());
                        break;
                }
                return null;
            }
        });

        //Страница по умолчианию
        meowBottomNavigation.show(time, true);
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
