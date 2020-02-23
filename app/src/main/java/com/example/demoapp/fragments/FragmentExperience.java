package com.example.demoapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.demoapp.R;
import com.example.demoapp.api.ShowFragmentListener;

public class FragmentExperience extends Fragment  {


    Button button0;
    Button button1;
    Button button21;
    Button button22;
    Button button3;
    ImageView imageBack;
    private ShowFragmentListener showFragmentListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_experience, container, false);

        button0 = rootView.findViewById(R.id.button0);
        button1 = rootView.findViewById(R.id.button1);
        button21 = rootView.findViewById(R.id.button21);
        button22 = rootView.findViewById(R.id.button22);
        button3 = rootView.findViewById(R.id.button3);
        imageBack = rootView.findViewById(R.id.imageViewBack);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button0.setBackgroundResource(R.drawable.button_border_true);
                showFragmentListener.showFragment(new FragmentProfile());
            }
        });


        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button0.setBackgroundResource(R.drawable.button_border_true);
                Toast.makeText(getActivity(), "Заявка отправлена", Toast.LENGTH_SHORT).show();
                button0.setEnabled(false);
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setBackgroundResource(R.drawable.button_border_true);
                Toast.makeText(getActivity(), "Заявка отправлена", Toast.LENGTH_SHORT).show();
                button1.setEnabled(false);
            }
        });

        button21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button21.setEnabled(false);
                button22.setEnabled(false);
                button22.setBackgroundResource(R.drawable.button_border_false);
                button21.setBackgroundResource(R.drawable.button_border_true);
                Toast.makeText(getActivity(), "Заявка отправлена", Toast.LENGTH_SHORT).show();
            }
        });

        button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button22.setEnabled(false);
                button21.setEnabled(false);
                button21.setBackgroundResource(R.drawable.button_border_false);
                button22.setBackgroundResource(R.drawable.button_border_true);
                Toast.makeText(getActivity(), "Заявка отправлена", Toast.LENGTH_SHORT).show();
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button3.setEnabled(false);
                button3.setBackgroundResource(R.drawable.button_border_true);
                Toast.makeText(getActivity(), "Заявка отправлена", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ShowFragmentListener) {
            showFragmentListener = (ShowFragmentListener) context;
        }
    }
}


































