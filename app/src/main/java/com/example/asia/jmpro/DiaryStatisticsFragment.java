package com.example.asia.jmpro;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.asia.jmpro.adapters.SpinnerAdapter;
import com.example.asia.jmpro.data.Day;
import com.example.asia.jmpro.data.db.DayDao;
import com.example.asia.jmpro.logic.calendar.DateUtilities;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by asia on 15/10/2017.
 *
 */

public class DiaryStatisticsFragment extends Fragment {
    DayDao dayDao;
    Spinner spinner1, spinner2;
    String[] spinnerItems, spinner2Items;
    GraphView graph;
    DataPoint[] points;
    ArrayList<Day> lastDaysArrayList;
    ArrayList<Double> countedElementsList;
    ArrayList<String> elementsArrayList;
    String[] allElementsList;
    Date currentDate;
    int selectedArguments = 0;
    int selectedPeriod = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View diaryFragment = inflater.inflate(R.layout.diary_statistics_fragment,container,false);

        spinner1 = (Spinner) diaryFragment.findViewById(R.id.spinner2);
        spinner2 = (Spinner) diaryFragment.findViewById(R.id.spinner3);

        graph = (GraphView) diaryFragment.findViewById(R.id.graph);

        spinnerItems = getResources().getStringArray(R.array.spinner_items);
        spinner2Items = getResources().getStringArray(R.array.spinner2_items);

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), spinnerItems);
        spinner1.setAdapter(spinnerAdapter);

        SpinnerAdapter spinner2Adapter = new SpinnerAdapter(getContext(), spinner2Items);
        spinner2.setAdapter(spinner2Adapter);

        currentDate = DateUtilities.getDate(DateUtilities.currentYear(), DateUtilities.currentMonth(), DateUtilities.currentDay());

        dayDao = new DayDao(getContext());



        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectedPeriod(position);
                drawProductsGraph(getSelectedPeriod(),getSelectedArguments());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectedArguments(position);
                drawProductsGraph(getSelectedPeriod(),getSelectedArguments());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return diaryFragment;
    }

    private void drawProductsGraph(int period, int kindOfArguments){
        graph.removeAllSeries();

        switch (period){
            case 0:
                lastDaysArrayList = dayDao.getLastSevenDays(currentDate);
                graph.setTitle(getString(R.string.last_week));
                break;
            case 1:
                lastDaysArrayList = dayDao.getCurrentMonthDays(currentDate);
                graph.setTitle(getString(R.string.last_month));
                break;
        }

        switch(kindOfArguments){
            case 0:
                allElementsList = dayDao.getAllEatenProductsList(lastDaysArrayList);
                elementsArrayList = dayDao.getAllEatenProductsString(lastDaysArrayList);
                countedElementsList = dayDao.countEverySingleElements(elementsArrayList,lastDaysArrayList, DayDao.CODE_PRODUCTS);
                break;

            case 1:
                allElementsList = dayDao.getAllEatenMedicinesList(lastDaysArrayList);
                elementsArrayList = dayDao.getAllMedicinesString(lastDaysArrayList);
                countedElementsList = dayDao.countEverySingleElements(elementsArrayList, lastDaysArrayList,DayDao.CODE_MEDICINES);
                break;

            case 2:
                allElementsList = dayDao.getAllSymptomsList(lastDaysArrayList);
                elementsArrayList = dayDao.getAllSymptomsString(lastDaysArrayList);
                countedElementsList = dayDao.countEverySingleElements(elementsArrayList,lastDaysArrayList, DayDao.CODE_SYMPTOMS);
                break;
        }

        points = convertToTableList(getDataPointsList(countedElementsList));


        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);

        graph.getViewport().setYAxisBoundsManual(true);

        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(allElementsList.length + 1);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(((int) data.getX() + (int) data.getY()) * 255/4, (int) Math.abs(data.getY() * 255/6), 50);
            }
        });
        series.setSpacing(20);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLUE);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(addMoreLabels(allElementsList));

        graph.getGridLabelRenderer().setLabelHorizontalHeight(200);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        graph.getGridLabelRenderer().setVerticalAxisTitle(getString(R.string.procent));

//        //
//        graph.getLegendRenderer().setVisible(true);
//        //

        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getViewport().setScalableY(true);

    }

    private String[] addMoreLabels(String[] labels){
        String[] newLabels = new String[labels.length+2];
        newLabels[0] = " ";
        System.arraycopy(labels, 0, newLabels, 1, labels.length);
        newLabels[labels.length+1] = " ";
        return newLabels;
    }

    private ArrayList<DataPoint> getDataPointsList(final ArrayList<Double> countedElements){
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        for(int i = 0; i < countedElements.size(); i++){
            dataPoints.add(new DataPoint(i+1, countedElements.get(i)));
        }
        return dataPoints;
    }

    private DataPoint[] convertToTableList(ArrayList<DataPoint> list){
        DataPoint[] newList = new DataPoint[list.size()];
        for(int i = 0; i<list.size(); i++){
            newList[i] = list.get(i);
        }
        return newList;
    }

    public void setSelectedArguments(int selectedArguments) {
        this.selectedArguments = selectedArguments;
    }

    public void setSelectedPeriod(int selectedPeriod) {
        this.selectedPeriod = selectedPeriod;
    }

    public int getSelectedArguments() {
        return selectedArguments;
    }

    public int getSelectedPeriod() {
        return selectedPeriod;
    }
}
