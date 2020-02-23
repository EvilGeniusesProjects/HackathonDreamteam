package com.example.demoapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.PendingIntent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.api.ShowFragmentListener;
import com.example.demoapp.api.chat.Message;
import com.example.demoapp.fragments.FragmentMail;
import com.example.demoapp.fragments.FragmentNFC;
import com.example.demoapp.fragments.FragmentOne;
import com.example.demoapp.fragments.FragmentProfile;
import com.example.demoapp.fragments.FragmentTime;
import com.example.demoapp.fragments.FragmentTwo;
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

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    boolean statusWork = false;
    private String nameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra(LoginActivity.USER_ID);
        }

        reference = FirebaseDatabase.getInstance().getReference().child("Chat");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);


            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
                if(nameString.equals("time")) {
                Toast.makeText(this, "Статус измёнен", Toast.LENGTH_SHORT).show();

                FragmentTime fragment = new FragmentTime();

                if (!statusWork) {
                    statusWork = true;
                } else {
                    statusWork = false;
                }

                Bundle bundle = new Bundle();
                bundle.putBoolean("key", statusWork);
                fragment.setArguments(bundle);

                showFragment(fragment);
            }

                if(nameString.equals("mail")){

                    Toast.makeText(this, "Подключение к чату", Toast.LENGTH_SHORT).show();

                    FragmentOne fragmentOne = new FragmentOne();
                    FragmentTwo fragmentTwo = new FragmentTwo();

                    if (!statusWork) {
                        statusWork = true;
                        showFragment(fragmentOne);
                    } else {
                        statusWork = false;
                        showFragment(fragmentTwo);
                    }





                }





        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_time);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_nfc:
                    nameString = "nfc";
                    switchTo(new FragmentNFC());
                    break;
                case R.id.nav_chat:
                    switchTo(new FragmentMail());
                    nameString = "mail";
                    break;
                case R.id.nav_time:
                    nameString = "time";


                    FragmentTime fragment = new FragmentTime();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("key", false);
                    fragment.setArguments(bundle);
                    switchTo(fragment);


                    break;
                case R.id.nav_search:
                    nameString = "status";
                    switchTo(new MailFragment());
                    break;
                case R.id.nav_profile:
                    nameString = "profile";
                    switchTo(new FragmentProfile());
                    break;

            }
            return true;
        });
        switchTo(new FragmentTime());
        nameString = "time";
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


    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            if(!statusWork){
                statusWork = true;
            }else{
                statusWork = false;
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }
}
