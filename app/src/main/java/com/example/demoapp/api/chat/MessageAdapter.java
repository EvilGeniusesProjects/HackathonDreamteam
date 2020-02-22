package com.example.demoapp.api.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private static final int SRC_CACHE = 0;
    private static final int SRC_EXTERNAL_STRG = 1;

    private ArrayList<Message> messages;
    private Context context;
    private String currentUserID;
    private static final int MY_MESSAGE = 0;
    private static final int MY_MESSAGE_WITH_1_PHOTO = 1;
    private static final int OTHER_MESSAGE = 2;
    private static final int OTHER_MESSAGE_WITH_1_PHOTO = 3;
    private static final int OTHER_AUDIO_MESSAGE = 4;
    private static final int MY_AUDIO_MESSAGE = 5;
    private FirebaseStorage firebaseStorage;
    private MediaPlayer mp;
    private boolean isPlaying;

    public MessageAdapter(Context context, ArrayList<Message> messages, String currentUserID, FirebaseStorage firebaseStorage) {
        this.context = context;
        this.messages = messages;
        this.currentUserID = currentUserID;
        this.firebaseStorage = firebaseStorage;

    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case OTHER_MESSAGE:
                v = LayoutInflater.from(context).inflate(R.layout.other_message_item, parent, false);
                break;
            case MY_MESSAGE:
                v = LayoutInflater.from(context).inflate(R.layout.my_message_item, parent, false);
                break;
            case MY_AUDIO_MESSAGE:
                v = LayoutInflater.from(context).inflate(R.layout.audio_my_message_item, parent, false);
                break;
            case OTHER_AUDIO_MESSAGE:
                v = LayoutInflater.from(context).inflate(R.layout.audio_other_message_item, parent, false);
                break;
        }
        return new MessageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message currMessage = messages.get(position);

        String uId = currMessage.getUserImg();
        File f = new File(context.getCacheDir() + "/" + uId);
        if (!f.exists())
            firebaseStorage.getReference("Photos/" + uId + ".jpg").getDownloadUrl().addOnSuccessListener(uri
                    -> downloadUserPhoto(uri.toString(), uId, SRC_CACHE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(b -> holder.circleImageView.setImageBitmap(b), Throwable::printStackTrace));
        else
            holder.circleImageView.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));

        String text = currMessage.getText();
        if (!text.equals("none"))
            holder.messageTextView.setText(currMessage.getText());
        holder.date.setText(currMessage.getDate());
        String audio = currMessage.getAudio();
        if (!audio.equals("none")) {
            holder.playBtn.setOnClickListener(v -> {
                if (!isPlaying) {
                    isPlaying = true;
                    mp = new MediaPlayer();
                    holder.playBtn.setImageResource(R.drawable.pause);
                    firebaseStorage.getReference()
                            .child("Audio")
                            .child(audio)
                            .getDownloadUrl().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            try {
                                if (!mp.isPlaying()) {
                                    mp.setDataSource(context, task.getResult());
                                    mp.prepareAsync();
                                    mp.setOnPreparedListener(MediaPlayer::start);
                                    mp.setOnCompletionListener(mp -> {
                                        holder.playBtn.setImageResource(R.drawable.play);
                                        mp.release();
                                        isPlaying = false;
                                    });
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        String uID = messages.get(position).getUserImg();
        String audio = messages.get(position).getAudio();
        if (uID.equals(currentUserID)) {
            if (!audio.equals("none")) return MY_AUDIO_MESSAGE;
            else return MY_MESSAGE;
        }
        if (!audio.equals("none"))
            return OTHER_AUDIO_MESSAGE;
        return OTHER_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void add(Message m) {
        messages.add(m);
    }


    private Single<Bitmap> downloadUserPhoto(String url, String id, int src) {
        return Single.create(s -> {
            Bitmap bitmap;
            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

                File f;
                FileOutputStream fo;
                try {
                    if (src == SRC_CACHE)
                        f = new File(context.getCacheDir().getAbsolutePath());
                    else
                        f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/UProject/photos");
                    boolean b = true;
                    if (!f.exists())
                        b = f.mkdirs();
                    if (!b)
                        Toast.makeText(context, "Ошибка создания директорий", Toast.LENGTH_SHORT).show();
                    f = new File(f, id);
                    fo = new FileOutputStream(Objects.requireNonNull(f));
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    s.onError(e);
                }
                s.onSuccess(bitmap);

            } catch (IOException e) {
                s.onError(e);
            }
        });
    }

    static class MessageHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        EditText messageTextView;
        TextView date;
        ImageView imageView;

        ImageView playBtn;

        MessageHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.userPhoto);
            messageTextView = itemView.findViewById(R.id.userMessage);
            date = itemView.findViewById(R.id.userMessageDate);
            imageView = itemView.findViewById(R.id.image);

            playBtn = itemView.findViewById(R.id.btn_play);
        }
    }
}
