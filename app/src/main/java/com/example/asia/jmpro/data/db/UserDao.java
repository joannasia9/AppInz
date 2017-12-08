package com.example.asia.jmpro.data.db;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.UserRealm;

import java.util.Date;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncUser;

/**
 * Created by asia on 21/08/2017.
 *
 */

public class UserDao {
    private Realm realmDatabase;
    private String userLogin;
    private UserRealm user;
    private SyncUser syncUser;


    public UserDao() {
        DbConnector dbConnector = DbConnector.getInstance();
        this.realmDatabase = dbConnector.getRealmDatabase();
        this.userLogin = dbConnector.getLogin();
        this.syncUser = dbConnector.getSyncUser();
    }

    public interface UserRegistrationCallback {
        void onUserRegistrationSuccess();
    }

    public void insertUser(final EditText login, final EditText password, final EditText email, final Date birthDate, final UserRegistrationCallback registrationCallback) {
        user = new UserRealm();
        user.setLogin(login.getText().toString().trim());
        user.setPassword(password.getText().toString().trim());
        user.setEmail(email.getText().toString().trim());
        user.setBirthDate(birthDate);

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
                registrationCallback.onUserRegistrationSuccess();
            }
        });
    }

    public UserRealm getUserRealmFromDatabase(){
        user = new UserRealm();
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user = realm.where(UserRealm.class)
                        .equalTo("login", userLogin)
                        .findFirst();
            }
        });
        return user;
    }

    public void updateUserPassword(final Context context, final String password) {
        user = getUserRealmFromDatabase();
        syncUser.changePasswordAsync(password, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                Toast.makeText(context, context.getResources().getString(R.string.password_changed),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(ObjectServerError error) {

            }
        });

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.setPassword(password);
                realm.copyToRealmOrUpdate(user);
            }
        });
    }

    public void updateUserEmail(final String email){
        user = getUserRealmFromDatabase();

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.setEmail(email);
                realm.copyToRealmOrUpdate(user);
            }
        });
    }

    public void deleteUser() {
        user = getUserRealmFromDatabase();
        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.deleteFromRealm();
                //how to delete private database? ---> privateRealm.close(); Realm.deleteRealm(privateConfiguration) works only for local copy of database
                //how to manage User deletion? ---> not only from database
            }
        });
    }


}
