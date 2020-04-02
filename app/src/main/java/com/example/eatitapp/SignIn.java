package com.example.eatitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    MaterialEditText phoneEditText, passwordEditText;
    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Edit Text inputs for phone number and password
        phoneEditText = (MaterialEditText) findViewById(R.id.phoneEditText);
        passwordEditText = (MaterialEditText) findViewById(R.id.passwordEditText);

        //Button input for Sign In button
        signInBtn = (Button) findViewById(R.id.signInBtn);

        //initialize firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        //Onclick listener for Sign In Button
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Check if user exists in the database
                        if(dataSnapshot.child(phoneEditText.getText().toString()).exists()) {
                            //Get User information w/ dataSnapshot of input
                            User user = dataSnapshot.child
                                    (phoneEditText.getText().toString()).getValue(User.class);
                            //get user phone number
                            user.setPhone(phoneEditText.getText().toString());
                            if (user.getPassword().equals(passwordEditText.getText().toString())) {
                                /*show sign in success message if password matches
                                Toast.makeText(SignIn.this, "Success!",
                                        Toast.LENGTH_SHORT).show();*/
                                Intent homeIntent = new Intent(SignIn.this,Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "Sign in Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(SignIn.this, "User Not Found",
                                    Toast.LENGTH_SHORT).show();
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
