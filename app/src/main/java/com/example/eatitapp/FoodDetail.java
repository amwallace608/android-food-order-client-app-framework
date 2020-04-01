package com.example.eatitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.eatitapp.Model.Food;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class FoodDetail extends AppCompatActivity {
    TextView food_name;
    TextView food_price;
    TextView food_description;
    ImageView food_img;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";

    FirebaseDatabase database;
    DatabaseReference foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Initialize firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //Initialize view (btns, txts, img)
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);
        food_name = (TextView)findViewById(R.id.food_name);
        food_price = (TextView)findViewById(R.id.food_price);
        food_description = (TextView)findViewById(R.id.food_description);
        food_img = (ImageView)findViewById(R.id.img_foodDetail);
        //Init Toolbar, set appearance(s)
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.detail_toolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Food ID from Intent
        if(getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");
            if(!foodId.isEmpty())
            {
                getDetailFood(foodId);
            }

        }
    }

    private void getDetailFood(String foodId){
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Food food = dataSnapshot.getValue(Food.class);
                //Set food image
                Picasso.get().load(food.getImage()).into(food_img);
                //Set food name on toolbar
                collapsingToolbarLayout.setTitle(food.getName());
                //set food name in description
                food_name.setText(food.getName());
                //set price
                food_price.setText(food.getPrice());
                //set description
                food_description.setText(food.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
