package com.mg.syrianpound.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mg.syrianpound.MainActivity;
import com.mg.syrianpound.R;
import com.mg.syrianpound.adapters.Goldadapter;
import com.mg.syrianpound.models.gold;

import java.util.List;

public class GoldFragment extends Fragment {
    List<gold> goldList;
    private RecyclerView myRecyclerView;
    private Goldadapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.gold_fragment, null);
        goldList= ((MainActivity)getActivity()).getGoldList();
        myRecyclerView =(RecyclerView) view.findViewById(R.id.goldrec);
        mAdapter =new Goldadapter(goldList,getActivity());
        myRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager =new LinearLayoutManager( getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(layoutManager);

        return view;

    }
}
