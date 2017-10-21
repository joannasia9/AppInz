package com.example.asia.jmpro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asia.jmpro.adapters.MyAllergenRealmListAdapter;
import com.example.asia.jmpro.adapters.SuggestedPlacesListAdapter;
import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.data.db.AllergenDao;
import com.example.asia.jmpro.data.db.SubstituteDao;
import com.example.asia.jmpro.data.db.UserDao;

import java.util.ArrayList;

import static com.example.asia.jmpro.R.id.nav_share_email;
import static com.example.asia.jmpro.R.id.nav_share_messenger;

public class SubstitutesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment;
    ArrayList<AllergenRealm> selectedAllergensList, allAllergensList;
    SubstituteDao substituteDao;
    AllergenDao allergenDao;
    Dialog dialog;
    MyAllergenRealmListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substitutes);
        selectedAllergensList = new ArrayList<>();
        substituteDao = new SubstituteDao(getApplicationContext());
        allergenDao = new AllergenDao();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new SubstitutesFragment();
        replaceFragmentContent(fragment);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectDrawerItem(item);
        return true;
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_dedicated_substitutes:
                fragment = new SubstitutesFragment();
                break;
            case R.id.nav_search_substitutes:
                fragment = new SubstitutesFragmentSearch();
                break;
            case R.id.nav_add_substitutes:
                fragment = new SubstitutesFragmentAdd();
                break;
            case R.id.nav_share_facebook:
                showSelectSubstitutesDialog(R.id.nav_share_facebook, SuggestedPlacesListAdapter.SELECT_FOR_SHARE_VIA_FACEBOOK_CODE);
                break;
            case R.id.nav_share_email:
                showSelectSubstitutesDialog(R.id.nav_share_email, SuggestedPlacesListAdapter.SELECT_CODE);
                fragment = new SubstitutesFragment();
                break;
            case R.id.nav_share_messenger:
                showSelectSubstitutesDialog(R.id.nav_share_messenger, SuggestedPlacesListAdapter.SELECT_FOR_SHARE_VIA_FACEBOOK_CODE);
                fragment = new SubstitutesFragment();
                break;
            default:
                fragment = new SubstitutesFragment();
        }

        replaceFragmentContent(fragment);

        setTitle(menuItem.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void replaceFragmentContent(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentSubstitutes, fragment).commit();
    }

    private void showSelectSubstitutesDialog(final int choice, final int code) {
        selectedAllergensList.clear();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_suggested_place_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Button okButton = (Button) dialog.findViewById(R.id.hideDialogButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButtonDialog);
        TextView title = (TextView) dialog.findViewById(R.id.suggestPlaceDialogTitle);
        title.setText(R.string.suggest_sub);
        ListView suggestedSubstitutesListView = (ListView) dialog.findViewById(R.id.favouritePlacesList);


        allAllergensList = allergenDao.getAllAllergenRealm();
        adapter = new MyAllergenRealmListAdapter(getApplicationContext(), allAllergensList, selectedAllergensList);
        suggestedSubstitutesListView.setAdapter(adapter);

        suggestedSubstitutesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AllergenRealm model = allAllergensList.get(position);

                ArrayList<String> sAllergens = new ArrayList<>();
                for (AllergenRealm sAllergen : selectedAllergensList) {
                    sAllergens.add(sAllergen.getAllergenName());
                }

                if (sAllergens.contains(model.getAllergenName())) {
                    selectedAllergensList.remove(model);
                } else {
                    if (code == SuggestedPlacesListAdapter.SELECT_FOR_SHARE_VIA_FACEBOOK_CODE) {
                        selectedAllergensList.clear();
                        selectedAllergensList.add(model);
                    } else {
                        selectedAllergensList.add(model);
                    }
                }

                adapter.updateAdapter(allAllergensList, selectedAllergensList);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (choice) {
                    case nav_share_email:
                        if(selectedAllergensList.size() != 0) {
                            shareViaEmail(selectedAllergensList);
                        } else dialog.cancel();
                        break;
                    case nav_share_messenger:
                        if (selectedAllergensList.size() != 0) {
                            shareViaMessenger(selectedAllergensList.get(0));
                        }
                    default:
                        if (selectedAllergensList.size() != 0) {
                            shareSelectedSubstitutes(selectedAllergensList);
                        }
                        break;
                }
                dialog.cancel();
            }
        });
    }

    private void shareSelectedSubstitutes(ArrayList<AllergenRealm> selectedAllergensList) {
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.recommended_sub))
                .append("\n")
                .append("\n");

        for (AllergenRealm item : selectedAllergensList) {
            builder.append(item.getAllergenName()).append("\n");
            ArrayList<SubstituteRealm> substitutes = substituteDao.getAllAllergensSubstituteList(item.getAllergenName());
            for (SubstituteRealm substituteRealm : substitutes) {
                builder.append(getString(R.string.arrow)).append(substituteRealm.getName()).append("\n");
            }
        }

        String sharedText = builder.toString();

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.recommended_sub));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));


    }

    private void shareViaMessenger(AllergenRealm selectedAllergene) {
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.recommended_sub))
                .append("\n")
                .append("\n");

        builder.append(selectedAllergene.getAllergenName()).append("\n");
        ArrayList<SubstituteRealm> substitutes = substituteDao.getAllAllergensSubstituteList(selectedAllergene.getAllergenName());
        for (SubstituteRealm substituteRealm : substitutes) {
            builder.append(getString(R.string.arrow)).append(substituteRealm.getName()).append("\n");
        }


        String sharedText = builder.toString();


        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(sharedText)
                .getIntent();

        if (isAppInstalled("com.facebook.orca")) {
            shareIntent.setPackage("com.facebook.orca");
            startActivity(shareIntent);
        }

    }

    private void shareViaEmail(ArrayList<AllergenRealm> selectedAllergens) {
        UserDao user = new UserDao();

        StringBuilder builder = new StringBuilder();
        builder.append(" <font color=\"#3300ff\" face =\"Times New Roman\"><b>")
                .append(getString(R.string.recommended_sub))
                .append("<br>")
                .append("</b></font>");
        for (AllergenRealm item : selectedAllergens) {
            builder.append("<p><font color=\"6699ff\" face =\"Times New Roman\">").append(item.getAllergenName()).append(": ").append("<br></font>");
            ArrayList<SubstituteRealm> substitutes = substituteDao.getAllAllergensSubstituteList(item.getAllergenName());
            for (SubstituteRealm substituteRealm : substitutes) {
                builder.append(getString(R.string.arrow)).append(substituteRealm.getName()).append("<br>");
            }
            builder.append("</p>");
        }
        String sharedText = builder.toString();


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setData(Uri.parse(getString(R.string.mailto)));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, user.getUserRealmFromDatabase().getLogin() + ": " + getString(R.string.recommended_sub));
        shareIntent.putExtra(Intent.EXTRA_TEXT, fromHtml(sharedText));
        shareIntent.setType("text/html");

        Intent chooser = Intent.createChooser(shareIntent, getString(R.string.e_mail));
        startActivity(chooser);

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }


    private boolean isAppInstalled(final String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.app_does_not_exist))
                    .setMessage(getString(R.string.want_to_install_question))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openPlayStoreToGetApp(uri);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            builder.show();
            return false;
        }
    }

    private void openPlayStoreToGetApp(String uri) {
        final String appPackageName = "https://play.google.com/store/apps/details?id=" + uri;
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            Log.e("ActivityNotFound", "openGooglePlayToGetMaps: " + anfe.getMessage());
        }
    }

}
