package com.raghu.testfire.activity;

import android.content.Intent;
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
import com.raghu.testfire.R;
import com.raghu.testfire.utils.LogUtil;
import com.raghu.testfire.utils.Utils;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextInputEditText emailText;
    private TextInputEditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        emailText = (TextInputEditText) findViewById(R.id.emailEditText);
        passwordText = (TextInputEditText) findViewById(R.id.passwordEditText);

        findViewById(R.id.signUpButton).setOnClickListener(this);
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

    private void signUpNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        LogUtil.d("createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        }

                        // ...
                    }
                });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpButton:
                if (isValidate()) {
                    signUpNewUser(emailText.getText().toString(), passwordText.getText().toString());
                }
                break;
        }
    }
}
