package visual.camp.sample.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import visual.camp.sample.app.R;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        String taskId = intent.getStringExtra("taskId");
        String uemail = intent.getStringExtra("uemail");
        uemail = uemail.replace('.', ',');

        videoView = findViewById(R.id.videoView);

        // Initialize Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Get the reference to the video file
        StorageReference storageRef = storage.getReference().child(taskId).child(uemail).child("ss").child("screen_capture.mp4");

        // Create a temporary local file to store the video
        File localFile;
        try {
            localFile = File.createTempFile("tempVideo", "mp4");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Download the video file to the local file
        storageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Video file downloaded successfully
                        // Play the video using VideoView

                        // Set the media controller to enable play, pause, etc.
                        MediaController mediaController = new MediaController(VideoActivity.this);
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);

                        // Set the URI of the local file to play
                        videoView.setVideoURI(Uri.fromFile(localFile));

                        // Start playing the video
                        videoView.start();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        exception.printStackTrace();
                    }
                });
    }
}
