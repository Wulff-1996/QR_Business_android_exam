package com.example.qrbusiness.controller.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrbusiness.R;
import com.example.qrbusiness.controller.Navigation.NavigationActivity;
import com.example.qrbusiness.model.Firestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LogInActivity";

    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        init();
    }

    private void init()
    {
        this.email = findViewById(R.id.login_email);
        this.password = findViewById(R.id.login_password);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = Firestore.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user)
    {
        if (user != null)
        {
            String email = user.getEmail();
            Bundle b = new Bundle();
            b.putString("EMAIL", email);
            Intent intent = new Intent(this, NavigationActivity.class);
            intent.putExtra("BUNDLE", b);
            startActivity(intent);
        }

    }

    public void signIn(View v)
    {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (email.length() == 0 || password.length() == 0)
        {
            Toast.makeText(this, "Email or password is missing.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Firestore.getFirebaseAuth().signInWithEmailAndPassword(this.email.getText().toString(), this.password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = Firestore.getCurrentUser();
                                updateUI(user);
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void signUp(View v)
    {
        final String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (email.length() == 0 || password.length() == 0)
        {
            Toast.makeText(this, "Email or password is missing.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Firestore.getFirebaseAuth().createUserWithEmailAndPassword(this.email.getText().toString(), this.password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = Firestore.getCurrentUser();
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
