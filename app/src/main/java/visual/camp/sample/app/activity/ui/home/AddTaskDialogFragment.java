package visual.camp.sample.app.activity.ui.home;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.ResultModel;
import visual.camp.sample.app.activity.TaskModel;

public class AddTaskDialogFragment extends DialogFragment {

    private EditText taskNameEditText;
    private EditText taskDescriptionEditText;
    private String userEmail;
    private String appName;

    private OnTaskAddedListener mCallback;

    public interface OnTaskAddedListener {
        void onTaskAdded(String taskName, String taskDescription);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnTaskAddedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTaskAddedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_task_dialog_fragment, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        userEmail = auth.getCurrentUser().getEmail();

        // Get references to the EditText views
        taskNameEditText = view.findViewById(R.id.task_name_edittext);
        taskDescriptionEditText = view.findViewById(R.id.task_description_edittext);

        // Set the dialog title
        getDialog().setTitle("Add new task");

        // Get the app name from the bundle arguments
        Bundle args = getArguments();
        appName = args.getString("appName");

        // Set a listener for the "Add" button
        Button addButton = view.findViewById(R.id.add_task_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user input

                String taskName = taskNameEditText.getText().toString().trim();
                String taskDescription = taskDescriptionEditText.getText().toString().trim();

                // Check if task name is blank or has more than 10 characters
                if (taskName.isEmpty()) {
                    taskNameEditText.setError("Task name cannot be blank");
                    return;
                }
                if (taskName.length() > 10) {
                    taskNameEditText.setError("Task name should have maximum 10 characters");
                    return;
                }

                if (!taskName.matches("^[a-zA-Z0-9]*$")) {
                    taskNameEditText.setError("Task name can only contain alphabets and numbers");
                    return;
                }

                // Check if task description is blank or has more than 100 characters
                if (taskDescription.isEmpty()) {
                    taskDescriptionEditText.setError("Task description cannot be blank");
                    return;
                }

                if (taskDescription.length() > 100) {
                    taskDescriptionEditText.setError("Task description should have maximum 100 characters");
                    return;
                }

                // Save the new task data
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference taskRef = database.getReference("apps").child(appName).child("tasks");
                Map<String, ResultModel> results = new HashMap<>();
                String taskKey = taskRef.push().getKey();
                TaskModel taskModel = new TaskModel(taskKey, taskName, taskDescription, results);
                taskRef.child(taskKey).setValue(taskModel);

//                final DatabaseReference aRef = FirebaseDatabase.getInstance().getReference().child("Analysis").child(taskKey);
//                aRef.child("valid").setValue("true");


//                DatabaseReference resultRef = database.getReference("apps").child(appName).child("tasks").child(taskKey).child("results");
//                String resultKey=resultRef.push().getKey();
//                Map<Long, Point> eyedata=new HashMap<>();
//                ResultModel resultModel=new ResultModel(resultKey,"test@email.com", "resultlink.in", eyedata  );
//                resultRef.child(resultKey).setValue(resultModel);

                // Dismiss the dialog
                dismiss();
            }
        });

        return view;
    }
}
