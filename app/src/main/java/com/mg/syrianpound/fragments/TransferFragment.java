package com.mg.syrianpound.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mg.syrianpound.MainActivity;
import com.mg.syrianpound.R;
import com.mg.syrianpound.models.Coin;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TransferFragment  extends Fragment {
    List<Coin> coinList;
    TextView fromCoin , toCoin ,sellRes ,buyRes;
    EditText input;
    int from ,to ;
    ImageView imageView;
    Animation animation;
    int type=0;
    TextView toText,fromText;
    Button toButton ,fromButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.transfer_fragment, null);
        imageView = (ImageView) view.findViewById(R.id.convert_button);
        input =(EditText)view.findViewById(R.id.button_input) ;
        sellRes = (TextView) view.findViewById(R.id.sellRes);
        buyRes = (TextView) view.findViewById(R.id.BuyRes);
        toText = (TextView) view.findViewById(R.id.to_text);
        fromText = (TextView) view.findViewById(R.id.from_text);
        toButton = (Button) view.findViewById(R.id.to_button);
        fromButton = (Button) view.findViewById(R.id.from_button);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValidation() ) {
                    animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                    imageView.startAnimation(animation);
                     cal();
                }
                else
                {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.inputEmpty), Toast.LENGTH_SHORT).show();
                }
            }
        });
        coinList= ((MainActivity)getActivity()).getCoinList();
        initFacebookLogin(view);
        from =-1;
        to = -1;

        setFromButton(coinList);
        setToButton(coinList);


        return view;
    }


    public  void  setFromButton(List<Coin> coinList)
    {
        String[] coins = new String[coinList.size()+1];
        coins[0] =getResources().getString(R.string.sp);
        for (int i=0;i<coinList.size();i++)
        {
            coins[i+1] =coinList.get(i).getCoin_name();
        }
        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from =0;
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setSingleChoiceItems(coins, 0, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        from = which;
                    }

                });
                adb.setNegativeButton(getActivity().getResources().getString(R.string.cancel), null);

                adb.setPositiveButton(getActivity().getResources().getString(R.string.select), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (from != 0) {
                            fromText.setText(coinList.get(from-1).getCoin_name());

                        }
                        else {
                            fromText.setText(getActivity().getResources().getString(R.string.sp));
                        }

                    }
                });
                adb.setTitle(getActivity().getResources().getString(R.string.select_coins));
                adb.show();
            }
        });
    }
    public  void  setToButton(List<Coin> coinList)
    {
        String[] coins = new String[coinList.size()+1];
        coins[0] =getResources().getString(R.string.sp);
        for (int i=0;i<coinList.size();i++)
        {
            coins[i+1] =coinList.get(i).getCoin_name();
        }
        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to =0;
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setSingleChoiceItems(coins, 0, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        to = which;
                    }

                });
                adb.setNegativeButton(getActivity().getResources().getString(R.string.cancel), null);

                adb.setPositiveButton(getActivity().getResources().getString(R.string.select), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (to != 0) {
                            toText.setText(coinList.get(to-1).getCoin_name());

                        }
                        else {
                            toText.setText(getActivity().getResources().getString(R.string.sp));
                        }

                    }
                });
                adb.setTitle(getActivity().getResources().getString(R.string.select_coins));
                adb.show();
            }
        });
    }
    private boolean isInputValidation() {
        boolean check = true;
        String message="";
        if (input.getText().toString().isEmpty()){
            check =false;
        }
        if (from==-1){

            check =false;
        }
        if (to==-1){

            check =false;
        }

        return check;

    }
    public  void cal()
    {
          double x=0;
          String fromText= input.getText().toString();
          x= Double.parseDouble(fromText);

           double res =0;
           double res2 =0;
           int tempFrom;
           int tempTO;
           tempFrom = from;
           tempTO =to;

               if (from == 0) {
                   if (to == 0) {
                       res = x;
                       res2 =x;
                   } else {
                       res = x / coinList.get(to - 1).getLog().get(0).getBuy();
                       res2 = x / coinList.get(to - 1).getLog().get(0).getSell();
                   }
               } else {
                   if (to == 0) {
                       res = x * coinList.get(from - 1).getLog().get(0).getBuy();
                       res2 = x * coinList.get(from - 1).getLog().get(0).getSell();
                   } else {
                       res = x * coinList.get(from - 1).getLog().get(0).getBuy();
                       res = res / coinList.get(to - 1).getLog().get(0).getBuy();

                       res2 = x * coinList.get(from - 1).getLog().get(0).getSell();
                       res2 = res2 / coinList.get(to - 1).getLog().get(0).getSell();
                   }
               }

           res = round(res,6);
         NumberFormat nf = NumberFormat.getNumberInstance(Locale.CANADA);
         nf.setMaximumFractionDigits(6);
         String rounded = nf.format(res);
         res2 = round(res2,6);
          nf = NumberFormat.getNumberInstance(Locale.CANADA);
         nf.setMaximumFractionDigits(6);
         String rounded2 = nf.format(res2);
         sellRes.setText(rounded2);
         buyRes.setText(rounded);
         from =tempFrom;
         to =tempTO;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private void initFacebookLogin(View container) {
       // LoginManager.getInstance().logOut();

        loginButton = container.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.setFragment(this);

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
