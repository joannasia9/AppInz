package com.example.asia.jmpro.data.db;

import android.widget.EditText;

import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.UserRealm;

import java.util.Date;

import io.realm.Realm;

/**
 * Created by asia on 21/08/2017.
 */

public class UserDao {
    private Realm realmDatabase;
    private String userLogin;
    private UserRealm user;


    public UserDao() {
        DbConnector dbConnector = DbConnector.getInstance();
        this.realmDatabase = dbConnector.getRealmDatabase();
        this.userLogin = dbConnector.getLogin();
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

    public void updateUserPassword(final String password) {
        user = getUserRealmFromDatabase();

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
}
