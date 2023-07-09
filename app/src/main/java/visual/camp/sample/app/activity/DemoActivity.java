package visual.camp.sample.app.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import camp.visual.gazetracker.GazeTracker;
import camp.visual.gazetracker.callback.GazeCallback;
import camp.visual.gazetracker.filter.OneEuroFilterManager;
import camp.visual.gazetracker.gaze.GazeInfo;
import camp.visual.gazetracker.state.EyeMovementState;
import camp.visual.gazetracker.util.ViewLayoutChecker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import visual.camp.sample.app.GazeTrackerManager;
import visual.camp.sample.app.R;
import visual.camp.sample.view.GazePathView;

//webview imports
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class DemoActivity extends AppCompatActivity {
  //recording
  private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;

  private ScreenRecorder screenRecorder;
  private Button startButton;
  private Button stopButton;
  private static final String CHANNEL_ID = "scapp_channel";
  private static final String CHANNEL_NAME = "SCApp Channel";
  private static final String CHANNEL_DESCRIPTION = "SCApp Notification Channel";
  private static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
  //recording

  private long duration=0l;
  private Button endTaskButton;
  private ArrayList<Long> ssTime= new ArrayList<>();
//  private ArrayList<Long> recordTime=new ArrayList<>();


  private ProgressDialog progressDialog;

//  private Handler handler = new Handler();
//  private Runnable screenshotRunnable = new Runnable() {
//    @Override
//    public void run() {
//      if(duration<9600){
//
//        View view = getWindow().getDecorView().getRootView();
//        view.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
//        view.setDrawingCacheEnabled(false);
//        screenshots.add(bitmap);
//        curTime=System.currentTimeMillis();
//        duration=curTime-startTime;
//        ssTime.add(duration);
//
////        if(diffTime>10000 && !stopThread ){
////          endTaskFn();
////        }
////        if(screenshots.size()%8==0) {
////          Toast.makeText(DemoActivity.this, "size:"+screenshots.size(), Toast.LENGTH_SHORT).show();
////        }
//        handler.postDelayed(screenshotRunnable, 350); // Schedule the next screenshot in 1 second
//      }
//    }
//  };
  private boolean stopThread = false;

  Map<Long, PointF> eyeData=new HashMap<>();
  long startTime = System.currentTimeMillis();
  long curTime = System.currentTimeMillis();
  long diffTime;
  long totalTime;
  Bitmap screenshot;
  ArrayList<Bitmap> screenshots = new ArrayList<>();
//  ArrayList<Bitmap> records = new ArrayList<>();
  Long lastss=0l;
  Long lastrecord=0l;
  private int sno=0;
  private static final String TAG = DemoActivity.class.getSimpleName();
  private final ViewLayoutChecker viewLayoutChecker = new ViewLayoutChecker();
  private GazePathView gazePathView;
  private GazeTrackerManager gazeTrackerManager;
  private WebView webView;
  String link, taskId, appName;
  private final OneEuroFilterManager oneEuroFilterManager = new OneEuroFilterManager(
          2, 30, 0.5F, 0.001F, 1.0F);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_demo);
    //recording
    createNotificationChannel(this, CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESCRIPTION, CHANNEL_IMPORTANCE);

    screenRecorder = new ScreenRecorder(this);
    //recording

    gazeTrackerManager = GazeTrackerManager.getInstance();

    endTaskButton = findViewById(R.id.endTaskButton);
    // Get the intent
    link = getIntent().getStringExtra("link");
    taskId = getIntent().getStringExtra("taskId");
    appName = getIntent().getStringExtra("appName");
    endTaskButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(stopThread==false){
          endTaskFn();
          stopThread=true;
        }
      }
    });
  }


  public static void createNotificationChannel(Context context, String channelId, String channelName, String channelDescription, int channelImportance) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(channelId, channelName, channelImportance);
      channel.setDescription(channelDescription);
      NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }
  private void startScreenRecording() {
    Intent serviceIntent = new Intent(this, ScreenCaptureForegroundService.class);
    serviceIntent.putExtra("channel_id", CHANNEL_ID);
    startService(serviceIntent);
    Intent mediaProjectionIntent = ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE)).createScreenCaptureIntent();
    startActivityForResult(mediaProjectionIntent, REQUEST_CODE_SCREEN_CAPTURE);
  }

  private String stopScreenRecording() {
    return screenRecorder.stopRecording();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE_SCREEN_CAPTURE && resultCode == RESULT_OK) {
      //String outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/screen_capture.mp4";
      String outputPath = getFilesDir() + "/screen_capture.mp4";
      screenRecorder.startRecording(resultCode, data, outputPath);
    }
  }

  @Override
  public void onBackPressed() {
    if (webView.canGoBack()) {
      webView.goBack();
    } else {
        Toast.makeText(DemoActivity.this, "Can't go back.", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.i(TAG, "onStart");
    gazeTrackerManager.setGazeTrackerCallbacks(gazeCallback);
//    handler.post(screenshotRunnable);
    initView();
  }

  @Override
  protected void onResume() {
    super.onResume();
    gazeTrackerManager.startGazeTracking();
    setOffsetOfView();
    Log.i(TAG, "onResume");
  }

  @Override
  protected void onPause() {
    super.onPause();
    gazeTrackerManager.stopGazeTracking();
    Log.i(TAG, "onPause");
  }

  @Override
  protected void onStop() {
    super.onStop();
    gazeTrackerManager.removeCallbacks(gazeCallback);
    Log.i(TAG, "onStop");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  private void initView() {
    gazePathView = findViewById(R.id.gazePathView);

    AssetManager am = getResources().getAssets();
    InputStream is = null;

    try {
//      is = am.open("palace_seoul.jpg");
//      Bitmap bm = BitmapFactory.decodeStream(is);
//      ImageView catView = findViewById(R.id.catImage);
//      catView.setImageBitmap(bm);
//      is.close();

      //webview
      startScreenRecording();
      webView = findViewById(R.id.webView);
      webView.getSettings().setJavaScriptEnabled(true);

      webView.setWebViewClient(new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
          // Page has finished loading
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
          // Handle error
        }
      });
//      Toast.makeText(DemoActivity.this, link, Toast.LENGTH_SHORT).show();
      webView.loadUrl(link);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setOffsetOfView() {
    viewLayoutChecker.setOverlayView(gazePathView, new ViewLayoutChecker.ViewLayoutListener() {
      @Override
      public void getOffset(int x, int y) {
        gazePathView.setOffset(x, y);
      }
    });
  }

  private final void endTaskFn(){
    String path = stopScreenRecording();
    gazeTrackerManager.removeCallbacks(gazeCallback);
//    gazeTrackerManager.stopGazeTracking();
//    gazeTrackerManager.deinitGazeTracker();
    // stop executing the Runnable after some time



//    progressDialog.show();

//    gazeTrackerManager.stopGazeTracking();
    stopThread = true;

    curTime = System.currentTimeMillis();
    totalTime=System.currentTimeMillis()-startTime;
    BitmapArrayListSingleton.getInstance().setBitmapArrayList(screenshots);
//    BitmapArrayListSingleton2.getInstance().setBitmapArrayList(records);

//  Save the screenshots to the directory
//    for (int i = 0; i < screenshots.size(); i++) {
//      Bitmap screenshot = screenshots.get(i);
//                try {
//            String ssname=i+"_"+".png";
//            FileOutputStream fos = openFileOutput(ssname, Context.MODE_PRIVATE);
//            screenshot.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.close();
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
//    }

//    progressDialog.dismiss();

    // Create Intent to start FeedbackActivity
    Intent intent = new Intent(DemoActivity.this, EndActivity.class);

//    Map<Long, PointF> sortedMap = new TreeMap<>(eyeData);

//        intent.putExtra("ss", getFilesDir()+"");
    intent.putExtra("eyeData", (Serializable) eyeData);
    intent.putExtra("totalTime", Long.toString(totalTime));
    intent.putExtra("taskId", taskId);
    intent.putExtra("appName", appName);
    intent.putExtra("ssTime", ssTime);
//    intent.putExtra("recordTime", recordTime);
    startActivity(intent);
  }

  private final GazeCallback gazeCallback = new GazeCallback() {
    @Override
    public void onGaze(GazeInfo gazeInfo) {
      if (oneEuroFilterManager.filterValues(gazeInfo.timestamp, gazeInfo.x, gazeInfo.y)) {
        float[] filtered = oneEuroFilterManager.getFilteredValues();
        String cor=Float.toString(filtered[0])+","+Float.toString(filtered[1]);

        curTime = System.currentTimeMillis();
        PointF point = new PointF(filtered[0], (filtered[1]));
        diffTime=curTime-startTime;
        eyeData.put(diffTime, point);
        if(diffTime>9600){
//          handler.removeCallbacks(screenshotRunnable);
        }
        if(diffTime>10000 && !stopThread ){
          endTaskFn();
        }
//        data1.setText(Long.toString(diffTime));


//        float pSize=gazePathView.getGazeData();
//        if(pSize>=90 && curTime-lastss>3000){
//          lastss=curTime;
//          View view = getWindow().getDecorView().getRootView();
//          view.setDrawingCacheEnabled(true);
//          Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
//          view.setDrawingCacheEnabled(false);
//          curTime = System.currentTimeMillis();
//          recordTime.add(curTime-startTime);
//          records.add(bitmap);
//        }


        gazePathView.onGaze(filtered[0], filtered[1], gazeInfo.eyeMovementState == EyeMovementState.FIXATION);
      }
    }
  };
}
