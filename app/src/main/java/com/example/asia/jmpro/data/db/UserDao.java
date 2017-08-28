package com.example.asia.jmpro.data.db;

import android.content.Context;
import android.widget.EditText;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.UserRealm;

import java.util.Date;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by asia on 21/08/2017.
 *
 */

public class UserDao {
    private Realm realmDatabase;
    private Context context;


    public UserDao(Context c){
        this.context = c;
    }

    public interface UserRegistrationCallback {
        public void onRegistrationSuccess();
        public void onRegistrationFailure(String errorMessage);
    }

    public void registerUser(final EditText login, final EditText password, final EditText email, final Date birthDate, final UserRegistrationCallback registrationCallback){
        Realm.init(context);
        String authUrl="http://192.168.0.12:9080/auth";

        final SyncCredentials myCredentials = SyncCredentials.usernamePassword(login.getText().toString().trim(),password.getText().toString().trim(),true);
        SyncUser.loginAsync(myCredentials, authUrl, new SyncUser.Callback() {
            @Override
            public void onSuccess(final SyncUser user) {
                insertUser(login, password, email, birthDate, user, registrationCallback);
            }

            @Override
            public void onError(ObjectServerError error) {
                login.setError(context.getString(R.string.occupied_login));
                registrationCallback.onRegistrationFailure(error.getErrorMessage());
            }
        });
    }

    private void insertUser(final EditText login, final EditText password, final EditText email, final Date birthDate, SyncUser credentials, final UserRegistrationCallback registrationCallback){
        fetchRealm(credentials, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                realm.beginTransaction();
                UserRealm newUser = realm.createObject(UserRealm.class);
                newUser.setLogin(login.getText().toString().trim());
                newUser.setPassword(password.getText().toString().trim());
                newUser.setEmail(email.getText().toString().trim());
                newUser.setBirthDate(birthDate);
                realm.commitTransaction();
                registrationCallback.onRegistrationSuccess();
            }

            @Override
            public void onError(Throwable exception) {
                super.onError(exception);
                registrationCallback.onRegistrationFailure(exception.getMessage());
            }
        });
    }

    private void fetchRealm(SyncUser credentials, final Realm.Callback callback){
        if(realmDatabase!=null){
            callback.onSuccess(realmDatabase);
        } else {
            RealmConfiguration configuration = new SyncConfiguration.Builder(credentials,"realm://192.168.0.12:9080/appInz")
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
                    callback.onError(exception);
                }
            });
        }
    }

    public void close(){
        realmDatabase.close();
    }

}
