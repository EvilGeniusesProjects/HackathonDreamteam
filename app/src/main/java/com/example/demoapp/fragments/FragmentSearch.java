package com.example.demoapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.api.ShowFragmentListener;
import com.example.demoapp.api.search.DummyContent;
import com.example.demoapp.api.search.DummyContent.Persons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FragmentSearch extends Fragment {

    private ShowFragmentListener mListener;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        Context context = view.getContext();
        searchView = (SearchView) view.findViewById(R.id.searchView1);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        MyItemRecyclerViewAdapter myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(DummyContent.ITEMS, mListener);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Persons> list = new ArrayList<>();
                String name;

                for (Persons persons : DummyContent.ITEMS) {
                    name = persons.name.toLowerCase() + " " + persons.sureName.toLowerCase();
                    if (name.contains(query.toLowerCase())) {
                        list.add(persons);
                    }
                }
                myItemRecyclerViewAdapter.updateAdapter(list);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(v -> myItemRecyclerViewAdapter.updateAdapter(DummyContent.ITEMS));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowFragmentListener) {
            mListener = (ShowFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
