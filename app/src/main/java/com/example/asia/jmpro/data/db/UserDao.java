package com.example.asia.jmpro.data.db;

import android.content.Context;

import com.example.asia.jmpro.data.UserRealm;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by asia on 21/08/2017.
 *
 */

public class UserDao {
    private Realm realm;
    private RealmConfiguration realmConfiguration;

    public UserDao(Context context){
        //REALM initialization
        realmConfiguration = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmConfiguration);
    }

    //closing
    public void close(){
    realm.close();
    }

    //inserting User to the database
    public void insertUser(final UserRealm userRealm){
        realm.beginTransaction();
        realm.insert(userRealm);
        realm.commitTransaction();
    }

    //all about user with specified login:
    public boolean getUserByLogin(String login){
        UserRealm userRealm = realm.where(UserRealm.class).equalTo("login",login).findFirst();
        return userRealm != null; //if simplified
}





}
