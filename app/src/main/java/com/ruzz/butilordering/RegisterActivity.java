package com.ruzz.butilordering;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruzz.butilordering.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {
    private ActivityRegisterBinding binding;
    private FirebaseAuth appAuthentication;
    private FirebaseFirestore database;
    private String genderSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        appAuthentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        final Button register = binding.registerButton;
        final Spinner genderSpinner = binding.gender;

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.Gender,
                android.R.layout.simple_spinner_dropdown_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setOnItemSelectedListener(this);
        genderSpinner.setAdapter(spinnerAdapter);

        register.setOnClickListener(v -> {
            createAccount();
        });

        binding.linkToLogin.setOnClickListener(v -> {
            goToLoginActivity();
        });

        setContentView(view);

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = appAuthentication.getCurrentUser();

        if (currentUser != null) {
            goToHomeActivity();
        }
    }

    private void createAccount() {
        String email = binding.txtEmail.getText().toString();
        String password = binding.password.getText().toString();
        String confirmPass = binding.condirmPassword.getText().toString();
        String contact = binding.contactNUmber.getText().toString();
        String firstName = binding.firstName.getText().toString();
        String lastName = binding.lastName.getText().toString();

        if (!password.equals(confirmPass)) {
            Toast.makeText(RegisterActivity.this, "Password does not match.",
                    Toast.LENGTH_SHORT).show();
        }

        appAuthentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = appAuthentication.getCurrentUser();
                        addUserInformation(contact, this.genderSelected, firstName, lastName, user.getUid());
                    } else {
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(RegisterActivity.this, "Weak password.",
                                    Toast.LENGTH_SHORT).show();
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(RegisterActivity.this, "Invalid email.",
                                    Toast.LENGTH_SHORT).show();
                        } catch(FirebaseAuthUserCollisionException e) {
                            Toast.makeText(RegisterActivity.this, "User already exists.",
                                    Toast.LENGTH_SHORT).show();
                        } catch(Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserInformation(String contact, String gender, String firstName, String lastName, String userUid) {
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("gender", gender);
        user.put("contact", contact);

        database.collection("accounts")
                .document(userUid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        goToLoginActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.genderSelected = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}