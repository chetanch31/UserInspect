package visual.camp.sample.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import visual.camp.sample.app.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView imageNameTextView;
    private Button previousButton, nextButton;
    private List<StorageReference> imageRefs;
    private int currentImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        Intent intent = getIntent();
        String taskId = intent.getStringExtra("taskId");
        String uemail = intent.getStringExtra("uemail");
        uemail = uemail.replace('.', ',');

        imageView = findViewById(R.id.imageView);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        imageNameTextView = findViewById(R.id.timeTextView);

        // Get a reference to the Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(taskId).child(uemail).child("ss");

        // Fetch the list of image references
        imageRefs = new ArrayList<>();
        storageRef.listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                if (task.isSuccessful()) {
                    for (StorageReference item : task.getResult().getItems()) {
                        imageRefs.add(item);
                    }
                    // Sort the image references based on their names
                    Collections.sort(imageRefs, new Comparator<StorageReference>() {
                        @Override
                        public int compare(StorageReference o1, StorageReference o2) {
                            String name1 = o1.getName();
                            String name2 = o2.getName();
                            Pattern p = Pattern.compile("\\d+");
                            Matcher m1 = p.matcher(name1);
                            Matcher m2 = p.matcher(name2);
                            if (m1.find() && m2.find()) {
                                int num1 = Integer.parseInt(m1.group());
                                int num2 = Integer.parseInt(m2.group());
                                return Integer.compare(num1, num2);
                            } else {
                                return name1.compareTo(name2);
                            }
                        }
                    });
                    // Load the first image if there are any images
                    if (imageRefs.size() > 0) {
                        loadImage(imageRefs.get(currentImageIndex));
                    } else {
                        // Otherwise, display a message indicating that there are no images
                        imageNameTextView.setText("No images found");
                    }
                } else {
                    // If there is no "ss" folder or there was an error fetching the list of image references,
                    // display a message indicating that there are no images
                    imageNameTextView.setText("No images found");
                }
            }
        });

        // Handle previous button click
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex > 0) {
                    currentImageIndex--;
                    loadImage(imageRefs.get(currentImageIndex));
                }
            }
        });

        // Handle next button click
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex < imageRefs.size() - 1) {
                    currentImageIndex++;
                    loadImage(imageRefs.get(currentImageIndex));
                }
            }
        });
    }

        private void loadImage(StorageReference imageRef) {
        // Set the text of the image name TextView to the name of the current image
        String imageName = "Time: "+imageRef.getName().replaceAll("\\.png$", "")+ "ms";

        imageNameTextView.setText(imageName);

        // Load the image from Firebase storage into the image view
        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    String downloadUrl = task.getResult().toString();
                    Picasso.get().load(downloadUrl).into(imageView);
                }
            }
        });
    }

}
