package com.example.asia.jmpro.data;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by asia on 29/08/2017.
 *
 */

public class DbConnectorSingleton {
    private static DbConnectorSingleton instance;
    private static final String AUTH_URL="http://192.168.0.12:9080/auth";

    private Realm realmDatabase;
    private SyncUser syncUser;


    private DbConnectorSingleton(String login, String password){
        dbConnect(login,password);
    }

    public static DbConnectorSingleton getInstance(String login, String password){
           if(instance==null) {
            instance = new DbConnectorSingleton(login, password);
           }
        return instance;
    }



    private void dbConnect(String login, String password) {
        SyncCredentials usersCredentials = SyncCredentials.usernamePassword(login, password, false);

            SyncUser.loginAsync(usersCredentials, AUTH_URL, new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) {
                    syncUser = user;
                }

                @Override
                public void onError(ObjectServerError error) {

                }
            });

    }

    public Realm getRealmDatabase(final Realm.Callback callback){
        if(realmDatabase!=null){
            return realmDatabase;
        } else {
            RealmConfiguration configuration = new SyncConfiguration.Builder(syncUser,"realm://192.168.0.12:9080/appInz")
                    .waitForInitialRemoteData()
                    .build();

            Realm.getInstanceAsync(configuration, new Realm.Callback() {
                @Override
                public void onSuccess(Realm realm) {
                    realmDatabase = realm;
                    callback.onSuccess(realmDatabase);
                }

                @Override
                public void onError(Throwable exception) {
                    exception.printStackTrace();
                   callback.onError(exception);
                }
            });
        }
        return realmDatabase;
    }

}

