package com.example.jagandeep.igotvkaraoke.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.jagandeep.igotvkaraoke.DataModel.KaraokeBuilderDataModel;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.CallBacks;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.KaraokeBuilderRequest;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.ServiceCallBack;
import com.example.jagandeep.igotvkaraoke.R;
import com.example.jagandeep.igotvkaraoke.Utility.IGOTVExoplayer;
import com.example.jagandeep.igotvkaraoke.Utility.LogUtils;
import com.example.jagandeep.igotvkaraoke.Utility.Utility;
import com.example.jagandeep.igotvkaraoke.databinding.ActivityKaraokePlayerBinding;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class KaraokePlayerActivity extends BaseActivity{

    private String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE};
   private KProgressHUD hud;
    private boolean recordingStarted = false;
    private ActivityKaraokePlayerBinding binding;

    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;

    private static final int DISPLAY_WIDTH = 720;
    private static final int DISPLAY_HEIGHT = 1280;

    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;

    SurfaceHolder holder;
    Timer timer = null;
    Integer period = 0;

    String isSingPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karaoke_player);

        isSingPlayer = getIntent().getStringExtra("sing");

        requestPermission();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_karaoke_player);
        binding.karaokePlayer.setPlayer(IGOTVExoplayer.getSharedInstance(this).getSimpleExoPlayerView().getPlayer());
       if (isSingPlayer.equals("record")) {
           binding.karaokePlayer.hideController();
           binding.karaokePlayer.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
               @Override
               public void onVisibilityChange(int i) {
                   if (i == 0) {
                       binding.karaokePlayer.hideController();
                   }
               }
           });
           binding.karaokePlayer.getPlayer().setVolume(0.5f);


       }else if (isSingPlayer.equals("sing")){
           binding.karaokePlayer.getPlayer().setVolume(1.0f);
           binding.recordButton.setVisibility(View.GONE);
           binding.recordTime.setVisibility(View.GONE);
           binding.userVolume.setVisibility(View.GONE);
           binding.playerVolume.setVisibility(View.GONE);

       }

        binding.playerVolume.setProgress(60);
        binding.userVolume.setProgress(100);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        String url = Utility.getSharedInstance().getVODInfoModel().getAppUrl() + Utility.getSharedInstance().getVODInfoModel().getVODCodecInfo().getHighResulutionURLs()[0];

        Utility.getSharedInstance().downloadBitmap(KaraokePlayerActivity.this, "record", new CallBacks() {
            @Override
            public void callbackObserver(Object obj) {
                binding.recordButton.getTextView().setCompoundDrawablesWithIntrinsicBounds((Drawable)obj, null, null, null);
            }
        });

        IGOTVExoplayer.getSharedInstance(this).playStream(url);

        if (isSingPlayer.equals("playrecord")){
            IGOTVExoplayer.getSharedInstance(this).playStream(getIntent().getStringExtra("singUrl"));

            binding.karaokePlayer.getPlayer().setVolume(1.0f);
            binding.recordButton.setVisibility(View.GONE);
            binding.recordTime.setVisibility(View.GONE);
            binding.userVolume.setVisibility(View.GONE);
            binding.playerVolume.setVisibility(View.GONE);
        }

        IGOTVExoplayer.getSharedInstance(this).stopPlayer(true);
        IGOTVExoplayer.getSharedInstance(this).setPlayerListener(new CallBacks.playerCallBack() {
            @Override
            public void onItemClickOnItem(Integer albumId) {

            }

            @Override
            public void onPlayingEnd() {

                if (mediaRecorder!= null) {
                    toggleTimer(null);
                    Utility.getSharedInstance().downloadBitmap(KaraokePlayerActivity.this, "record", new CallBacks() {
                        @Override
                        public void callbackObserver(Object obj) {
                            binding.recordButton.getTextView().setCompoundDrawablesWithIntrinsicBounds((Drawable)obj, null, null, null);
                        }
                    });

                    binding.recordButton.setVisibility(View.INVISIBLE);
                    binding.playBtn.setVisibility(View.VISIBLE);
                    try {
                        mediaRecorder.stop();
                        mediaRecorder.reset();
                        mediaRecorder.release();
                        mediaRecorder = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                    IGOTVExoplayer.getSharedInstance(KaraokePlayerActivity.this).stopPlayer(true);
                    Toast.makeText(KaraokePlayerActivity.this, "Recording Completed",
                            Toast.LENGTH_LONG).show();
                    recordingStarted = false;
                }
            }
        });

        if (isSingPlayer.equals("sing")||isSingPlayer.equals("playrecord")) {
            IGOTVExoplayer.getSharedInstance(this).stopPlayer(false);
        }

        binding.playerVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtils.LOGE("Player",seekBar.getProgress()+"");
                binding.karaokePlayer.getPlayer().setVolume((float)seekBar.getProgress()/100);
            }
        });

    }


    /**
     * Method to request server for all Video data and filter keys.
     * Show error on api failure and call method to load next activity on api success
     *
     */
    private void saveKaraoke(){
        Utility.getSharedInstance().saveToPreference(this,"videoid",Utility.getSharedInstance().getVODInfoModel().getVideoId()+"");
        Map<String,String> map = new HashMap<>();

        try {

            JSONObject obj = new JSONObject(Utility.getSharedInstance().getAccessToken(this));
            map.put(getResources().getString(R.string.Authorization),
                    obj.getString(getResources().getString(R.string.TokenType)) + " " +
                            obj.getString(getResources().getString(R.string.AccessTokenWithUnderscore)));
            Log.e("token = ",obj.getString(getResources().getString(R.string.AccessTokenWithUnderscore)));
            map.put("Accept", "application/json");
            map.put("Mac-Id", Utility.getSharedInstance().getMacAddr("wlan0"));
            map.put("Serial-No", Utility.getSharedInstance().getSerialNumber());
//            map.put("Content-Type", "application/json");
        } catch (JSONException ex) {
            Log.e("EXCEPTION",ex.getLocalizedMessage());
        }

       hud =  KProgressHUD.create(KaraokePlayerActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait...")
                .setDetailsLabel("Building Karaoke")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        File file = new File(AudioSavePathInDevice);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("video/3gpp"),
                        file
                );
        Map<String,RequestBody> param = new HashMap<>();
        param.put("v1", RequestBody.create(
                okhttp3.MultipartBody.FORM, (float)binding.playerVolume.getProgress()/100+""));
        param.put("v2", RequestBody.create(
                okhttp3.MultipartBody.FORM, (float)binding.userVolume.getProgress()/100+""));
//        MultipartBody.Part v1 = MultipartBody.Part.createFormData("v1",binding.playerVolume.getProgress()+"");
//        MultipartBody.Part v2 = MultipartBody.Part.createFormData("v1",binding.playerVolume.getProgress()+"");
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);



        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);
        param.put("file",description);
        new KaraokeBuilderRequest().saveKaraokeWithMap(Utility.getSharedInstance().getVODInfoModel().getVideoId()+"",
                map,param,body, new ServiceCallBack<KaraokeBuilderDataModel>(){
                    @Override
                    public void onSuccess(Response<KaraokeBuilderDataModel> response) {
                        hud.dismiss();

                        if(response.body()!=null&&response.body().getstatus().equals(getResources().getString(R.string.unauthorised))) {
                            Utility.getSharedInstance().logout(KaraokePlayerActivity.this);
                            return;
                        }

                        Utility.getSharedInstance().saveToPreference(KaraokePlayerActivity.this,"recordkey",response.body().getKey());
                        Utility.getSharedInstance().saveToPreference(KaraokePlayerActivity.this,"recordUrl",response.body().getfile());
                        Utility.getSharedInstance().showToast(KaraokePlayerActivity.this,"Successfully upload");
                        binding.karaokePlayer.getPlayer().setVolume(1.0f);
                        binding.recordButton.setVisibility(View.GONE);
                        binding.recordTime.setVisibility(View.GONE);

                        File file = new File(AudioSavePathInDevice);
                        boolean deleted = file.delete();
                        LogUtils.LOGE("Deleted","  "+deleted);

                        IGOTVExoplayer.getSharedInstance(KaraokePlayerActivity.this).stopPlayer(true);
//                        IGOTVExoplayer.getSharedInstance(KaraokePlayerActivity.this).playStream(response.body().getfile());
                        gotoHomeActivity(null);
                        binding.karaokePlayer.setControllerVisibilityListener(null);

                    }
                    @Override
                    public void onError(String header, String message) {
                        hud.dismiss();
                        Utility.getSharedInstance().showToast(KaraokePlayerActivity.this,getResources().getString(R.string.something_wrong));
                        System.out.println("--failure--"+message);
                    }
                });
    }

    public void startRecording(View view) {

        if (!recordingStarted && checkPermission()){
            if(checkPermission()) {

                if (!isNetworkAvailable()){
                    Utility.getSharedInstance().showAlert(this,"Internet Problem","Please check your internet connection.","Quit",
                            new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    KaraokePlayerActivity.this.finish();
                                }
                            });
                    return;
                }
                IGOTVExoplayer.getSharedInstance(this).stopPlayer(false);
                AudioSavePathInDevice =
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                MediaRecorderReady();

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("Exception","IllegalStateException");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    IGOTVExoplayer.getSharedInstance(this).stopPlayer(true);
                    Utility.getSharedInstance().showAlertWithListner(this, "Permission Denied", "You denied microphone permission !! \n Please check your phone settings.", "Quit", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            KaraokePlayerActivity.this.finish();
                        }
                    });
                    return;

                }
                Utility.getSharedInstance().downloadBitmap(KaraokePlayerActivity.this, "record_stop", new CallBacks() {
                    @Override
                    public void callbackObserver(Object obj) {
                        binding.recordButton.getTextView().setCompoundDrawablesWithIntrinsicBounds((Drawable)obj, null, null, null);
                    }
                });

                toggleTimer(null);


                Toast.makeText(KaraokePlayerActivity.this, "Recording started",
                        Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }

            recordingStarted=true;
        }else if (recordingStarted){
            toggleTimer(null);
            Utility.getSharedInstance().downloadBitmap(KaraokePlayerActivity.this, "record", new CallBacks() {
                @Override
                public void callbackObserver(Object obj) {
                    binding.recordButton.getTextView().setCompoundDrawablesWithIntrinsicBounds((Drawable)obj, null, null, null);
                }
            });
            binding.recordButton.setVisibility(View.INVISIBLE);
//            binding.playBtn.setVisibility(View.VISIBLE);

            try {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
            }
            catch (Exception e){
                e.printStackTrace();
                return;
            }

            IGOTVExoplayer.getSharedInstance(this).stopPlayer(true);
            Toast.makeText(KaraokePlayerActivity.this, "Recording Completed",
                    Toast.LENGTH_LONG).show();
            recordingStarted=false;
            saveKaraoke();
        }
        else {
            requestPermission();
        }
    }

    public void playToggle(View view){
//        OminaAudioPlayer.getSharedInstance(this).playStream(AudioSavePathInDevice);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(AudioSavePathInDevice);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("karaoke player","error here - "+what+"extra"+extra);
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                toggleTimer(null);
            }
        });

        period=0;
        toggleTimer(null);
        mediaPlayer.start();

        binding.karaokePlayer.getPlayer().setVolume(1.0f);
        binding.playBtn.setVisibility(View.GONE);

    }

    public void toggleTimer(Integer duration){
        if (period==0) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {


                                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                                      period++;
                                                      int sec = period%60;
                                                      String secString = String.format("%02d", sec);
                                                      String minString = String.format("%02d", period/60);
                                                      binding.recordTime.setText(minString+":"+secString);


                                                  }

                                              });}
                                      },
                    0,
                    1000);
        }
        else {
            period = 0;
            timer.cancel();
        }
    }


    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(RandomAudioFileName.length());
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(KaraokePlayerActivity.this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, RequestPermissionCode);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    boolean CameraPermission = grantResults[2] ==
                            PackageManager.PERMISSION_GRANTED;

//                    if (StoragePermission && RecordPermission && CameraPermission) {
//                        Toast.makeText(KaraokePlayerActivity.this, "Permission Granted",
//                                Toast.LENGTH_LONG).show();
//                    }
                    if (!(StoragePermission && RecordPermission && CameraPermission)){
                        Toast.makeText(KaraokePlayerActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED;
    }

    public void gotoHomeActivity(View view){
        if(recordingStarted) {
            Utility.getSharedInstance().showAlert(this,"","Do you want to save this Recording","Yes",
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            toggleTimer(null);
                            Utility.getSharedInstance().downloadBitmap(KaraokePlayerActivity.this, "record", new CallBacks() {
                                @Override
                                public void callbackObserver(Object obj) {
                                    binding.recordButton.getTextView().setCompoundDrawablesWithIntrinsicBounds((Drawable)obj, null, null, null);
                                }
                            });
                            binding.recordButton.setVisibility(View.INVISIBLE);
//            binding.playBtn.setVisibility(View.VISIBLE);

                            try {
                                mediaRecorder.stop();
                                mediaRecorder.reset();
                                mediaRecorder.release();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                return;
                            }
                            IGOTVExoplayer.getSharedInstance(KaraokePlayerActivity.this).stopPlayer(true);
                            Toast.makeText(KaraokePlayerActivity.this, "Recording Completed",
                                    Toast.LENGTH_LONG).show();
                            recordingStarted=false;
                            saveKaraoke();
                        }
                    });
            return;
        }
           Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        IGOTVExoplayer.getSharedInstance(this).stopPlayer(true);
        if (recordingStarted){
            startRecording(null);
            KaraokePlayerActivity.this.finish();
            Utility.getSharedInstance().downloadBitmap(KaraokePlayerActivity.this, "record", new CallBacks() {
                @Override
                public void callbackObserver(Object obj) {
                    binding.recordButton.getTextView().setCompoundDrawablesWithIntrinsicBounds((Drawable)obj, null, null, null);
                }
            });

            IGOTVExoplayer.getSharedInstance(this).stopPlayer(true);
            recordingStarted=false;
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {

        IGOTVExoplayer.getSharedInstance(this).stopPlayer(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }
}

