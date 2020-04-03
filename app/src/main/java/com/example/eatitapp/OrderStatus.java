package com.example.eatitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Model.Request;
import com.example.eatitapp.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());

    }

    private void loadOrders(String phone) {
        Query query = FirebaseDatabase.getInstance().getReference("Requests");
        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>().setQuery(query, Request.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Request model) {
                holder.txtOrderStatus.setText(convertStatusCode(model.getStatus()));
                holder.txtOrderPhone.setText(model.getPhone());
                holder.txtOrderId.setText(adapter.getRef(position).getKey());
                holder.txtOrderAddress.setText(model.getAddress());
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.order_layout,parent,false);
                return new OrderViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private String convertStatusCode (String status){
        switch(status)
        {
            case "0":
                return "Placed";
            case "1":
                return "On the way";
            case "2":
                return "Delivered";
            default:
                return "Status Error";
        }
    }
}
