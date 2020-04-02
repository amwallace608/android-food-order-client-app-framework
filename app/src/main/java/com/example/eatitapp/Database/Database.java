package com.example.eatitapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.eatitapp.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "EatItDB.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    //get cart
    public List<Order> getCarts(){
        //SQLite DB setup
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect={"ProductId","ProductName","Quantity","Price"};
        String sqlTable = "OrderDetail";

        queryBuilder.setTables(sqlTable);
        Cursor c = queryBuilder.query(db,sqlSelect,
                null,null,null,null,null);

        final List<Order> result = new ArrayList<>();
        //loop through cart, adding food orders to result
        if(c.moveToFirst()){
            do{
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price"))
                        ));
            } while (c.moveToNext());
        }
        return result;
    }
    //Add food order to DB for cart
    public void addToCart (Order order){
        SQLiteDatabase db = getReadableDatabase();
        //formate query with order details
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price) VALUES('%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice()
                );
        db.execSQL(query);
    }
    //clean cart
    public void cleanCart (){
        SQLiteDatabase db = getReadableDatabase();
        //formate query to delete orders
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

}
