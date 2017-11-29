package com.example.jagandeep.igotvkaraoke.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.jagandeep.igotvkaraoke.DataModel.KaraokeBuilderDataModel;
import com.example.jagandeep.igotvkaraoke.DataModel.VideoOnDemandInformation;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.KaraokeStatusCheckRequest;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.ServiceCallBack;
import com.example.jagandeep.igotvkaraoke.R;
import com.example.jagandeep.igotvkaraoke.Utility.Utility;
import com.example.jagandeep.igotvkaraoke.databinding.ActivityKaraokeActionBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

public class KaraokeActionActivity extends BaseActivity {

    ActivityKaraokeActionBinding binding;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karaoke_action);

        VideoOnDemandInformation datamodel = Utility.getSharedInstance().getVODInfoModel();



        binding =  DataBindingUtil.setContentView(this, R.layout.activity_karaoke_action);
        String value = getIntent().getExtras().getString("background");
        if (value!=null){
            Picasso.with(this).load(value).into(binding.karaokeBackgroundImage);
        }

        if (!(datamodel.getVideoId()+"").equals(Utility.getSharedInstance().getValueFromPreference(this,"videoid"))&& datamodel!=null
                ||Utility.getSharedInstance().getValueFromPreference(this,"recordUrl")==null){
            binding.buttonthird.setVisibility(View.GONE);
        }

        Picasso.with(this).load(datamodel.getCover_image()).into(binding.karaokeCoverImage);
    }

    public void gotoHomeActivity(View view){
        Intent intent = new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void firstButton(View view){
        Utility.getSharedInstance().setPlayerTitle("KARAOKE");
        Intent intent = new Intent(KaraokeActionActivity.this, KaraokePlayerActivity.class);
        intent.putExtra("sing","sing");
        startActivity(intent);

    }

    public void secondButton(View view){
        Intent intent = new Intent(KaraokeActionActivity.this, KaraokePlayerActivity.class);
        intent.putExtra("sing","record");
        startActivity(intent);
    }

    public void playRecord(View view){

        getstatusPath(Utility.getSharedInstance().getValueFromPreference(this,"videoid"),Utility.getSharedInstance().getValueFromPreference(this,"recordkey"));
    }

    /**
     * Method to request server for Assets Path.
     * Show error on api failure
     *
     */
    private void getstatusPath(String videoId,String Key){

        Map<String,String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/x-www-form-urlencoded");
        map.put("Mac-Id", Utility.getSharedInstance().getMacAddr("wlan0"));
        map.put("Serial-No", Utility.getSharedInstance().getSerialNumber());

        try {

            JSONObject obj = new JSONObject(Utility.getSharedInstance().getAccessToken(this));
            map.put(getResources().getString(R.string.Authorization),
                    obj.getString(getResources().getString(R.string.TokenType)) + " " +
                            obj.getString(getResources().getString(R.string.AccessTokenWithUnderscore)));
            Log.e("token = ",obj.getString(getResources().getString(R.string.AccessTokenWithUnderscore)));
//            map.put("Content-Type", "application/json");
        } catch (Exception ex) {
            Log.e("EXCEPTION",ex.getLocalizedMessage());
            return;
        }

        Map<String,String> param = new HashMap<>();
        map.put("key", Key);

        new KaraokeStatusCheckRequest().statusKaraokeWithMap(videoId,map, Key, new ServiceCallBack<KaraokeBuilderDataModel>(){
            @Override
            public void onSuccess(Response<KaraokeBuilderDataModel> response) {
                if (response.body()!=null&&response.body().getstatus().equals("success")) {
                    Intent intent = new Intent(KaraokeActionActivity.this, KaraokePlayerActivity.class);
                    intent.putExtra("sing", "playrecord");
                    intent.putExtra("singUrl", Utility.getSharedInstance().getValueFromPreference(KaraokeActionActivity.this, "recordUrl"));
                    startActivity(intent);
                }
                else if(response.body()!=null&&response.body().getstatus().equals(getResources().getString(R.string.unauthorised))) {
                    Utility.getSharedInstance().logout(KaraokeActionActivity.this);
                }
                else if(response.body()!=null){
                    Utility.getSharedInstance().showToast(KaraokeActionActivity.this,response.body().getmessage());
                }
                else {
                    Utility.getSharedInstance().showToast(KaraokeActionActivity.this,
                            "error");
                }
            }

            @Override
            public void onError(String header, String message) {
                Utility.getSharedInstance().showToast(KaraokeActionActivity.this,getResources().getString(R.string.something_wrong));
                System.out.println("--failure--"+message);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!( Utility.getSharedInstance().getVODInfoModel().getVideoId()+"").equals(Utility.getSharedInstance().getValueFromPreference(this,"videoid"))
                ||Utility.getSharedInstance().getValueFromPreference(this,"recordUrl")==null){
            binding.buttonthird.setVisibility(View.GONE);
        }
        else {
            binding.buttonthird.setVisibility(View.VISIBLE);
        }
    }
}
