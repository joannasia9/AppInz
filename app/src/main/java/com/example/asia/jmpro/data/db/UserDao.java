package com.example.asia.jmpro.data.db;

import android.content.Context;
import android.util.Log;

import com.example.asia.jmpro.data.UserRealm;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by asia on 21/08/2017.
 *
 */

public class UserDao {
    private Realm realm;
    private RealmConfiguration realmConfiguration;

    public UserDao(Context context){
        Realm.init(context);
        String authURL = "http://192.168.0.12:9080/auth";
        SyncCredentials myCredentials = SyncCredentials.usernamePassword("joannasia.maciak@gmail.com", "przysietnica", false);
        SyncUser.loginAsync(myCredentials, authURL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                Log.d("---> APP", "Great success!");
                SyncConfiguration config = new SyncConfiguration.Builder(user, "realm://192.168.0.12:9080/~/users")
                        .waitForInitialRemoteData()
                        .build();

                RealmAsyncTask task = Realm.getInstanceAsync(config, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        Log.d("---> APP", "Even greater success!");
                    }
                });
            }

            @Override
            public void onError(ObjectServerError error) {
                Log.d("---> APP", "Great failure!");
                Log.d("---> APP", error.toString());
            }
        });
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
    public boolean isUserWithLoginRegistered(String login){
        UserRealm userRealm = realm.where(UserRealm.class).equalTo("login",login).findFirst();
        return userRealm != null;
    }

    public boolean isUserWithLoginAndPasswordRegistered(String login, String password){
        UserRealm userRealm = realm.where(UserRealm.class)
                .equalTo("login",login)
                .equalTo("password",password)
                .findFirst();
        return userRealm != null;
    }

}
