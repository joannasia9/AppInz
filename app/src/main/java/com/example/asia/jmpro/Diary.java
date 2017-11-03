package com.example.asia.jmpro;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.adapters.DaysListAdapter;
import com.example.asia.jmpro.data.Day;
import com.example.asia.jmpro.data.Medicine;
import com.example.asia.jmpro.data.Note;
import com.example.asia.jmpro.data.Product;
import com.example.asia.jmpro.data.Symptom;
import com.example.asia.jmpro.data.db.DayDao;
import com.example.asia.jmpro.data.db.UserDao;
import com.example.asia.jmpro.logic.calendar.DateUtilities;
import com.example.asia.jmpro.logic.language.PreferencesChangeObserver;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import io.realm.RealmList;

public class Diary extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment;
    DayDao dayDao;
    ArrayList<String> selectedDaysList;
    ArrayList<Day> selectedDaysObjects;
    DaysListAdapter adapter;
    Dialog dialog;
    UserDao userDao;
    String dateString;
    PreferencesChangeObserver preferencesChangeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(MyApp.getThemeId(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dayDao = new DayDao(getApplicationContext());
        userDao = new UserDao();

        dateString = DateUtilities.currentDay() + "." + DateUtilities.currentMonth() + "." + DateUtilities.currentYear();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragmentContent(new DiaryMyNotedDaysFragment());
        preferencesChangeObserver = new PreferencesChangeObserver(this).start();
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
        selectItem(item);
        return true;
    }

    private void selectItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_noted_days:
                fragment = new DiaryMyNotedDaysFragment();
                break;
            case R.id.nav_add_day:
                fragment = new DiaryAddSingleDayFragment();
                break;
            case R.id.nav_statistics:
                fragment = new DiaryStatisticsFragment();
                break;
            case R.id.nav_share_message:
                showSelectDaysDialog(0);
                fragment = new DiaryMyNotedDaysFragment();
                break;
            case R.id.nav_export_document:
                fragment = new DiaryMyNotedDaysFragment();
                showSelectDaysDialog(1);
                break;
        }
        replaceFragmentContent(fragment);

        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void replaceFragmentContent(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentDiary, fragment).commit();
    }

    private void showSelectDaysDialog(final int choice) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_suggested_place_dialog);
        dialog.setCanceledOnTouchOutside(true);


        Button okButton = (Button) dialog.findViewById(R.id.hideDialogButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButtonDialog);
        TextView title = (TextView) dialog.findViewById(R.id.suggestPlaceDialogTitle);
        title.setText(getString(R.string.select_place_to_share));
        ListView daysListView = (ListView) dialog.findViewById(R.id.favouritePlacesList);

        final ArrayList<Day> daysList = dayDao.getAllSavedDays();

        adapter = new DaysListAdapter(daysList, getApplicationContext());
        daysListView.setAdapter(adapter);


        selectedDaysList = new ArrayList<>();
        selectedDaysObjects = new ArrayList<>();
        daysListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Day model = daysList.get(position);

                if (!selectedDaysList.contains(model.getId())) {
                    selectedDaysList.add(model.getId());
                    selectedDaysObjects.add(model);
                } else {
                    selectedDaysList.remove(model.getId());
                    selectedDaysObjects.remove(model);
                }
                adapter.updateAdapter(daysList, selectedDaysList);


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
                    case 0:
                        if(selectedDaysObjects.size()!= 0) {
                            shareViaMessage(selectedDaysObjects);
                        } else dialog.cancel();
                        break;
                    case 1:
                        if(selectedDaysObjects.size()!= 0) {
                            createPdfFile(selectedDaysObjects);
                        } else dialog.cancel();
                        break;
                }
                dialog.cancel();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void createPdfFile(ArrayList<Day> selectedDaysObjects) {
        RealmList<Product> products;
        RealmList<Symptom> symptoms;
        RealmList<Medicine> medicines;
        RealmList<Note> notes;

        Document document = new Document();
        String fileName = getString(R.string.app_name) + "Days" + dateString + ".pdf";
        String outPath = Environment.getExternalStorageDirectory() + "/Download/" + fileName;

        try {
            Font fontTitle = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.RED);
            Font fontChapter = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLUE);
            Font fontParagraph = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
            Font fontContent = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.GRAY);

            String userNameAndDate = getString(R.string.user) + " " + userDao.getUserRealmFromDatabase().getLogin() + "\n" + getString(R.string.date_of_creating) + " " + dateString;

            PdfWriter.getInstance(document, new FileOutputStream(outPath));
            document.open();

            Paragraph title = new Paragraph(userNameAndDate, fontTitle);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setFont(fontTitle);
            document.add(title);

            for (Day item : selectedDaysObjects) {
                products = item.getProductsList();
                medicines = item.getMedicinesList();
                symptoms = item.getSymptomsList();
                notes = item.getNotesList();

                StringBuilder prodBuilder = new StringBuilder();
                for (Product product : products) {
                    prodBuilder.append(product.getName()).append("\n");
                }

                StringBuilder medBuilder = new StringBuilder();
                for (Medicine medicine : medicines) {
                    medBuilder.append(medicine.getName()).append("\n");
                }
                StringBuilder symBuilder = new StringBuilder();
                for (Symptom symptom : symptoms) {
                    symBuilder.append(symptom.getName()).append("\n");
                }
                StringBuilder noteBuilder = new StringBuilder();
                for (Note note : notes) {
                    noteBuilder.append(note.getNoteContent()).append("\n\n");
                }

                Paragraph chapter = new Paragraph(item.getId() + "\n", fontChapter);
                chapter.setSpacingBefore(20);
                Paragraph prod = new Paragraph("\n" + getString(R.string.eaten_products), fontParagraph);
                prod.setSpacingBefore(10);
                Paragraph prodParagraph = new Paragraph(prodBuilder.toString(), fontContent);
                Paragraph med = new Paragraph("\n" + getString(R.string.medicines), fontParagraph);
                Paragraph medParagraph = new Paragraph(medBuilder.toString(), fontContent);
                Paragraph sym = new Paragraph("\n" + getString(R.string.symptoms), fontParagraph);
                Paragraph symParagraph = new Paragraph(symBuilder.toString(), fontContent);
                Paragraph n = new Paragraph("\n" + getString(R.string.notes), fontParagraph);
                Paragraph nParagraph = new Paragraph(noteBuilder.toString(), fontContent);

                prodParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                medParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                symParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                nParagraph.setAlignment(Paragraph.ALIGN_CENTER);

                document.add(chapter);
                document.add(prod);
                document.add(prodParagraph);
                document.add(med);
                document.add(medParagraph);
                document.add(sym);
                document.add(symParagraph);
                document.add(n);
                document.add(nParagraph);
            }

            document.close();
            Toast.makeText(this, getString(R.string.creating_file_suc) + " " + fileName, Toast.LENGTH_LONG).show();

        } catch (DocumentException | FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.file_making_problem), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    private void shareViaMessage(ArrayList<Day> selectedDaysObjects) {
        StringBuilder buffer = new StringBuilder();
        RealmList<Product> products;
        RealmList<Medicine> medicines;
        RealmList<Symptom> symptoms;
        RealmList<Note> notes;

        for (Day item : selectedDaysObjects) {
            products = item.getProductsList();
            medicines = item.getMedicinesList();
            symptoms = item.getSymptomsList();
            notes = item.getNotesList();

            buffer.append("\n").append(item.getId()).append("\n")
                    .append(getString(R.string.eaten_products))
                    .append("\n");
            for (Product prod : products) {
                buffer.append(prod.getName()).append("\n");
            }

            buffer.append("\n")
                    .append(getString(R.string.medicines))
                    .append("\n");
            for (Medicine med : medicines) {
                buffer.append(med.getName()).append("\n");
            }

            buffer.append("\n")
                    .append(getString(R.string.symptoms))
                    .append("\n");
            for (Symptom sym : symptoms) {
                buffer.append(sym.getName()).append("\n");
            }

            buffer.append("\n")
                    .append(getString(R.string.notes))
                    .append("\n");
            for (Note note : notes) {
                buffer.append(note.getNoteContent()).append("\n\n");
            }
        }

        UserDao user = new UserDao();

        String subject = getString(R.string.app_name) + ": " + user.getUserRealmFromDatabase().getLogin() + ": " + getString(R.string.shared_diary);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setData(Uri.parse(getString(R.string.mailto)));
        shareIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.saved_days));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, buffer.toString());
        shareIntent.setType("text/plain");

        Intent chooser = Intent.createChooser(shareIntent, getString(R.string.share_using));

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }


}
