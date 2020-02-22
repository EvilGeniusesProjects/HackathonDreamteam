package com.example.demoapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.api.Gifts.RecyclerViewAdapterGifts;
import com.example.demoapp.api.ShowFragmentListener;

import java.util.ArrayList;

public class FragmentGift extends Fragment implements RecyclerViewAdapterGifts.ItemClickListener {

    ImageView imageViewBack;
    private ShowFragmentListener showFragmentListener;
    RecyclerViewAdapterGifts adapter;

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



            ArrayList<String> animalNames = new ArrayList<>();
            animalNames.add("Horse");
            animalNames.add("Cow");
            animalNames.add("Camel");
            animalNames.add("Sheep");
            animalNames.add("Goat");

            // set up the RecyclerView
            RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new RecyclerViewAdapterGifts(getContext(), animalNames);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);











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
