package com.example.demoapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.activity.LoginActivity;
import com.example.demoapp.api.chat.Message;
import com.example.demoapp.api.chat.MessageAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.content.Context.VIBRATOR_SERVICE;

public class MailFragment extends Fragment {

    private static final int STANDART_MESSAGE = 2;
    private static final int VOICE_MESSAGE = 3;

    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    private EditText messageText;
    private ImageView sendButton;
    private OnButtonClickListener onButtonClickListener;
    private OnAudioUploaded onAudioUploaded;
    private String userID;
    private View recordView;
    private ConstraintLayout cs;
    private TextView textTime;
    private int currentType = VOICE_MESSAGE;
    private Disposable d;
    private int sec;
    private int min;
    private MediaRecorder mediaRecorder;
    private String tmpDir;
    private String[] audioPermissions = {Manifest.permission.RECORD_AUDIO};
    private String[] writePermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private DatabaseReference reference;
    private ChildEventListener childEventListener;


    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 201;


    public MailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null)
            userID = args.getString(LoginActivity.USER_ID);
        tmpDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Demo/Audio";

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                updateAdapter(message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference = FirebaseDatabase.getInstance().getReference().child("Chat");
    }

    public static MailFragment newInstance(String userID) {

        Bundle args = new Bundle();
        args.putString(LoginActivity.USER_ID, userID);

        MailFragment fragment = new MailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickListener) {
            onButtonClickListener = (OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnButtonClickListener");
        }
        if (context instanceof OnAudioUploaded) {
            onAudioUploaded = (OnAudioUploaded) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAudioUploaded");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mail, container, false);

        recyclerView = v.findViewById(R.id.messageListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessageAdapter(getContext(), new ArrayList<>(), userID, FirebaseStorage.getInstance());
        recyclerView.setAdapter(adapter);
        messageText = v.findViewById(R.id.messageEt);
        cs = v.findViewById(R.id.bottom);
        textTime = cs.findViewById(R.id.textTime);
        recordView = cs.findViewById(R.id.recIndicator);

        sendButton = v.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(view -> onButtonClickListener.onButtonClick(this));
        sendButton.setOnTouchListener((v1, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (currentType == VOICE_MESSAGE)
                        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                            startRecord();
                        else {
                            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), audioPermissions, REQUEST_RECORD_AUDIO_PERMISSION);
                        }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (currentType == VOICE_MESSAGE) {
                        if (event.getX() <= -50) {

                            if (d != null)
                                d.dispose();
                            textTime.setText("");

                            ConstraintSet set = new ConstraintSet();
                            set.clone(cs);

                            set.clear(R.id.recPanel, ConstraintSet.LEFT);

                            TransitionManager.beginDelayedTransition(cs);
                            set.applyTo(cs);
                            releaseRec();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (currentType == VOICE_MESSAGE) {
                        ConstraintSet set = new ConstraintSet();
                        set.clone(cs);

                        set.clear(R.id.recPanel, ConstraintSet.LEFT);

                        TransitionManager.beginDelayedTransition(cs);
                        set.applyTo(cs);
                        if (d != null)
                            d.dispose();
                        textTime.setText("");
                        if (mediaRecorder != null)
                            mediaRecorder.stop();

                        try {
                            FirebaseStorage fs = FirebaseStorage.getInstance();
                            StorageReference sr = fs.getReference();
                            FileInputStream f = new FileInputStream(tmpDir + "/rec.3gpp");
                            String fName = String.valueOf(System.currentTimeMillis());
                            sr.child("Audio/").child(fName).putStream(f).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    onAudioUploaded.audioAttachComplete(fName);
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    sendButton.performClick();
                    break;
                default:
                    break;
            }
            return true;
        });
        messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    setMessageMode(STANDART_MESSAGE);
                } else {
                    setMessageMode(VOICE_MESSAGE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return v;
    }

    private void startRecord() {
        try {
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                File f = new File(tmpDir);
                boolean b = true;
                if (!f.exists())
                    b = f.mkdirs();
                if (!b) {
                    Toast.makeText(getContext(), "Ошибка создания файлов", Toast.LENGTH_SHORT).show();
                }

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile(tmpDir + "/rec.3gpp");
                mediaRecorder.prepare();
            } else {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), audioPermissions, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ((Vibrator) Objects.requireNonNull(Objects.requireNonNull(getContext()).getSystemService(VIBRATOR_SERVICE))).vibrate(150);

        ConstraintSet set = new ConstraintSet();
        set.clone(cs);

        set.connect(R.id.recPanel, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);

        TransitionManager.beginDelayedTransition(cs);
        set.applyTo(cs);
        recordView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rec_anim));
        sec = 0;
        min = 0;
        Single<String> single = Single.create(s -> {
            sec++;
            if (sec >= 60) {
                min++;
                sec %= 60;
            }
            String secStr;
            if (sec < 10)
                secStr = String.format(Locale.getDefault(), "0%d", sec);
            else
                secStr = String.valueOf(sec);
            String minStr;
            if (min < 10)
                minStr = String.format(Locale.getDefault(), "0%d", min);
            else
                minStr = String.valueOf(min);
            s.onSuccess(String.format(Locale.getDefault(), "%s:%s", minStr, secStr));
        });

        try {
            mediaRecorder.start();
            d = single
                    .delay(1, TimeUnit.SECONDS)
                    .repeat()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> textTime.setText(String.valueOf(s)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseRec() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        File file = new File(tmpDir + "/rec.3gpp");
        boolean b = true;
        if (file.exists())
            b = file.delete();
        if (!b) {
            Toast.makeText(getContext(), "Файл не удален", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMessageMode(int mode) {
        switch (mode) {
            case STANDART_MESSAGE:
                currentType = STANDART_MESSAGE;
                sendButton.setImageResource(R.drawable.send);
                break;
            case VOICE_MESSAGE:
                currentType = VOICE_MESSAGE;
                sendButton.setImageResource(R.drawable.micro);
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onButtonClickListener = null;
        releaseRec();
        if (childEventListener != null) {
            reference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    public void updateAdapter(Message m) {
        adapter.add(m);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    public String getText() {
        return Objects.requireNonNull(messageText.getText()).toString();
    }

    public void setText(String text) {
        messageText.setText(text);
    }

    public interface OnButtonClickListener {
        void onButtonClick(MailFragment fragment);
    }

    public interface OnAudioUploaded {
        void audioAttachComplete(String audio);
    }
}
