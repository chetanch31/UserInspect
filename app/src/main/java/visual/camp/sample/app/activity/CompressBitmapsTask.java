package visual.camp.sample.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class CompressBitmapsTask extends AsyncTask<Pair<String, String>, Void, Void> {

    private Context mContext;
    private String mUserEmail;
    private String type;

    public CompressBitmapsTask(Context context, String userEmail, String type) {
        mContext = context;
        mUserEmail = userEmail;
        this.type=type;
    }

    @Override
    protected Void doInBackground(Pair<String, String>... pairs) {
        String path = pairs[0].first;
        String taskId = pairs[0].second;

        // Firebase storage setup
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(taskId).child(mUserEmail).child(type);

        // Create a reference to the video file
        File videoFile = new File(path);
        Uri videoUri = Uri.fromFile(videoFile);

        // Set the file name for storage reference
        String fileName = videoFile.getName();
        StorageReference videoRef = storageRef.child(fileName);

        // Upload the video file to Firebase Storage
        UploadTask uploadTask = videoRef.putFile(videoUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Video uploaded successfully
            // Get the download URL
            videoRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                String downloadUrlString = downloadUrl.toString();
                // Perform any necessary actions with the download URL
                Log.d("TAG", "Video uploaded successfully. Download URL: " + downloadUrlString);
            }).addOnFailureListener(e -> {
                // Error occurred while getting the download URL
                Log.e("TAG", "Failed to get download URL: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            // Error occurred while uploading the video
            Log.e("TAG", "Failed to upload video: " + e.getMessage());
        });

        return null;
    }


}

