package visual.camp.sample.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import visual.camp.sample.app.R;

public class EndActivity extends AppCompatActivity {
    private Button btnYes, btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        btnYes = findViewById(R.id.btn_yes);
        btnNo = findViewById(R.id.btn_no);

        // get the task ID from the intent
        String taskId = getIntent().getStringExtra("taskId");
        String appName = getIntent().getStringExtra("appName");
        ArrayList<Long> ssTime = (ArrayList<Long>) getIntent().getSerializableExtra("ssTime");
//        ArrayList<Long> recordTime = (ArrayList<Long>) getIntent().getSerializableExtra("recordTime");

        // Get the intent
        Intent intent = getIntent();
        String totalTime=getIntent().getStringExtra("totalTime");
        Map eyeData = (Map<Long, PointF>) intent.getSerializableExtra("eyeData");


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndActivity.this, FeedbackActivity.class);
                intent.putExtra("eyeData", (Serializable) eyeData);
                intent.putExtra("totalTime", totalTime);
                intent.putExtra("taskId", taskId);
                intent.putExtra("appName", appName);
                intent.putExtra("ssTime", ssTime);
//                intent.putExtra("recordTime", recordTime);
                startActivity(intent);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndActivity.this, IncompleteActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("appName", appName);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(EndActivity.this, "Can't go back.", Toast.LENGTH_SHORT).show();
    }
}
