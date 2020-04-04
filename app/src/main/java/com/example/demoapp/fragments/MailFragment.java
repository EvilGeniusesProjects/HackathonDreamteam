package com.example.demoapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.api.chat.ClassChatMessage;
import com.example.demoapp.api.chat.MessageAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MailFragment extends Fragment implements View.OnClickListener {

    private EditText editTextMessage;
    private MessageAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private String chatId;

    private static final String CHAT_ID = "chat_id";

    private MailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mail, container, false);
        floatingActionButton = v.findViewById(R.id.floatingActionButton);
        editTextMessage = v.findViewById(R.id.editTextMessage);
        floatingActionButton.setOnClickListener(this);
        recyclerView = v.findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessageAdapter(getContext(), new ArrayList<>(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView.setAdapter(adapter);
        return v;
    }

    public static final MailFragment getInstance(String chatId) {
        MailFragment mailFragment = new MailFragment();
        Bundle b = new Bundle();
        b.putString(CHAT_ID, chatId);
        mailFragment.setArguments(b);
        return mailFragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle arguments = getArguments();
        if (arguments != null) {
            chatId = arguments.getString(CHAT_ID);
        }

        floatingActionButton.setOnClickListener(this);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ClassChatMessage message = dataSnapshot.getValue(ClassChatMessage.class);
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(chatId);
        reference.addChildEventListener(childEventListener);

    }

    public void updateAdapter(ClassChatMessage m) {
        adapter.add(m);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onClick(View v) {
        if (editTextMessage.length() != 0) {
            FirebaseDatabase.getInstance().getReference(chatId).push().setValue(new ClassChatMessage(editTextMessage.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid()));
        }
        editTextMessage.setText("");
    }

}
