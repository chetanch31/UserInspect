package visual.camp.sample.app.activity.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//url safe import
//import visual.camp.sample.app.activity.ui.home.CheckUrlTask;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.AppModel;
import visual.camp.sample.app.activity.RegisterActivity;
import visual.camp.sample.app.activity.ResultModel;
import visual.camp.sample.app.activity.TaskModel;

public class AddAppDialogFragment extends DialogFragment {

    private EditText appNameEditText;
    private EditText appLinkEditText;
    private String userEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_app_dialog_fragment, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        userEmail = auth.getCurrentUser().getEmail();

        // Get references to the EditText views
        appNameEditText = view.findViewById(R.id.edit_text_app_name);
        appLinkEditText = view.findViewById(R.id.edit_text_app_link);

        // Set the dialog title
        getDialog().setTitle("Add new app");

        // Set a listener for the "Add" button
        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user input
                String appName = appNameEditText.getText().toString().trim();
                String appLink = appLinkEditText.getText().toString().trim();

// Check for empty app name
                if (appName.isEmpty()) {
                    appNameEditText.setError("Please enter app name");
                    return;
                }

// Check for empty app link
                if (appLink.isEmpty()) {
                    appLinkEditText.setError("Please enter app link");
                    return;
                }

// Limit app name and app link to 50 characters
                if (appName.length() > 10) {
                    appNameEditText.setError("App name should be maximum 10 characters");
                    return;
                }

                if (appLink.length() > 50) {
                    appLinkEditText.setError("App link should be maximum 50 characters");
                    return;
                }

                if (!Patterns.WEB_URL.matcher(appLink).matches()) {
                    // Show an error message to the user and return
                    appLinkEditText.setError("Please enter a valid app link");
                    return;
                }

                if (!appName.matches("^[a-zA-Z0-9]*$")) {
                    appNameEditText.setError("App name can only contain alphabets and numbers");
                    return;
                }

                // Check for invalid characters in app name
                if (appName.contains(".") || appName.contains("#") || appName.contains("$") ||
                        appName.contains("[") || appName.contains("]")) {
                    appNameEditText.setError("App name cannot contain '.', '#', '$', '[', or ']'");
                    return;
                }
                // Check that app name doesn't start with a number
                if (Character.isDigit(appName.charAt(0))) {
                    appNameEditText.setError( "App name can't start with a number");
                    return;
                }








                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference appRef = database.getReference("apps");



                appRef.child(appName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // App with this name already exists in the database, show an error message to the user and return
//                            Toast.makeText(getContext(), "App name already exists", Toast.LENGTH_SHORT).show();
                            appNameEditText.setError("App name already exists");
                            return;
                        } else {
                            // App with this name doesn't exist in the database, save the new app data
                            Map<String, TaskModel> tasks = new HashMap<>();
                            AppModel appModel = new AppModel(userEmail, appName, appLink, 1, true, tasks);
                            appRef.child(appName).setValue(appModel);
                            // Dismiss the dialog
                            dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error
                    }
                });
            }
        });

        return view;
    }
}

