package com.example.asia.jmpro.data.db;

import android.content.Context;

import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.Product;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by asia on 15/10/2017.
 */

public class DayDao {
    private Realm realmDatabase;
    private Realm privateDatabase;
    private Context context;
    private RealmResults<Product> allProductsList;

    public DayDao(Context context) {
        this.context = context;
        this.realmDatabase = DbConnector.getInstance().getRealmDatabase();
        this.privateDatabase = DbConnector.getInstance().getPrivateRealmDatabase();
    }

    public ArrayList<Product> getAllProductsArrayList(){
        final ArrayList<Product> list = new ArrayList<>();
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allProductsList = realm.where(Product.class).findAll();
            }
        });

        for(Product item : allProductsList){
            list.add(item);
        }

        return list;
    }
}
