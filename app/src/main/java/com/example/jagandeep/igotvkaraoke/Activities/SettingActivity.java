package com.example.jagandeep.igotvkaraoke.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.jagandeep.igotvkaraoke.ApplicationUpdate.AutoUpdateApk;
import com.example.jagandeep.igotvkaraoke.R;
import com.example.jagandeep.igotvkaraoke.databinding.ActivitySettingBinding;

import java.util.ArrayList;

public class SettingActivity extends BaseActivity {

    ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        setActivityLayout();
    }

    void setActivityLayout(){
        String version = null;
        String build = null;;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            build = pInfo.versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> values = new ArrayList<String>();
        values.add("Check for update");

        if (version!=null){
            values.add("Version Name : "+version);
            values.add("Version No :"+build);
        }

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.size(); ++i) {
            list.add(values.get(i));
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        binding.settingList.setAdapter(adapter);

        binding.settingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                if (position==0) {
                    setUpAutoUpdate();
                }
            }

        });
    }


    private void setUpAutoUpdate()
    {
        AutoUpdateApk aua = new AutoUpdateApk(SettingActivity.this);	// <-- don't forget to instantiate
        aua.checkUpdatesManually();
    }

    public void gotoHomeActivity(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
