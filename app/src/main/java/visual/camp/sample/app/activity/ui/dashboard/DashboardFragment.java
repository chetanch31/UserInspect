package visual.camp.sample.app.activity.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.LoginActivity;
import visual.camp.sample.app.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEmail;
        final TextView displayNameTextView = binding.displayNameTextView;
        final Button changePasswordButton = binding.changePasswordButton;
        final Button logoutButton = binding.logoutButton;

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Set the dashboard text to show the user's email address
            String userEmail = currentUser.getEmail();
            textView.setText("Logged in as: " + userEmail);

            // Set the display name text to show the user's display name
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                displayNameTextView.setText("Display Name: " + displayName);
            } else {
                displayNameTextView.setText("Display Name: Not Set");
            }

            // Set a click listener for the change password button
            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Send a password reset email to the user's email address
                    mAuth.sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(),
                                                "Password reset email sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(),
                                                "Failed to send password reset email",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            // Set a click listener for the logout button
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Log out the user and go back to the login screen
                    mAuth.signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}