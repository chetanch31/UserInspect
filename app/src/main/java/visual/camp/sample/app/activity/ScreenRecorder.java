package visual.camp.sample.app.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.IOException;

public class ScreenRecorder {
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    private int screenDensity;
    private int screenWidth;
    private int screenHeight;
    private String outPath;
    public ScreenRecorder(Context context) {
        mediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenDensity = metrics.densityDpi;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    public void startRecording(int resultCode, Intent data, String outputPath) {
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);

        outPath = outputPath;

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setVideoSize(screenWidth, screenHeight);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoEncodingBitRate(1024 * 1024 * 10);
        mediaRecorder.setOutputFile(outputPath);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        virtualDisplay = mediaProjection.createVirtualDisplay("ScreenRecorder",
                screenWidth, screenHeight, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(), null, null);

        mediaRecorder.start();
        isRecording = true;
    }

    public String stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            virtualDisplay.release();
            mediaProjection.stop();
            isRecording = false;

        }

        return outPath;
    }

    public boolean isRecording() {
        return isRecording;
    }
}
