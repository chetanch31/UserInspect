package visual.camp.sample.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import visual.camp.sample.app.R;

public class EyeActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye);

        listView = findViewById(R.id.list_view);
        Intent intent = getIntent();
        Map<String, Map<String, Float>> eyeData = (Map<String, Map<String, Float>>) intent.getSerializableExtra("eyeData");

        Map<Long, Map<String, Float>> longEyeData = new HashMap<>();
        for (Map.Entry<String, Map<String, Float>> entry : eyeData.entrySet()) {
            Long key = Long.valueOf(entry.getKey());
            Map<String, Float> value = entry.getValue();
            longEyeData.put(key, value);
        }

        Map<Long, Map<String, Float>> longEyeData2 = new TreeMap<>(longEyeData);

        EyeDataAdapter adapter = new EyeDataAdapter(this, longEyeData2);
        listView.setAdapter(adapter);
    }
}
