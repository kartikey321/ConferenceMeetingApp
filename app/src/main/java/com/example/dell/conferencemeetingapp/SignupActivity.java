package com.example.dell.conferencemeetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.dell.conferencemeetingapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        binding.tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
        binding.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.fullname.getText().toString().isEmpty()) {
                    binding.fullname.setError("Please enter your name");
                    binding.fullname.requestFocus();
                    return;
                }

                if (binding.emailBox.getText().toString().isEmpty()) {
                    binding.emailBox.setError("Please enter email");
                    binding.emailBox.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher( binding.emailBox.getText().toString()).matches()) {
                    binding.emailBox.setError("Please enter valid email");
                    binding.emailBox.requestFocus();
                    return;
                }

                if ( binding.passwordBox.getText().toString().isEmpty()) {
                    binding.passwordBox.setError("Please enter password");
                    binding.passwordBox.requestFocus();
                    return;
                }
                String email=binding.emailBox.getText().toString();
                String password= binding.passwordBox.getText().toString();
                User user = new User();
                user.setName(binding.fullname.getText().toString());
                user.setEmail(email);
                user.setPass(password);
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    database.collection("Users")
                                            .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Toast.makeText(SignupActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}