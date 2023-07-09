package visual.camp.sample.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import visual.camp.sample.app.R;

public class IncompleteActivity extends AppCompatActivity {

    private Switch switchCompletionTime, switchWebsiteUrl;
    private EditText editTextOther;
    private Button btnSubmit;

    private boolean isCompletionTimeLess, isWebsiteUrlBroken;
    private String otherIssue;

    private DatabaseReference mDatabase;
    String devUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomplete);

        // get the task ID from the intent
        String taskId = getIntent().getStringExtra("taskId");
        String appName = getIntent().getStringExtra("appName");

        // get the current user's email from Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();

        switchCompletionTime = findViewById(R.id.switch_completion_time);
        switchWebsiteUrl = findViewById(R.id.switch_website_url);
        editTextOther = findViewById(R.id.edittext_other);
        btnSubmit = findViewById(R.id.btn_submit);

        // get the Firebase Realtime Database instance




        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCompletionTimeLess = switchCompletionTime.isChecked();
                isWebsiteUrlBroken = switchWebsiteUrl.isChecked();
                otherIssue = editTextOther.getText().toString().trim();

                // create a new FeedbackModel object
                FeedbackModel feedbackModel = new FeedbackModel(email, taskId, isCompletionTimeLess, isWebsiteUrlBroken, otherIssue);

                mDatabase = FirebaseDatabase.getInstance().getReference();
                // get a unique key for the new feedback entry
                String key = mDatabase.child("feedback").push().getKey();

                // add the feedback to the database at the generated key
                mDatabase.child("feedback").child(key).setValue(feedbackModel);


                if(isWebsiteUrlBroken){

                    DatabaseReference aref = FirebaseDatabase.getInstance().getReference("apps").child(appName).child("verified");

                    aref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Integer verified = snapshot.getValue(Integer.class);
                                if(verified<2){

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("apps").child(appName).child("email");
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            devUser = dataSnapshot.getValue(String.class);
                                            String devKey=devUser.replace(".", ",");

                                            // Create the NotificationModel object
                                            NotificationModel notificationModel = new NotificationModel(
                                                    appName,
                                                    false,
                                                    taskId,
                                                    email,
                                                    devUser                            ,
                                                    System.currentTimeMillis()
                                            );

                                            // Save the NotificationModel to the database
                                            DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("notifications").child(devKey).child(Long.toString(System.currentTimeMillis()));
                                            notificationRef.setValue(notificationModel);
                                            aref.setValue(0);
                                            // You can use the devUser value here as needed
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Handle any errors that occur
                                        }
                                    });


                                }

                                // Do something with the value of "verified"
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the error
                        }
                    });


                }

                // Show a toast message to confirm submission
                Toast.makeText(IncompleteActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(IncompleteActivity.this, HomeActivity.class);
                startActivity(intent);

                // close the activity
                finish();
            }
        });
    }
}

