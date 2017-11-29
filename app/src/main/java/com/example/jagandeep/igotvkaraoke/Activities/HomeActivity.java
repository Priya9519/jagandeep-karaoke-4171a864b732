package com.example.jagandeep.igotvkaraoke.Activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.jagandeep.igotvkaraoke.Adapters.KaraokeGridCellAdapter;
import com.example.jagandeep.igotvkaraoke.Adapters.KaraokeRecyclerAdapter;
import com.example.jagandeep.igotvkaraoke.DataModel.KaraokeDataModel;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.CallBacks;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.KaraokeRequest;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.ServiceCallBack;
import com.example.jagandeep.igotvkaraoke.R;
import com.example.jagandeep.igotvkaraoke.Utility.NavigationInterface;
import com.example.jagandeep.igotvkaraoke.Utility.Utility;
import com.example.jagandeep.igotvkaraoke.databinding.ActivityHomeBinding;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

public class HomeActivity extends BaseActivity implements NavigationInterface {


    private ActivityHomeBinding binding;
    private KaraokeDataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Assigning Videmodel to data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

       loadKaraokeData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void startingNextActivity(Bundle bundle) {
        Intent intent = new Intent(this,KaraokeActionActivity.class);
        intent.putExtra("background", dataModel.getBackground());
        startActivity(intent);
    }

    public void showMenu(View view){
        PopupMenu popup = new PopupMenu(HomeActivity.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.home_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.quit:Utility.getSharedInstance().showAlert(HomeActivity.this, getResources().getString(R.string.app_name),
                            getResources().getString(R.string.quit_message), "YES", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    HomeActivity.this.finish();
                                    HomeActivity.this.finishAffinity();
                                }
                            });
                        break;
                    case R.id.setting:
                        Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(intent);

                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }


    public void setActivityLayout(){
        Picasso.with(this).load(dataModel.getBackground()).into(binding.backgroundKaraoke);

        RecyclerView.LayoutManager mLayoutManagerThree = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        binding.karaokeList.setLayoutManager(mLayoutManagerThree);
        binding.karaokeList.setAdapter(new KaraokeRecyclerAdapter(this, dataModel.getData(),
                new CallBacks() {
                    @Override
                    public void callbackObserver(Object id) {
                        Log.e("Karaoke Activity","Item clicked - "+id);
                        Utility.getSharedInstance().setVODInfoModel(dataModel.getData().get((int)id));
                        startingNextActivity(null);
                    }

                }));

        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList("All"," A "," B "," C "," D "," E ",
                "F","G","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"));
        binding.filterGrid.setAdapter(new KaraokeGridCellAdapter(this,arrayList));
    }

    /**
     * Method to request server for Karaoke data and filter keys.
     * Show error on api failure and call method to load next activity on api success
     *
     */
    private void loadKaraokeData(){
        Map<String,String> map = new HashMap<>();

        try {

            JSONObject obj = new JSONObject(Utility.getSharedInstance().getAccessToken(this));
            map.put(getResources().getString(R.string.Authorization),
                    obj.getString(getResources().getString(R.string.TokenType)) + " " +
                            obj.getString(getResources().getString(R.string.AccessTokenWithUnderscore)));
            Log.e("token = ",obj.getString(getResources().getString(R.string.AccessTokenWithUnderscore)));
            map.put("Accept", "application/json");
            map.put("Content-Type", "application/json");
            map.put("Mac-Id", Utility.getSharedInstance().getMacAddr("wlan0"));
            map.put("Serial-No", Utility.getSharedInstance().getSerialNumber());
        } catch (JSONException ex) {
            Log.e("EXCEPTION",ex.getLocalizedMessage());
        }

        final KProgressHUD hud =  KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait...")
                .setDetailsLabel("Fetching Karaoke")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        new KaraokeRequest().getKaraokeWithMap(map, new ServiceCallBack<KaraokeDataModel>(){
            @Override
            public void onSuccess(Response<KaraokeDataModel> response) {
                hud.dismiss();
                if(response.body()!=null&&response.body().getStatus().equals(getResources().getString(R.string.success))) {
                    dataModel=response.body();
                    setActivityLayout();
                }
                else if(response.body()!=null&&response.body().getStatus().equals(getResources().getString(R.string.unauthorised))) {
                    Utility.getSharedInstance().logout(HomeActivity.this);
                }
                else{
                    Utility.getSharedInstance().showToast(HomeActivity.this,getResources().getString(R.string.vod_failure));
                }
            }
            @Override
            public void onError(String header, String message) {
                hud.dismiss();
                Utility.getSharedInstance().showToast(HomeActivity.this,getResources().getString(R.string.something_wrong));
                System.out.println("--failure--"+message);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
                deleteDir(context.getFilesDir());
                deleteDir(context.getExternalCacheDir());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
}
