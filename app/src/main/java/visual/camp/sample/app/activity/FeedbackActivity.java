package visual.camp.sample.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import visual.camp.sample.app.R;

public class FeedbackActivity extends AppCompatActivity {
    String taskId, ss, userEmail,userName;
    Map<Long, PointF> eyeData;
    String totalTime;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Get the intent
        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
        totalTime=getIntent().getStringExtra("totalTime");
        ArrayList<Bitmap> screenshots = BitmapArrayListSingleton.getInstance().getBitmapArrayList();
//        ArrayList<Bitmap> records = BitmapArrayListSingleton2.getInstance().getBitmapArrayList();
        ArrayList<Long> ssTime = (ArrayList<Long>) getIntent().getSerializableExtra("ssTime");
//        ArrayList<Bitmap> screenshots =new ArrayList<>();
//        Toast.makeText(FeedbackActivity.this, screenshots.size()+":"+ssTime.size(), Toast.LENGTH_SHORT).show();

// Get the Map object from the intent extra with key "EYE_DATA"
        eyeData = (Map<Long, PointF>) intent.getSerializableExtra("eyeData");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving screenshots...");
        progressDialog.setCancelable(false);


        Button submitButton = findViewById(R.id.submit_button);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button privacyButton = findViewById(R.id.privacy_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressDialog.show();
                // TODO: Handle submit button click event
                Collections.sort(ssTime);

                Map<Long, PointF> sortedMap = new TreeMap<>(eyeData);
                Map<String, Map<String, Float>> serializedEyeData = new HashMap<>();
                for (Map.Entry<Long, PointF> entry : eyeData.entrySet()) {
                    Long key = entry.getKey();
                    PointF value = entry.getValue();
                    String serializedKey = Long.toString(key);
                    Map<String, Float> serializedValue = new HashMap<>();
                    serializedValue.put("x", value.x);
                    serializedValue.put("y", value.y);
                    serializedEyeData.put(serializedKey, serializedValue);
                }

                EditText feedbackEditText = findViewById(R.id.feedback_edittext);
                String feedback = feedbackEditText.getText().toString();

                final DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference().child("Analysis").child(taskId);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                userEmail = auth.getCurrentUser().getEmail();
                userName=auth.getCurrentUser().getDisplayName();
                String key = userEmail.replace(".", ",");
                AnalysisModel analysis = new AnalysisModel(userEmail, userName, totalTime, feedback, serializedEyeData);
                taskRef.child(key).setValue(analysis);

//                CompressBitmapsTask task = new CompressBitmapsTask(FeedbackActivity.this, key, "ss", ssTime);
//                task.execute(new Pair<>(screenshots, taskId));

                String path = getFilesDir() + "/screen_capture.mp4";
                CompressBitmapsTask task = new CompressBitmapsTask(FeedbackActivity.this, key, "ss");
                task.execute(new Pair<>(path, taskId));


//                CompressBitmapsTask task2 = new CompressBitmapsTask(FeedbackActivity.this, key,"record", ssTime);
//                task2.execute(new Pair<>(records, taskId));




//                FirebaseStorage storage = FirebaseStorage.getInstance();
//                StorageReference storageRef = storage.getReference();
//                Uri file = Uri.fromFile(new File(getFilesDir(), "eye_data.txt"));
//                StorageReference fileRef = storageRef.child("files/" + file.getLastPathSegment());
//                UploadTask uploadTask = fileRef.putFile(file);

                Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
                startActivity(intent);
                // Code to submit the result goes here
                // Show a toast or dialog to indicate successful submission
                Toast.makeText(FeedbackActivity.this, "Result submitted successfully!", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Handle cancel button click event
                // Code to cancel the submission goes here
                // Show a toast or dialog to indicate cancellation
                Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
                startActivity(intent);
                Toast.makeText(FeedbackActivity.this, "Submission cancelled!", Toast.LENGTH_SHORT).show();
                finish(); // close the activity
            }
        });

        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrivacyDialog();
            }
        });



    }

    public void showPrivacyDialog() {
        // Create a new AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the dialog_privacy layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_privacy, null);

        // Get references to the TextView and Button in the layout
        TextView textPrivacy = dialogView.findViewById(R.id.text_privacy);
        Button buttonOk = dialogView.findViewById(R.id.button_ok);

        // Set the text for the TextView
        // textPrivacy.setText(R.string.privacy_policy);

        // Set the dialog view and show the dialog
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Set an OnClickListener for the "OK" button
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}