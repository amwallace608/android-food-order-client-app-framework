package com.example.eatitapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    FirebaseRecyclerOptions<Food> options;

    String categoryId="";
    //searchbar
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

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
        //search bar
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
        materialSearchBar.setHint("Enter Food");
        //load suggestions from Firebase
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        //monitor search input to adjust suggestions
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //change suggestions based on user input text
                List<String> suggest = new ArrayList<String>();
                //loop through suggestion list
                for(String search:suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                    {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //restore original suggest adapter when search bar closed
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //Show result of search adapter on confirmation
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        //compare name to search item
        Query query = foodList.orderByChild("Name").equalTo(text.toString());
        options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(query, Food.class).build();
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
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
                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", searchAdapter.getRef(position).getKey());
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
        };
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);//search result in recyclerview
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

    private void loadSuggest(){
        foodList.orderByChild("MenuID").equalTo(categoryId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> test = dataSnapshot.getChildren();
                        for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                            Food item = postSnapshot.getValue(Food.class);
                            suggestList.add(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
