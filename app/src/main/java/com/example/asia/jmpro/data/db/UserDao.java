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
    private DbConnector dbConnector;


    public UserDao() {
        this.dbConnector = DbConnector.getInstance();
        this.realmDatabase = DbConnector.getInstance().getRealmDatabase();
    }


    public interface UserRegistrationCallback {
        void onUserRegistrationSuccess();
    }

    public void insertUser(final EditText login, final EditText password, final EditText email, final Date birthDate, final UserRegistrationCallback registrationCallback) {
        final UserRealm newUser = new UserRealm();
        newUser.setLogin(login.getText().toString().trim());
        newUser.setPassword(password.getText().toString().trim());
        newUser.setEmail(email.getText().toString().trim());
        newUser.setBirthDate(birthDate);

        realmDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(newUser);
                registrationCallback.onUserRegistrationSuccess();
            }
        });
    }
}
