package com.example.eatitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Interface.ItemClickListener;
import com.example.eatitapp.Model.Category;
import com.example.eatitapp.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseDatabase database;
    DatabaseReference category;
    TextView fullNameTextView;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //Initialize firebase

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //go to cart when button clicked
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
       NavigationUI.setupWithNavController(navigationView, navController);

       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               int id = item.getItemId();

               if(id == R.id.nav_menu){

               }
               else if(id == R.id.nav_cart){
                   //view cart
                   Intent cartIntent = new Intent(Home.this,Cart.class);
                   startActivity(cartIntent);
               }
               else if(id == R.id.nav_orders){
                   //orders page
                   Intent orderStatusIntent = new Intent(Home.this, OrderStatus.class);
                   startActivity(orderStatusIntent);
               }
               else if(id == R.id.nav_sign_out){
                   //Logout/return to login page
                   Intent signIn = new Intent(Home.this, SignIn.class);
                   signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(signIn);
               }
               drawer.closeDrawer(GravityCompat.START);
               return true;
           }
       });

        //Set Name for current user
        View headerView = navigationView.getHeaderView(0);
        fullNameTextView = (TextView) headerView.findViewById(R.id.fullNameTextView);
        fullNameTextView.setText(Common.currentUser.getName());

        //Load Menu

        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();

    }

    private void loadMenu() {

        Query query = FirebaseDatabase.getInstance().getReference("Category");
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query, Category.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Category model) {
                //bind category object to menuviewholder
                // display category name in textview, image in imageview
                holder.txtMenuName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                final Category clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get category ID and send to new Activity for showing those foods
                        Intent foodList = new Intent(Home.this, FoodList.class);
                        //categoryId is the key, just get key of item at this position
                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.menu_item,parent,false);
                return new MenuViewHolder(view);
            }

            @Override
            public void onError(DatabaseError e){
                Toast.makeText(Home.this, ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        recycler_menu.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recycler_menu.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
