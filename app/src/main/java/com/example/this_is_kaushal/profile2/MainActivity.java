package com.example.this_is_kaushal.profile2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail;

    private EditText mPassword;

    private Button btnSignIn;

    private Button btnSignOut;

    private Button btnSignUp;

    private Button btnAddData;

    private Button btnViewData;

    private Button btnViewOthers;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmail = (EditText) findViewById(R.id.email);

        mPassword = (EditText) findViewById(R.id.password);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnSignOut = (Button) findViewById(R.id.btnSignOut);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnAddData = (Button) findViewById(R.id.btnAddData);

        btnViewData = (Button) findViewById(R.id.btnViewData);

        btnViewOthers = (Button) findViewById(R.id.btnViewOther);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("info", "onAuthStateChanged:signed_in:" + user.getEmail());

                    Toast.makeText(MainActivity.this, "Signed In, User: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                } else {
                    // User is signed out
                    Log.d("info", "onAuthStateChanged:signed_out");

                    Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();

                String password = mPassword.getText().toString();

                if (!email.equals("") || !password.equals("")) {

                    mAuth.signInWithEmailAndPassword(email, password);

                } else {

                    Toast.makeText(MainActivity.this, "Email or Password empty", Toast.LENGTH_SHORT).show();

                }

            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();

                mEmail.setText("");

                mPassword.setText("");

                Toast.makeText(MainActivity.this, "Signing Out...", Toast.LENGTH_SHORT).show();

            }
        });
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = mAuth.getCurrentUser();

                if (user == null) {

                    Toast.makeText(MainActivity.this, "You must login to update your Info", Toast.LENGTH_SHORT).show();

                    return;

                } else {

                    Intent intent = new Intent(MainActivity.this, UpdateDataActivity.class);

                    startActivity(intent);

                }

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent3 = new Intent(MainActivity.this, SignUpActivity.class);

                startActivity(intent3);

            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = mAuth.getCurrentUser();

                if (user == null) {

                    Toast.makeText(MainActivity.this, "You must login to view your Info", Toast.LENGTH_SHORT).show();

                    return;

                } else {

                    Intent intent2 = new Intent(MainActivity.this, ViewDataActivity.class);

                    startActivity(intent2);

                }
            }
        });

        btnViewOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = mAuth.getCurrentUser();

                if (user == null) {

                    Toast.makeText(MainActivity.this, "You must login to meet others", Toast.LENGTH_SHORT).show();

                    return;

                } else {

                    Intent intent4 = new Intent(MainActivity.this, OtherUsersActivity.class);

                    startActivity(intent4);

                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
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
