package com.ms.ryejoon.lettercounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.StringTokenizer;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.microsoft.azure.mobile.MobileCenter;
import com.microsoft.azure.mobile.analytics.Analytics;
import com.microsoft.azure.mobile.crashes.Crashes;
import com.ms.ryejoon.lettercounter.R;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class MainActivity extends Activity{
    /** Called when the activity is first created. */
    private Thread t = null;
    private static Bundle temp = null;
    private boolean spaceChecked = true;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            TextView v = (TextView)findViewById(R.id.number);
            int num = msg.arg1;
            v.setText(String.valueOf(num));

        }
    };



    public void onPause(){
        super.onPause();
        if(temp == null)
            temp = new Bundle();
        super.onSaveInstanceState(temp);
    }

    public void onStop() {
        super.onStop();
        Log.e("Activity", "onStop()");
    }

    public void onDestroy(){
        super.onDestroy();
        System.exit(0);

    }

    public void onResume(){
        super.onResume();
        if(temp != null)
            super.onRestoreInstanceState(temp);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBox space = (CheckBox)findViewById(R.id.space);
        space.setChecked(true);

        space.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                spaceChecked = isChecked;

            }
        });

        Button exit = (Button)findViewById(R.id.exit);
        exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onDestroy();
            }
        });

        if(t == null){
            t = new Thread(){
                public void run(){
                    EditText texts = (EditText) findViewById(R.id.texts);
                    while(true){
                        int num;
                        if(spaceChecked){
                            num = texts.getText().length();
                        }else{
                            StringTokenizer st = new StringTokenizer(texts.getText().toString()," ",false);
                            String t="";
                            while (st.hasMoreElements()) t += st.nextElement();
                            num = t.length();

                        }

                        Message message = Message.obtain(mHandler);
                        message.arg1 = num;
                        mHandler.sendMessage(message);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                        }
                    }
                }
            };
            t.start();
        }

        MobileCenter.start(getApplication(), "3b7e8880-5710-471e-8f1d-15479a76c071",
                Analytics.class, Crashes.class);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6947124393113000~9040713172");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }



    public void onConfigurationChanged(Configuration newConfig) {
        // Ignore orientation change not to restart activity
        super.onConfigurationChanged(newConfig);
    }

}