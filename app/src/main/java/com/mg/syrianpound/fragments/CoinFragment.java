package com.mg.syrianpound.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.mg.syrianpound.MainActivity;
import com.mg.syrianpound.R;
import com.mg.syrianpound.adapters.Coinadapter;
import com.mg.syrianpound.models.Coin;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import java.util.List;

public class CoinFragment extends Fragment {
    private static final String TAG = "CoinFragment";
    List<Coin> coinList;
    private RecyclerView myRecyclerView;
    private Coinadapter mAdapter;
    private RewardedVideoAd mRewardedVideoAd;

    public CoinFragment() {
    }

    @Nullable
    @Override

    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.coins_fragment, container,false);
        Log.d("TSTS","entered");
        coinList= ((MainActivity)getActivity()).getCoinList();
        myRecyclerView =(RecyclerView) view.findViewById(R.id.coinrec);
        mAdapter =new Coinadapter(coinList,getActivity());
        myRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager =new LinearLayoutManager( getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(layoutManager);
        // Prepare the Interstitial Ad
        MobileAds.initialize(getActivity(), "ca-app-pub-9346728385477859~9335744094");
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        //mRewardedVideoAd.setRewardedVideoAdListener((RewardedVideoAdListener) getActivity());
        loadRewardedVideoAd();
        return view;

    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("1ca-app-pub-9346728385477859/3700274039",
                new AdRequest.Builder().build());
    }
}
