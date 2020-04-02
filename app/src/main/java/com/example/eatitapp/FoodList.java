package com.example.eatitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eatitapp.Interface.ItemClickListener;
import com.example.eatitapp.Model.Food;
import com.example.eatitapp.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    FirebaseRecyclerOptions<Food> options;

    String categoryId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Initialize Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");
        //Set up recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Get intent if not null
        if(getIntent() != null){
            categoryId = getIntent().getStringExtra("CategoryId");
            //if category id is valid, load list of foods in the category
            if(!categoryId.isEmpty() && categoryId != null){
                loadListFood(categoryId);
            }
        }
    }

    private void loadListFood(String categoryId){
        //select all foods with MenuId = categoryId
        //foodList.orderByChild("MenuId").equalTo(categoryId);
        Query query = foodList.orderByChild("MenuID").equalTo(categoryId);
        //prepare Firebase options
        options = new FirebaseRecyclerOptions.Builder<Food>().
                setQuery(query, Food.class).build();
        //set up adapter
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                //set food name
                holder.txtFoodName.setText(model.getName());
                //set food image
                Picasso.get().load(model.getImage()).into(holder.foodImgView);

                final Food clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    //show food name on click
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //New activity for food detail, send Food ID to new activity
                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(view);
            }

            @Override
            public void onError(DatabaseError e){
                Toast.makeText(FoodList.this, ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
