package com.raghu.testfire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.raghu.testfire.R;
import com.raghu.testfire.app.Constants;
import com.raghu.testfire.utils.LogUtil;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        isUserSignOrNot();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void isUserSignOrNot() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            // User is signed in
                            LogUtil.d("onAuthStateChanged:signed_in:" + user.getUid());
                            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            // User is signed out
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                            LogUtil.d("onAuthStateChanged:signed_out");
                        }

                    }
                }, Constants.SPLASH_TIME);

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null)
            mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
