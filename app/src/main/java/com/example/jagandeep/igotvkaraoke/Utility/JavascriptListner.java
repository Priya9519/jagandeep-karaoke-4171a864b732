package com.example.jagandeep.igotvkaraoke.Utility;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.example.jagandeep.igotvkaraoke.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jagandeep on 5/3/17.
 */

public class JavascriptListner {
    private Context mContext;
    public JavascriptListner(Context context) {
        this.mContext = context;
    }

    /**
     * Javascript method to fetch accesstoken and save to preference.
     * Show error on invalid JSON.
     *
     * @param html
     */
    @JavascriptInterface
    public void processHTML(String html)
    {
        if (isJSONValid(html)==true) {
//            Utility.getSharedInstance().saveToPreference(mContext, mContext.getResources().getString(R.string.access_token), html);
            Utility.getSharedInstance().showToast(mContext,"Missing IGOTV Main application");
            Log.e("result accessToken = ",html);
        }
        else
        {
            Utility.getSharedInstance().showToast(mContext,mContext.getResources().getString(R.string.authentication_failed));
            Log.e("failed on login ","html");
        }

    }

    /**
     * Instance method to check for valid JSON
     *
     * @param jsonString
     */
    public boolean isJSONValid(String jsonString) {
        try {
            new JSONObject(jsonString);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(jsonString);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
