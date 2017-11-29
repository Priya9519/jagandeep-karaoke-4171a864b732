package com.example.jagandeep.igotvkaraoke.Activities.Application;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Field;

import io.fabric.sdk.android.Fabric;


/**
 * Created by jagandeep on 6/23/17.
 */

public class StreamApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        overrideFont(getApplicationContext(), "NORMAL", "fonts/Champagne.ttf");
        overrideFont(getApplicationContext(), "MONOSPACE", "fonts/ChampagneBoldItalic.ttf");
        overrideFont(getApplicationContext(), "SERIF", "fonts/snell.ttf");
        overrideFont(getApplicationContext(), "SANS", "fonts/signikaSemibold.ttf");
    }

    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {

        }
    }
}