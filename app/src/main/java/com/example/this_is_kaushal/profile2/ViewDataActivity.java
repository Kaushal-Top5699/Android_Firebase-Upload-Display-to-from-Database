package com.example.this_is_kaushal.profile2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDataActivity extends AppCompatActivity {

    private static final String TAG = "ViewDataActivity";

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference myRef;

    private String userID;

    //private ListView listView;

    private TextView outputName;

    private TextView outputEmail;

    public static String STR;

    private TextView outputPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

       // listView = (ListView) findViewById(R.id.listView);

        outputName = (TextView) findViewById(R.id.outputName);

        outputEmail = (TextView)findViewById(R.id.outputEmail);

        outputPhone = (TextView) findViewById(R.id.outputPhone);

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

                showData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds: dataSnapshot.getChildren()) {

            UserInformation userInformation = new UserInformation();

            userInformation.setName(ds.child(userID).getValue(UserInformation.class).getName());

            userInformation.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail());

            userInformation.setPhone_num(ds.child(userID).getValue(UserInformation.class).getPhone_num());

            Log.d(TAG, "Name: "+userInformation.getName());

            STR = userInformation.getName();

            Log.d(TAG, "Email: "+userInformation.getEmail());

            Log.d(TAG, "Phone_Num"+userInformation.getPhone_num());

            outputName.setText(userInformation.getName());

            outputEmail.setText(userInformation.getEmail());

            outputPhone.setText(userInformation.getPhone_num());


          /*  ArrayList<String> array = new ArrayList<>();

            array.add(userInformation.getName());

            array.add(userInformation.getEmail());

            array.add(userInformation.getPhone_num());

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);

            listView.setAdapter(adapter); */

        }

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
