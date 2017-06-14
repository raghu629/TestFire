package com.raghu.testfire.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.raghu.testfire.R;
import com.raghu.testfire.utils.LogUtil;
import com.raghu.testfire.utils.Utils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextInputEditText emailText;
    private TextInputEditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        emailText = (TextInputEditText) findViewById(R.id.emailEditText);
        passwordText = (TextInputEditText) findViewById(R.id.passwordEditText);

        findViewById(R.id.signInButton).setOnClickListener(this);

    }

    private void isUserSignedIn() {
          /*In the onCreate() method, initialize the FirebaseAuth instance and the AuthStateListener
         method so you can track whenever the user signs in or out.*/
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    LogUtil.d("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    LogUtil.d("onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        LogUtil.d("signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            LogUtil.d("signInWithEmail:failed: " + task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
//            LogUtil.d("Name: " +name +"\n" +
//            email);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:
                if (isValidate()) {
                    signInUser(emailText.getText().toString(), passwordText.getText().toString());
                }
                break;
        }
    }

    public boolean isValidate() {
        boolean validate = false;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if (Utils.isEmpty(email))
            emailText.setError("Email required!");
        else if (Utils.isEmpty(password))
            passwordText.setError("Password Required!");
        else if (passwordText.length() < 6)
            passwordText.setError("Minimum 6 digits required!");
        else
            validate = true;
        return validate;
    }

    public void onSignUpClick(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
