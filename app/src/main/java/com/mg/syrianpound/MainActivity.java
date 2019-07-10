package com.mg.syrianpound;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mg.syrianpound.adapters.Coinadapter;
import com.mg.syrianpound.adapters.Goldadapter;
import com.mg.syrianpound.api.classes.Api;
import com.mg.syrianpound.api.classes.ApiClient;
import com.mg.syrianpound.api.classes.ApiResponse;
import com.mg.syrianpound.fragments.CoinFragment;
import com.mg.syrianpound.fragments.GoldFragment;
import com.mg.syrianpound.fragments.TransferFragment;
import com.mg.syrianpound.models.Coin;
import com.mg.syrianpound.models.gold;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener , RewardedVideoAdListener {
    private TextView mTextMessage;
    private  RecyclerView myRecyclerView;
    private Coinadapter mAdapter;
    private Goldadapter mAdapter2;
    public ArrayList<Coin> coinList;
    public   ArrayList<gold> goldList;
    ProgressDialog progressDialog;
    TextView textView;
    Toolbar toolbar ;
    SharedPreferences  mPrefs;
    Dialog reConnectDialog;
    Button reConnect;
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;
    RewardedVideoAd mAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog =ProgressDialog.getInstance();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        coinList= new ArrayList<Coin>();
        goldList=new ArrayList<gold>();
        mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
        progressDialog.show(this);

        AdRequest adRequest = new AdRequest.Builder().build();

        MobileAds.initialize(this ,"ca-app-pub-9346728385477859~9335744094");
        mAd=MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        mAd.loadAd("ca-app-pub-9346728385477859/3700274039",
                new AdRequest.Builder().build());

        getData();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest2);



    }
    public void getData() {
        Api apiService = ApiClient.getClient().create(Api.class);
        Call<ApiResponse<List<Coin>>> call = apiService.getCoins(Constants.API_KEY);
        call.enqueue(new Callback<ApiResponse<List<Coin>>>() {
         @Override
         public void onResponse(Call<ApiResponse<List<Coin>>> call, Response<ApiResponse<List<Coin>>> response) {
             if (response.isSuccessful()) {
                 List<Coin> temp = response.body().getData();
                 for (int i = 0; i < temp.size(); i++) {
                     coinList.add(temp.get(i));
                 }
                 SharedPreferences.Editor prefsEditor = mPrefs.edit();
                 Gson gson = new Gson();
                 String json = gson.toJson(coinList);
                 prefsEditor.putString("coinList", json);
                 prefsEditor.commit();
                 getGold();
             }
             else
             {
                 importFromPref();
             }

         }
         @Override
         public void onFailure(Call<ApiResponse<List<Coin>>> call, Throwable t) {
             importFromPref();
             Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
         }
     });

    }
    public void getGold()
    {
        Api apiService = ApiClient.getClient().create(Api.class);
        Call<ApiResponse<List<gold>>> call = apiService.getGold(Constants.API_KEY);

        call.enqueue(new Callback<ApiResponse<List<gold>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<gold>>> call, Response<ApiResponse<List<gold>>> response) {
               if (response.isSuccessful()) {
                   List<gold> temp = response.body().getData();
                   for (int i = 0; i < temp.size(); i++) {
                       goldList.add(temp.get(i));
                   }
                   SharedPreferences.Editor prefsEditor = mPrefs.edit();
                   Gson gson = new Gson();
                   String json = gson.toJson(goldList);
                   prefsEditor.putString("goldList", json);
                   prefsEditor.commit();
                   if (reConnectDialog != null)
                       if (reConnectDialog.isShowing())
                           reConnectDialog.cancel();
                   progressDialog.cancel();
                   GoldFragment fragment = new GoldFragment();
                   loadFragment(fragment);
               }
               else
               {
                   importFromPref();
               }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<gold>>> call, Throwable t) {

                Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<gold> getGoldList() {  return  goldList;
    }

    public List<Coin> getCoinList() { return  coinList;
    }
    private  boolean loadFragment(Fragment fragment)
    {
        if (mAd.isLoaded())
            mAd.show();
        if (fragment  != null )
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fargment_container,fragment).commitNowAllowingStateLoss();
                 return true;
        }
        return false;
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment= null;
        switch (menuItem.getItemId())
        {
            case R.id.navigation_coins:
                fragment = new CoinFragment();

                break;
            case R.id.navigation_gold:
                fragment = new GoldFragment();

                break;
            case R.id.navigation_tranfer:
                fragment = new TransferFragment();
                break;
        }
        return loadFragment(fragment);
    }


    public void  importFromPref()
    {
        if (mPrefs.contains("coinList") && mPrefs.contains("goldList") ) {
            Gson gson = new Gson();
            String json = mPrefs.getString("coinList", "");
            Type type = new TypeToken<List<Coin>>() {
            }.getType();
            coinList = gson.fromJson(json, type);
            gson = new Gson();
            json = mPrefs.getString("goldList", "");
            type = new TypeToken<List<gold>>() {
            }.getType();
            goldList = gson.fromJson(json, type);
            progressDialog.cancel();
            GoldFragment fragment = new GoldFragment();
            loadFragment(fragment);
        }
        else
        {
            progressDialog.cancel();
            if (reConnectDialog == null) {
                reConnectDialog = new Dialog(MainActivity.this, R.style.myDialog);
                reConnectDialog.setContentView(R.layout.dialog_connect);
                reConnectDialog.setCancelable(false);
                reConnectDialog.setCanceledOnTouchOutside(false);
                reConnectDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                reConnectDialog.show();
                reConnect = (Button) reConnectDialog.findViewById(R.id.buttonReconnect);
                reConnect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reConnectDialog.cancel();
                        progressDialog.show(MainActivity.this);
                        getData();
                    }
                });
            }
            else
            {
                if(!reConnectDialog.isShowing())
                {
                    reConnectDialog.show();
                }
            }


        }
    }
    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
