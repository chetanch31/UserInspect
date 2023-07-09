package visual.camp.sample.app.activity.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.AppModel;
import visual.camp.sample.app.activity.TaskModel;

public class TaskActivity extends AppCompatActivity implements AddTaskDialogFragment.OnTaskAddedListener {
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        userEmail = auth.getCurrentUser().getEmail();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final String appName = getIntent().getStringExtra("appName");
        final String role = getIntent().getStringExtra("role");
        final String link = getIntent().getStringExtra("link");
//        Toast.makeText(TaskActivity.this, role, Toast.LENGTH_SHORT).show();


        if(role.equals("dev")){
            Button addTaskButton = findViewById(R.id.add_task_button);
            addTaskButton.setVisibility(View.VISIBLE);
            addTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openAddTaskDialog(appName);
                }
            });
        }
        else if(role.equals("test")){
            Button addTaskButton = findViewById(R.id.add_task_button);
            addTaskButton.setVisibility(View.GONE);
        }



        final RecyclerView recyclerView=findViewById(R.id.task_list_recycler_view);
        // Set up the RecyclerView with the LinearLayout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference("apps").child(appName).child("tasks");

        final ArrayList<TaskModel> taskList = new ArrayList<>();

        // Attach a ValueEventListener to the appsRef reference
        ValueEventListener tasksListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the appList before adding new apps to avoid duplication
                taskList.clear();

                // Loop through all the children of the "apps" node
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    // Get the App object from the appSnapshot and add it to the appList
                    TaskModel task = taskSnapshot.getValue(TaskModel.class);
                    taskList.add(task);

                }
                Log.i("taskslist", taskList.toString());
                RecyclerTaskAdapter taskAdapter=new RecyclerTaskAdapter(TaskActivity.this, taskList, role, link, tasksRef, appName, userEmail);
                recyclerView.setAdapter(taskAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        };

// Attach the ValueEventListener to the appsRef reference
        tasksRef.addValueEventListener(tasksListener);


    }
    private void openAddTaskDialog(String appName) {
        AddTaskDialogFragment newTaskDialogFragment = new AddTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("appName", appName);
        newTaskDialogFragment.setArguments(args);
        newTaskDialogFragment.show(getSupportFragmentManager(), "AddTaskDialogFragment");
    }

    @Override
    public void onTaskAdded(String taskName, String taskDescription) {

    }

}