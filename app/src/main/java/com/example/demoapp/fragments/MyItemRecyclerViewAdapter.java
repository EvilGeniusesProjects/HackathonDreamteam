package com.example.demoapp.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.api.ShowFragmentListener;
import com.example.demoapp.api.search.DummyContent.Persons;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<Persons> mValues = new ArrayList<>();
    private final ShowFragmentListener mListener;

    public MyItemRecyclerViewAdapter(List<Persons> items, ShowFragmentListener listener) {
        mValues.addAll(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.imageView.setImageResource(mValues.get(position).image);
        holder.name.setText(mValues.get(position).name + " " + mValues.get(position).sureName);
        switch (mValues.get(position).status){
            case 1:
                holder.statusInWork.setBackgroundResource(R.drawable.rounded_rect_green);
                break;
            case 2:
                holder.statusInKitchen.setBackgroundResource(R.color.orange);
                break;
            case 3:
                holder.statusInHome.setBackgroundResource(R.drawable.rounded_rect_red);
                break;
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
//                    mListener.showFragment(holder.mItem);
                }
            }
        });
    }

    public void updateAdapter(List<Persons> list){
        mValues.clear();
        mValues.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircleImageView imageView;
        public final TextView name;
        public final TextView statusInWork;
        public final TextView statusInKitchen;
        public final TextView statusInHome;
        public Persons mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.name);
            imageView = (CircleImageView) view.findViewById(R.id.image);

            statusInWork = (TextView) view.findViewById(R.id.statusInWork);
            statusInKitchen = (TextView) view.findViewById(R.id.statusInKitchen);
            statusInHome = (TextView) view.findViewById(R.id.statusInHome);
        }

    }
}
