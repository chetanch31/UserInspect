package visual.camp.sample.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import visual.camp.sample.app.R;

//firebase imports
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            if (currentUser.isEmailVerified()) {
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                // Login successful
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.registerProgressBar);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }
    private void registerUser() {
        final String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        radioGroup = findViewById(R.id.roleRadioGroup);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (name.isEmpty()) {
            nameEditText.setError("Name is required!");
            nameEditText.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required!");
            passwordEditText.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Confirm password is required!");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match!");
            confirmPasswordEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            final String role;
                            if (selectedId == R.id.developerRadioButton) {
                                role = "dev";
                            } else {
                                role = "test";
                            }
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        DatabaseReference userRef = database.getReference("users");
                                                        UserModel userModel = new UserModel(email, name, role, false);
                                                        String email=userModel.getEmail();
                                                        String key = email.replace(".", ",");
                                                        userRef.child(key).setValue(userModel);
                                                        Toast.makeText(RegisterActivity.this, "Please check your email for verification.", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });

                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(RegisterActivity.this, "Registration failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}