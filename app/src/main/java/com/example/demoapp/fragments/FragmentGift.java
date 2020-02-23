package com.example.demoapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.api.Gifts.RecyclerViewAdapterGifts;
import com.example.demoapp.api.ShowFragmentListener;

import java.util.ArrayList;

public class FragmentGift extends Fragment implements RecyclerViewAdapterGifts.ItemClickListener {

    ImageView imageViewBack;
    private ShowFragmentListener showFragmentListener;
    RecyclerViewAdapterGifts adapter;
    int str = 228;
    Button button111;
    TextView textView14;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gift, container, false);
        imageViewBack = rootView.findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentListener.showFragment(new FragmentTime());
            }
        });

        button111 = rootView.findViewById(R.id.button111);
        textView14 = rootView.findViewById(R.id.textView14);





        button111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = str - 100;
                textView14.setText("У вас " + str + " ч.");
                Toast.makeText(getActivity(), "Вам добавлен 1 день для выходного", Toast.LENGTH_SHORT).show();
                if(str < 100){
                    button111.setBackgroundResource(R.drawable.button_border_false);
                    button111.setEnabled(false);

                }
            }
        });








        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ShowFragmentListener) {
            showFragmentListener = (ShowFragmentListener) context;
        }
    }
}
