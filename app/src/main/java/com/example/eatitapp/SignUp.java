package com.example.eatitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eatitapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText phoneEditText,nameEditText, passwordEditText;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Edit Texts
        phoneEditText = (MaterialEditText) findViewById(R.id.phoneEditText);
        nameEditText = (MaterialEditText) findViewById(R.id.nameEditText);
        passwordEditText = (MaterialEditText) findViewById(R.id.passwordEditText);
        //Sign Up button
        signUpBtn = (Button) findViewById(R.id.signUpBtn);

        //Initialize firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //check if phone number already registered in database
                        if(dataSnapshot.child(phoneEditText.getText().toString()).exists()){
                            Toast.makeText(SignUp.this,
                                    "Phone Number Already Registered",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //Register new user in Database
                            User user = new User(nameEditText.getText().toString(),
                                    passwordEditText.getText().toString());
                            //Register phone number, and User object
                            table_user.child(phoneEditText.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this,
                                    "Registration Successful",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
