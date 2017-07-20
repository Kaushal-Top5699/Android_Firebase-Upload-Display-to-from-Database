package com.example.this_is_kaushal.profile2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDataActivity extends AppCompatActivity {

    private EditText mName;

    private EditText mEmail;

    private EditText mPhone;

    private Button btnUpdateData;

    private String userID;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        mName = (EditText) findViewById(R.id.textName);

        mEmail = (EditText) findViewById(R.id.textEmail);

        mPhone = (EditText) findViewById(R.id.textPhone);

        btnUpdateData = (Button) findViewById(R.id.btnUpdate);

        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference();

        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("info", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.i("info", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("info", "onAuthStateChanged:signed_in:" + dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mName.getText().toString();

                String email = mEmail.getText().toString();

                String phoneNum = mPhone.getText().toString();

                if (!name.equals("") || !email.equals("") || !phoneNum.equals("")) {

                    UserInformation userInformation = new UserInformation(name, email, phoneNum);

                    myRef.child("users").child(userID).setValue(userInformation);

                    Toast.makeText(UpdateDataActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                    mName.setText("");

                    mEmail.setText("");

                    mPhone.setText("");

                } else {

                    Toast.makeText(UpdateDataActivity.this, "Fields are Empty, Try again", Toast.LENGTH_SHORT).show();

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
