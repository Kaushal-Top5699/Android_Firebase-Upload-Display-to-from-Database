package com.example.this_is_kaushal.profile2;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmail;

    private EditText mName;

    private EditText mPhone;

    private EditText mPassword;

    private Button btnSignUp;

    private Button btnSignIn;

    private Button btnUpload;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference myRef;

    private String userID;

    private String email;

    private String name;

    private String phone_num;

    private ImageButton imageButton;

    private String password;

    private static final int GALLARY_ACCESS_CODE = 1;

    private Uri uri = null;

    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imageButton = (ImageButton) findViewById(R.id.imageBtn);

        mEmail = (EditText) findViewById(R.id.email);

        mName = (EditText) findViewById(R.id.textName);

        mPhone = (EditText) findViewById(R.id.textPhone);

        mPassword = (EditText) findViewById(R.id.password);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnUpload = (Button) findViewById(R.id.uploadBtn);

        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        setupFirebaseAuth();

        imageSetup();

        //uploadImage();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEmail.getText().toString();

                name = mName.getText().toString();

                phone_num = mPhone.getText().toString();

                password = mPassword.getText().toString();

                if (!email.equals("") || !password.equals("") || !name.equals("") || !phone_num.equals("")) {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Failed",
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                        userID = mAuth.getCurrentUser().getUid();

                                    }
                                }
                            });

                } else {

                    Toast.makeText(SignUpActivity.this, "Fields are empty, try again", Toast.LENGTH_SHORT).show();

                    return;

                }

            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

    /**
     * -------------------------------------Friebase Setup---------------------------------------------------*
     */

    //Method to setup firebase authentication

    private void setupFirebaseAuth() {

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser user = firebaseAuth.getCurrentUser();

                mFirebaseDatabase = FirebaseDatabase.getInstance();

                myRef = mFirebaseDatabase.getReference();

                if (user != null) {
                    // User is signed in
                    Toast.makeText(SignUpActivity.this, "Signed In", Toast.LENGTH_SHORT).show();

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            addNewUser(name,email, phone_num);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // User is signed out
                    Toast.makeText(SignUpActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };

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
    }/**
     * -------------------------------------Friebase Setup---------------------------------------------------*
     */

    //This method add new user to the database
    public void addNewUser(String email, String name, String phone_num) {

        /*OtherUsers otherUsers = new OtherUsers(name);
        myRef.child("users_names")
                .child(userID)
                .setValue(otherUsers); */

        //This is to update the users_account_settings node in database
        //For this we created a UserAccountSettings class
        UserInformation userInformation = new UserInformation(email, name, phone_num); //pass the default values of the user
        myRef.child("users")
                .child(userID)
                .setValue(userInformation); //Pass the object name

    }

    private void imageSetup() {

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent, GALLARY_ACCESS_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLARY_ACCESS_CODE && resultCode == RESULT_OK) {

            uri = data.getData();

            imageButton.setImageURI(uri);

        }
    }

   /* private void uploadImage() {

        StorageReference filePath = mStorageRef.child("ProfileImage").child(uri.getLastPathSegment());

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                Toast.makeText(SignUpActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

            }
        });

    } */
}
