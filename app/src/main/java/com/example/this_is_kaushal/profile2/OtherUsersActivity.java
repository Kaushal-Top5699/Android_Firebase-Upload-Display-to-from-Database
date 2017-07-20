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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OtherUsersActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference myRef;

    private String userID;

    //private TextView data;
    private ListView mListView;

    ArrayList<String> array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_users);

        mListView = (ListView) findViewById(R.id.listView);
        //data = (TextView)findViewById(R.id.textData);

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

        //to fetch all the users of firebase Auth app
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String name = ds.child("name").getValue(String.class);

                            Log.d("TAG", name);

                            array.add(name);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter(OtherUsersActivity.this, android.R.layout.simple_list_item_1, array);

                mListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
    }

    /* private void showData(DataSnapshot dataSnapshot) {

         DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

         DatabaseReference usersdRef = rootRef.child("users");

        for (DataSnapshot ds: dataSnapshot.getChildren()) {

            ArrayList<String> array = new ArrayList<>();

            array.add(name);

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);

            mListView.setAdapter(adapter);

        }

    } */

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
