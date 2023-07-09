package visual.camp.sample.app.activity.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.AnalysisModel;

public class AnalysisActivity extends AppCompatActivity {
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        // Retrieve the extras from the Intent
        Intent intent = getIntent();
        String taskId = intent.getStringExtra("taskId");
        String taskName = intent.getStringExtra("taskName");
        String taskDescription = intent.getStringExtra("taskDesc");
        String link = intent.getStringExtra("link");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        userEmail = auth.getCurrentUser().getEmail();
        String key = userEmail.replace(".", ",");

        final RecyclerView recyclerView=findViewById(R.id.analysis_list_recycler_view);
        // Set up the RecyclerView with the LinearLayout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final DatabaseReference aRef = FirebaseDatabase.getInstance().getReference().child("Analysis").child(taskId);

        aRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<AnalysisModel> analysisList = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    String email = userSnapshot.child("email").getValue(String.class);
                    String name = userSnapshot.child("name").getValue(String.class);
                    String time = userSnapshot.child("time").getValue(String.class);
                    String userFeedback = userSnapshot.child("userFeedback").getValue(String.class);
                    Map<String, Map<String, Float>> eyeData=(Map<String, Map<String, Float>>)userSnapshot.child("eyeData").getValue();

                    AnalysisModel analysis = new AnalysisModel(email, name, time, userFeedback, eyeData);
                    analysisList.add(analysis);

                    // Do whatever you need to do with the user data here
                }
                RecyclerAnalysisAdapter aAdapter=new RecyclerAnalysisAdapter(AnalysisActivity.this, analysisList, taskId);
                recyclerView.setAdapter(aAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });

    }
}