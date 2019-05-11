package com.example.qrbusiness.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrbusiness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LogInActivity";

    private FirebaseAuth mAuth;

    private Button btnSignIn, btnSignUp;
    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        init();
    }

    private void init()
    {
        this.editTextEmail = findViewById(R.id.EmailEditText);
        this.editTextPassword = findViewById(R.id.PasswordEditText);
        this.btnSignIn = findViewById(R.id.SigninBtn);
        this.btnSignUp = findViewById(R.id.SignupBtn);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user)
    {
        if (user != null)
        {
            Intent intent = new Intent(this, LibraryActivity.class);
            startActivity(intent);
        }

    }

    public void signIn(View v)
    {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.length() == 0 || password.length() == 0)
        {
            Toast.makeText(this, "Email or password is missing.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }
    }

    public void signUp(View v)
    {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.length() == 0 || password.length() == 0)
        {
            Toast.makeText(this, "Email or password is missing.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
