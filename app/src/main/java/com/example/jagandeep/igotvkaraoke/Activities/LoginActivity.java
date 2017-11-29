package com.example.jagandeep.igotvkaraoke.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.jagandeep.igotvkaraoke.ApplicationUpdate.AutoUpdateApk;
import com.example.jagandeep.igotvkaraoke.DataModel.AssetsDataModel;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.ApplicationConfig;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.GetAssetsFolderRequest;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.ServiceCallBack;
import com.example.jagandeep.igotvkaraoke.R;
import com.example.jagandeep.igotvkaraoke.Utility.JavascriptListner;
import com.example.jagandeep.igotvkaraoke.Utility.LogUtils;
import com.example.jagandeep.igotvkaraoke.Utility.NavigationInterface;
import com.example.jagandeep.igotvkaraoke.Utility.Utility;
import com.example.jagandeep.igotvkaraoke.databinding.ActivityLoginBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

public class LoginActivity extends  BaseActivity implements NavigationInterface {

    boolean doubleBackToExitPressedOnce = false;
    ActivityLoginBinding binding;
    private String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpAutoUpdate();

        hasPermission(new ArrayList<String>(Arrays.asList(permission)));


        LogUtils.LOGE("assets path", Utility.getSharedInstance().getAssetsPath(LoginActivity.this) + "");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        JavascriptListner listnerObj = new JavascriptListner(this);

        binding.authWebView.loadUrl(ApplicationConfig.getBaseUrl() + ApplicationConfig.getAuthorizationUrl());
        binding.authWebView.getSettings().setJavaScriptEnabled(true);
        binding.authWebView.addJavascriptInterface(listnerObj, "HTMLOUT");
        binding.authWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.authWebView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.LOGE("Errorrrr", "here it is ");

                if (url.contains(ApplicationConfig.getBaseUrl() + "auth/callback?code=")) {
//                    startingNextActivity(null);
                    try {
                        Utility.getSharedInstance().showAlertWithListner(LoginActivity.this, getResources().getString(R.string.app_name), "Please Login with Viewer Club.", "OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CookieSyncManager.createInstance(LoginActivity.this);
                                CookieManager cookieManager = CookieManager.getInstance();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    cookieManager.removeAllCookies(null);
                                } else {
                                    cookieManager.removeAllCookie();
                                }
                                binding.authWebView.loadUrl(ApplicationConfig.getBaseUrl() + ApplicationConfig.getAuthorizationUrl());

                                PackageManager manager = getPackageManager();
                                try {
                                    Intent i = manager.getLaunchIntentForPackage("com.example.jagandeep.igotvmain");
                                    if (i == null)
                                        throw new PackageManager.NameNotFoundException();
                                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                                    startActivity(i);
                                } catch (Exception e) {
                                    Utility.getSharedInstance().showToast(LoginActivity.this, "Unable to open Viewer Club");
                                }
                            }
                        });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    binding.authWebView.setVisibility(View.VISIBLE);
                }

                view.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('pre')[0].innerHTML);");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Make a note about the failed load.
                onError(errorCode);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // Make a note about the failed load.
                if (Build.VERSION.SDK_INT > 22) {
                    onError(error.getErrorCode());
                }
            }

        });


        binding.button2.setBackground(getResources().getDrawable(R.drawable.alert_background));
        binding.button2.setTextColor(Color.parseColor("#bb3333"));

    }

    public void onError(Integer errorCode) {
        LogUtils.LOGE("Login Activity ", "error : " + errorCode);
        binding.button2.setVisibility(View.VISIBLE);
    }

    private void setUpAutoUpdate() {
        AutoUpdateApk aua = new AutoUpdateApk(getApplicationContext());    // <-- don't forget to instantiate
        aua.checkUpdatesManually();
    }

    public void reloadWebview(View view) {
        binding.button2.setVisibility(View.INVISIBLE);
        getAssetsPath();
//        binding.authWebView.reload();
    }

    /**
     * Method to request server for Assets Path.
     * Show error on api failure
     */
    private void getAssetsPath() {

        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        map.put("Mac-Id", Utility.getSharedInstance().getMacAddr("wlan0"));
        map.put("Serial-No", Utility.getSharedInstance().getSerialNumber());

        float density = getResources().getDisplayMetrics().density;
// return 0.75 if it's LDPI
// return 1.0 if it's MDPI
// return 1.5 if it's HDPI
// return 2.0 if it's XHDPI
// return 3.0 if it's XXHDPI
// return 4.0 if it's XXXHDPI
        String reso = "";
        if (density == 0.75) {
            reso = "ldpi";
        } else if (density == 1.0) {
            reso = "mdpi";
        } else if (density == 1.0) {
            reso = "hdpi";
        } else if (density == 1.5) {
            reso = "xhdpi";
        } else {
            reso = "xxhdpi";
        }

        new GetAssetsFolderRequest().getAssetsPath(map, reso, new ServiceCallBack<AssetsDataModel>() {
            @Override
            public void onSuccess(Response<AssetsDataModel> response) {
                if (response.body() != null && response.body().getStatus().equals(getResources().getString(R.string.success))) {
                    Utility.getSharedInstance().saveAssetsPath(LoginActivity.this, response.body().getData().getFolder().get(0));
                    LogUtils.LOGE("assets path", Utility.getSharedInstance().getAssetsPath(LoginActivity.this) + "");

                    ActivityManager am = (ActivityManager) LoginActivity.this.getSystemService(Context.ACTIVITY_SERVICE);
                    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                    LogUtils.LOGE("activity", cn.getClassName());
                    if (Utility.getSharedInstance().getAccessToken(LoginActivity.this) != null &&
                            Utility.getSharedInstance().getAssetsPath(LoginActivity.this) != null &&
                            !cn.getClassName().equals(getApplicationContext().getPackageName() + ".Activities.HomeActivity")) {
                        startingNextActivity(null);
                        return;
                    }
                }
                else {
                    Utility.getSharedInstance().removeAssetsPath(LoginActivity.this);
                    binding.button2.setVisibility(View.VISIBLE);
                    binding.button2.setText(" Not Registered 没有认真 ");
//                    Utility.getSharedInstance().showToast(LoginActivity.this,getResources().getString(R.string.vod_failure));
                }
            }

            @Override
            public void onError(String header, String message) {
                Utility.getSharedInstance().showToast(LoginActivity.this, getResources().getString(R.string.something_wrong));
                System.out.println("--failure--" + message);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.getSharedInstance().getAccessToken(this) != null &&
                Utility.getSharedInstance().getAssetsPath(this) != null) {
            LoginActivity.this.finish();
            LoginActivity.this.finishAffinity();

        }

        if (Utility.getSharedInstance().getAccessToken(this) == null) {
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeAllCookies(null);
            } else {
                cookieManager.removeAllCookie();
            }
            binding.authWebView.loadUrl(ApplicationConfig.getBaseUrl() + ApplicationConfig.getAuthorizationUrl());

        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finish();
            this.finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Utility.getSharedInstance().showToast(this, "Please click BACK again to QUIT");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }

    @Override
    public void startingNextActivity(Bundle bundle) {

        if (Utility.getSharedInstance().getAssetsPath(this) != null
                &&Utility.getSharedInstance().getAccessToken(this) != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            getAssetsPath();
        }

        if (Utility.getSharedInstance().getAccessToken(this) == null) {
            binding.authWebView.clearCache(true);
            binding.authWebView.loadUrl(ApplicationConfig.getBaseUrl() + ApplicationConfig.getAuthorizationUrl());
        }
    }

    public void permissionRequest(String[] arr) {
        ActivityCompat.requestPermissions(LoginActivity.this, arr, 11111);
    }

    private boolean hasPermission(ArrayList<String> permissions) {
        boolean permissionGranted = true;
        boolean askedPermission = false;
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission :
                    permissions) {
                permissionGranted = (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED);
                if (!permissionGranted) {
                    permissionRequest(permissions.toArray(new String[]{permission}));
                    askedPermission = true;
                }
            }

            if (!askedPermission) {
                if (Utility.getSharedInstance().getAccessToken(this) != null &&
                        Utility.getSharedInstance().getAssetsPath(this) != null) {
                    startingNextActivity(null);
                } else {
                    getAssetsPath();
                }
            }

            return permissionGranted;
        } else { //permission is automatically granted on sdk<23 upon installation
            getAssetsPath();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int isPermitted = -1;
        if (requestCode == 11111) {
            if (grantResults.length > 0)
                isPermitted = 0;
            for (int i = 0; i < grantResults.length; i++) {

                isPermitted = grantResults[i] + isPermitted;
            }

            if (isPermitted == 0) {
                if ((android.os.Build.VERSION.SDK_INT > 25 &&
                        checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) &&
                        (android.os.Build.VERSION.SDK_INT > 25 &&
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ||
                        android.os.Build.VERSION.SDK_INT < 25)
                    getAssetsPath();
//                else
//                    Utility.getSharedInstance().showToast(LoginActivity.this, "Please allow permission from phone setting.");
            }
//            else {
//                Utility.getSharedInstance().showToast(LoginActivity.this, "Please allow permission from phone setting.");
//            }

        }
    }
}