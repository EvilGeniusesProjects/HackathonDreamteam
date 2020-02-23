package com.example.demoapp.api.chat;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<ClassChatMessage> messages;
    private static final int MY_MESSAGE = 0;
    private static final int OTHER_MESSAGE = 2;
    private String currentUserID;
    private Context context;

    public MessageAdapter(Context context, ArrayList<ClassChatMessage> messages, String currentUserID) {
        this.messages = messages;
        this.context = context;
        this.currentUserID = currentUserID;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {

            case OTHER_MESSAGE:
                v = LayoutInflater.from(context).inflate(R.layout.my_message, parent, false);
                break;
            case MY_MESSAGE:
                v = LayoutInflater.from(context).inflate(R.layout.other_message, parent, false);
                break;
        }
        return new MessageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        ClassChatMessage currMessage = messages.get(position);
        holder.message_text.setText(currMessage.getMessageText());
        holder.message_time.setText(DateFormat.format("HH:mm dd/MM/yy", currMessage.getMessageTime()));
    }

    @Override
    public int getItemViewType(int position) {
        String uID = messages.get(position).getUid();
        if (uID.equals(currentUserID)) return MY_MESSAGE;
        return OTHER_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void add(ClassChatMessage m) {
        messages.add(m);
    }

    static class MessageHolder extends RecyclerView.ViewHolder {

        TextView message_text;
        TextView message_time;


        MessageHolder(View itemView) {
            super(itemView);
            message_text = itemView.findViewById(R.id.message_text);
            message_time = itemView.findViewById(R.id.message_time);
        }
    }
}
