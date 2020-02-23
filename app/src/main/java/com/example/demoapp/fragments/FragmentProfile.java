package com.example.demoapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.demoapp.R;
import com.example.demoapp.api.ShowFragmentListener;

public class FragmentProfile extends Fragment implements View.OnClickListener{

    Button change_user2;
    Button change_experience;
    Button exit;
    private ShowFragmentListener showFragmentListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        change_user2 = rootView.findViewById(R.id.change_user2);
        change_experience = rootView.findViewById(R.id.change_experience);
        exit = rootView.findViewById(R.id.exit);


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finishAffinity();

            }
        });


        change_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentListener.showFragment(new FragmentExperience());

            }
        });


        return rootView;
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ShowFragmentListener) {
            showFragmentListener = (ShowFragmentListener) context;
        }
    }
}
