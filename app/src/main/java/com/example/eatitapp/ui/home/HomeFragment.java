package com.example.eatitapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Home;
import com.example.eatitapp.Interface.ItemClickListener;
import com.example.eatitapp.Model.Category;
import com.example.eatitapp.R;
import com.example.eatitapp.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    FirebaseDatabase database;
    DatabaseReference category;
    TextView fullNameTextView;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Initialize firebase
        //database = FirebaseDatabase.getInstance();
        //category = database.getReference("Category");


        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        //Load Menu
        /*
        recycler_menu = (RecyclerView) root.findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recycler_menu.setLayoutManager(layoutManager);

         */

        //loadMenu();
        return root;
    }

    private void loadMenu() {

        Query query = FirebaseDatabase.getInstance().getReference("Category");
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query, Category.class)
                        .build();

        FirebaseRecyclerAdapter adapter =
                new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
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
                                //Toast.makeText(Home.this, ""+clickItem.getName(),
                                 //       Toast.LENGTH_SHORT).show();
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
                };
        recycler_menu.setAdapter(adapter);

    }
}
