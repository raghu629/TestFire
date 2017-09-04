package com.raghu.testfire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.raghu.testfire.R;
import com.raghu.testfire.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 3001;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.emailEditText)
    TextInputEditText emailText;
    @BindView(R.id.passwordEditText)
    TextInputEditText passwordText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);

    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Snackbar.make(coordinatorLayout, TextUtils.isEmpty(result.getStatus().
                                getStatusMessage()) ? "Error" : result.getStatus().getStatusMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        LogUtil.d("FirebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            LogUtil.d("signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            LogUtil.i("User Info:\n" +
                                    (TextUtils.isEmpty(user.getDisplayName()) ? "N/A" : user.getDisplayName()) + "\n" +
                                    user.getEmail() + "\n" +
                                    user.getPhoneNumber());
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            LogUtil.d("signInWithCredential:failure: " + task.getException().getMessage());
                            Snackbar.make(coordinatorLayout, "Authentication failed.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
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
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            Snackbar.make(coordinatorLayout, getString(R.string.auth_failed),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
//            FirebaseUser firebaseUser = mAuth.getCurrentUser();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick({R.id.signInButton, R.id.googleView, R.id.faceBookView, R.id.twitterView})
    // must have to include view here to get onClickEvent
    public void onClickLogin(View view) {
        switch (view.getId()) {
            case R.id.signInButton:
                if (isValidate()) {
                    signInUser(emailText.getText().toString(), passwordText.getText().toString());
                }
                break;

            case R.id.googleView:
                googleSignIn();
                break;

            case R.id.faceBookView:
                break;

            case R.id.twitterView:
                break;
        }
    }


    public boolean isValidate() {
        boolean validate = false;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if (TextUtils.isEmpty(email))
            emailText.setError("Email required!");
        else if (TextUtils.isEmpty(password))
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtil.d("onConnectionFailed: " + connectionResult.getErrorMessage());
    }
}
