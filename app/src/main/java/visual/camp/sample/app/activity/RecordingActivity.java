package visual.camp.sample.app.activity;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import visual.camp.sample.app.R;

public class RecordingActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;

    private ScreenRecorder screenRecorder;
    private Button startButton;
    private Button stopButton;
    private static final String CHANNEL_ID = "scapp_channel";
    private static final String CHANNEL_NAME = "SCApp Channel";
    private static final String CHANNEL_DESCRIPTION = "SCApp Notification Channel";
    private static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
    String link, taskId, appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        startButton = findViewById(R.id.start_recording_button);
        stopButton = findViewById(R.id.stop_recording_button);

        link = getIntent().getStringExtra("link");
        taskId = getIntent().getStringExtra("taskId");
        appName = getIntent().getStringExtra("appName");

        createNotificationChannel(this, CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESCRIPTION, CHANNEL_IMPORTANCE);

        screenRecorder = new ScreenRecorder(this);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScreenRecording();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = stopScreenRecording();
                Toast.makeText(RecordingActivity.this, "Path: " + path, Toast.LENGTH_SHORT).show();
            }
        });
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
    protected void onStop() {
        super.onStop();

        if (screenRecorder.isRecording()) {
            stopScreenRecording();
        }
    }

    public static void createNotificationChannel(Context context, String channelId, String channelName, String channelDescription, int channelImportance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, channelImportance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
