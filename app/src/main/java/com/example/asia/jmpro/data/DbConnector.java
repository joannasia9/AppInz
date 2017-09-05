package com.example.asia.jmpro.data;

import android.widget.EditText;

import com.example.asia.jmpro.data.modules.GlobalEntitiesModule;
import com.example.asia.jmpro.data.modules.PrivateEntitiesModule;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by asia on 29/08/2017.
 */

public class DbConnector {
    private static DbConnector instance;
    private static final String PRIVATE_REALM_URL = "realm://192.168.0.12:9080/~/appInz";
    private static final String AUTH_URL = "http://192.168.0.12:9080/auth";
    private static final String REALM_URL = "realm://192.168.0.12:9080/appInz";

    private String login;
    private String password;
    private Realm realmDatabase;
    private Realm privateRealmDatabase;
    private SyncUser syncUser;
    private RealmConfiguration configuration;
    private RealmConfiguration privateConfiguration;

    public interface DBConnectorRegistrationCallback {
        void onRegistrationSuccess(SyncUser user);

        void onRegistrationFailure(Throwable error);
    }

    public interface DBConnectorLoginCallback {
        void onSuccess(SyncUser user);

        void onError(RuntimeException error);
    }

    public interface DBConnectorDatabaseCallback {
        void onSuccess(Realm realm);

        void onError(Throwable exception);
    }


    private DbConnector() {

    }

    public static DbConnector getInstance() {
        if (instance == null) {
            instance = new DbConnector();
        }
        return instance;
    }

    public void registerNewUser(final EditText uLogin, EditText uPassword, final DBConnectorRegistrationCallback registrationCallback) {
        this.login = uLogin.getText().toString().trim();
        this.password = uPassword.getText().toString();

        SyncCredentials usersCredentials = SyncCredentials.usernamePassword(login, password, true);

        SyncUser.loginAsync(usersCredentials, AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                registrationCallback.onRegistrationSuccess(user);
            }

            @Override
            public void onError(ObjectServerError e) {
                registrationCallback.onRegistrationFailure(e);
            }
        });
    }

    public void dbConnect(String login, String password, final DBConnectorLoginCallback callback) {
        this.login = login;
        this.password = password;

        if (syncUser != null) {
            callback.onSuccess(syncUser);
            return;
        }

        SyncCredentials usersCredentials = SyncCredentials.usernamePassword(login, password, false);
        SyncUser.loginAsync(usersCredentials, AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                setSyncUser(user);
                callback.onSuccess(syncUser);
            }

            @Override
            public void onError(ObjectServerError e) {
                callback.onError(e);
            }
        });
    }


    public void connectToDatabase(final DBConnectorDatabaseCallback dbCallback) {
        dbConnect(login, password, new DBConnectorLoginCallback() {
            @Override
            public void onSuccess(SyncUser user) {
                if (realmDatabase != null) {
                    dbCallback.onSuccess(realmDatabase);
                    return;
                }

                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        setRealmDatabase(realm);
                        dbCallback.onSuccess(realmDatabase);
                    }

                    @Override
                    public void onError(Throwable exception) {
                        exception.printStackTrace();
                        dbCallback.onError(exception);
                    }
                });
            }


            @Override
            public void onError(RuntimeException error) {
            }
        });
    }

    public void connectToPrivateDatabase(final DBConnectorDatabaseCallback dbCallback) {
        dbConnect(login, password, new DBConnectorLoginCallback() {
            @Override
            public void onSuccess(SyncUser user) {
                if (privateRealmDatabase != null) {
                    dbCallback.onSuccess(privateRealmDatabase);
                    return;
                }

                Realm.getInstanceAsync(privateConfiguration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        setPrivateRealmDatabase(realm);
                        dbCallback.onSuccess(privateRealmDatabase);
                    }

                    @Override
                    public void onError(Throwable exception) {
                        exception.printStackTrace();
                        dbCallback.onError(exception);
                    }
                });
            }

            @Override
            public void onError(RuntimeException error) {
            }
        });

    }

    public void setSyncUser(SyncUser syncUser) {
        this.syncUser = syncUser;
    }

    public void setRealmDatabase(Realm realmDatabase) {
        this.realmDatabase = realmDatabase;
    }

    public Realm getRealmDatabase() {
        return realmDatabase;
    }

    public Realm getPrivateRealmDatabase() {
        return privateRealmDatabase;
    }

    public void setPrivateRealmDatabase(Realm privateRealmDatabase) {
        this.privateRealmDatabase = privateRealmDatabase;
    }

    public void clearData() {
        this.login = null;
        this.password = null;
        this.realmDatabase = null;
        this.privateRealmDatabase = null;
        this.syncUser = null;
    }

    public void setConfiguration(SyncUser user) {
        if (configuration == null) {
            configuration = new SyncConfiguration.Builder(syncUser, REALM_URL)
                    .modules(new GlobalEntitiesModule())
                    .waitForInitialRemoteData()
                    .build();

            Realm.setDefaultConfiguration(configuration);
        }
    }

    public void setPrivateConfiguration(SyncUser user) {
        if (privateConfiguration == null) {
            privateConfiguration = new SyncConfiguration.Builder(syncUser, PRIVATE_REALM_URL)
                    .modules(new PrivateEntitiesModule())
                    .waitForInitialRemoteData()
                    .build();
        }
    }

    public String getLogin() {
        return login;
    }

}

