package com.example.jagandeep.igotvkaraoke.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jagandeep.igotvkaraoke.Activities.LoginActivity;
import com.example.jagandeep.igotvkaraoke.DataModel.VideoOnDemandInformation;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.CallBacks;
import com.example.jagandeep.igotvkaraoke.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import io.github.kobakei.centereddrawablebutton.CenteredDrawableButton;

/**
 * Created by jagandeep on 5/3/17.
 */

public class Utility extends BroadcastReceiver{

    private static Utility mInstance = null;

    private  VideoOnDemandInformation movieDataModel;

    private String playerTitle;
    private Toast mToast;
    private CallBacks picassoCallBacks;

    private com.squareup.picasso.Target picassoTarget = new com.squareup.picasso.Target()
    {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
        {

            picassoCallBacks.callbackObserver(bitmap);
            LogUtils.LOGE("UTILITY","Drawable downloaded ");
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable)
        {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable)
        {
        }
    };
    private Utility(){
    }

    public static Utility getSharedInstance(){
        if(mInstance == null)
        {
            mInstance = new Utility();
        }
        return mInstance;
    }

    public void showToast(Context context,String message){
        if (mToast==null) {
            mToast = Toast.makeText(context, message, mToast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.show();
    }

    public void setVODInfoModel(VideoOnDemandInformation model){

        this.movieDataModel = model;
    }

    public VideoOnDemandInformation getVODInfoModel(){
        if(movieDataModel.toString()==null){
            Log.e("Null=======","Null=======");
        }
        return this.movieDataModel;
    }

    public void hideToast(){
        mToast.cancel();
    }


    public void showAlert(final Context context, String title, String message, String okText, final View.OnClickListener listner){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        View view = inflater.inflate(R.layout.custom_alert, null);

        TextView titleTV = (TextView)view.findViewById(R.id.title_textview);
        titleTV.setText(title);

        TextView descpTV = (TextView)view.findViewById(R.id.description_textview);
        descpTV.setText(message);

        dialog.setView(view);

        final AlertDialog alert = dialog.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert.show();


        final CenteredDrawableButton closeBtn = (CenteredDrawableButton)view.findViewById(R.id.close_btn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        final CenteredDrawableButton okBtn = (CenteredDrawableButton)view.findViewById(R.id.ok_btn);
        okBtn.getTextView().setText(okText);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClick(v);
            }
        });

        downloadBitmap(context, "button", new CallBacks() {
            @Override
            public void callbackObserver(Object obj) {
                closeBtn.setBackground((Drawable) obj);
                closeBtn.requestLayout();
                okBtn.setBackground((Drawable)obj);
                okBtn.requestLayout();
            }
        });

    }


    public void downloadBitmap(final Context context,String image, final CallBacks callBacks){
        picassoCallBacks = new CallBacks() {
            @Override
            public void callbackObserver(Object obj) {
                Drawable d = new BitmapDrawable(context.getResources(), (Bitmap) obj);
                callBacks.callbackObserver(d);
            }
        };
        Picasso.with(context).load(this.getAssetsPath(context)+image+".png").into(picassoTarget);
    }

    public void showAlertWithListner(Context context, String title, String message, String okText, final View.OnClickListener listner){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        View view = inflater.inflate(R.layout.custom_alert, null);

        TextView titleTV = (TextView)view.findViewById(R.id.title_textview);
        titleTV.setText(title);

        TextView descpTV = (TextView)view.findViewById(R.id.description_textview);
        descpTV.setText(message);

        dialog.setView(view);

        final AlertDialog alert = dialog.create();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert.show();


        CenteredDrawableButton closeBtn = (CenteredDrawableButton)view.findViewById(R.id.close_btn);
        closeBtn.setVisibility(View.GONE);

        CenteredDrawableButton okBtn = (CenteredDrawableButton)view.findViewById(R.id.ok_btn);
        okBtn.getTextView().setText(okText);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                listner.onClick(v);
            }
        });

    }

    public void saveToPreference(Context context,String key,String value){
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(key,value).apply();
    }

    public String getValueFromPreference(Context context,String key){
        return  PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
    }

    public void setToUnstrictMode() {
        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void setPlayerTitle(String model){
        this.playerTitle = model;
    }

    public String getPlayerTitle(){
        return this.playerTitle;
    }

    public void removeAssetsPath(Context context){
        LogUtils.LOGE("Utility","Saved Assets Path");
        SharedPreferences.Editor editor = context.getSharedPreferences("packageNames", Context.MODE_PRIVATE).edit();
        editor.remove("assetPath");
        editor.apply();
    }


    public void logout(Context context){
        Utility.getSharedInstance().removeAssetsPath(context);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public String getMacAddr(String str) {

        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase(str)) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                String text = res1.toString();
//                String result = text.substring(text.length()/2-2,text.length()/2+2);
                return text;
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public String getSerialNumber(){

//        String text = Build.SERIAL;
//        text = text.substring(text.length()/2-2,text.length()/2+2);
        if (Build.VERSION.SDK_INT > 25){

            return Build.getSerial();
        } else{
            return Build.SERIAL;
        }

    }

    public void getMultiplier(Activity activity,TextView textView){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float)(textView.getTextSize()* (float)width/height)*0.7f);
    }

    public String getAccessToken(Context context){
        LogUtils.LOGE("Utility","Get Token");
            try {
                Uri allTitles = Uri.parse(
                        "content://com.igotvmain.igotv_contentprovider.applicationmoderator/applicationmoderators");
                Cursor cursor = context.getContentResolver().query(allTitles, null, null, null, null);
                if (cursor != null) {
                    if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                        while (!cursor.isAfterLast()) {
                            String accesstoken = cursor.getString(cursor.getColumnIndex("accesstoken"));
                            cursor.close();
                            return accesstoken;
                        }
                    }
                }

            } catch (Exception exp) {
                exp.printStackTrace();
            }
            return null;

    }

    public Boolean saveAssetsPath(Context context,String token){
        LogUtils.LOGE("Utility","Saved Assets Path");
        SharedPreferences.Editor editor = context.getSharedPreferences("packageNames", Context.MODE_PRIVATE).edit();
        editor.putString("assetPath",token);
        editor.apply();
        return true;
    }

    public String getAssetsPath(Context context){
        LogUtils.LOGE("Utility","Get Assetes Path");
        SharedPreferences prefs = context.getSharedPreferences("packageNames", Context.MODE_PRIVATE);
        String restoredText = prefs.getString("assetPath", null);
        return  restoredText;
    }






    /**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     */
    public static boolean isRooted() {

        // get from build info
        String buildTags = Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
            // ignore
        }

        // try executing commands
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
