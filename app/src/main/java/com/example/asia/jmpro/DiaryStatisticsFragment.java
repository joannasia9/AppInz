package com.example.asia.jmpro;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.adapters.SpinnerAdapter;
import com.example.asia.jmpro.data.Day;
import com.example.asia.jmpro.data.db.DayDao;
import com.example.asia.jmpro.logic.MyCustomXAxisValueFormatter;
import com.example.asia.jmpro.logic.calendar.DateUtilities;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asia on 15/10/2017.
 *
 */

public class DiaryStatisticsFragment extends Fragment {
    DayDao dayDao;
    Spinner spinner1, spinner2;
    String[] spinnerItems, spinner2Items;
    BarChart barChart;

    ArrayList<Day> lastDaysArrayList;
    ArrayList<String> argumentsStringArrayList;

    String[] xLabels;
    ArrayList<Float> yValuesCountedElementsList;
    ArrayList<Integer> colors;

    Date currentDate;
    int selectedArguments = 0;
    int selectedPeriod = 0;
    boolean selectedYearItem = false;

    TextView maxValueOfShownData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View diaryFragment = inflater.inflate(R.layout.diary_statistics_fragment,container,false);

        selectedYearItem = false;
        spinner1 = (Spinner) diaryFragment.findViewById(R.id.spinner2);
        spinner2 = (Spinner) diaryFragment.findViewById(R.id.spinner3);

        barChart = (BarChart) diaryFragment.findViewById(R.id.graph);


        maxValueOfShownData = (TextView) diaryFragment.findViewById(R.id.maxValueOfShownDatas);

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
                drawBarChart(getSelectedPeriod(),getSelectedArguments());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectedArguments(position);
                drawBarChart(getSelectedPeriod(),getSelectedArguments());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            drawBarChart(0,0);
            }
        });
        return diaryFragment;
    }

    private void drawBarChart(int period, int kindOfArguments){
        barChart.clear();
        Description description = new Description();
        String dataSetName="";
        String maxValue;

        switch (period){
            case 0:
                lastDaysArrayList = dayDao.getLastSevenDays(currentDate);
                description.setText(getString(R.string.last_week));
                barChart.setDescription(description);
                dataSetName = getString(R.string.last_week);
                selectedYearItem = false;
                break;
            case 1:
                lastDaysArrayList = dayDao.getCurrentMonthDays(currentDate);
                description.setText(getString(R.string.last_month));
                barChart.setDescription(description);
                dataSetName = getString(R.string.last_month);
                selectedYearItem = false;
                break;
            case 2:
                lastDaysArrayList = dayDao.getCurrentYearDays(currentDate);
                description.setText(getString(R.string.last_year));
                barChart.setDescription(description);
                dataSetName = getString(R.string.last_year);
                selectedYearItem = true;
                break;
        }

        switch(kindOfArguments){
            case 0:
                argumentsStringArrayList = dayDao.getAllEatenProductsString(lastDaysArrayList);
                yValuesCountedElementsList = dayDao.countEverySingleElements(argumentsStringArrayList,lastDaysArrayList, DayDao.CODE_PRODUCTS);
                xLabels = dayDao.getAllEatenProductsList(lastDaysArrayList);
                dataSetName+=": Spożyte produkty.\n";
                break;

            case 1:
                argumentsStringArrayList = dayDao.getAllMedicinesString(lastDaysArrayList);
                yValuesCountedElementsList = dayDao.countEverySingleElements(argumentsStringArrayList, lastDaysArrayList,DayDao.CODE_MEDICINES);
                xLabels = dayDao.getAllEatenMedicinesList(lastDaysArrayList);
                dataSetName+=": Zażyte leki.\n";
                break;


            case 2:
                argumentsStringArrayList = dayDao.getAllSymptomsString(lastDaysArrayList);
                yValuesCountedElementsList = dayDao.countEverySingleElements(argumentsStringArrayList,lastDaysArrayList, DayDao.CODE_SYMPTOMS);
                xLabels = dayDao.getAllSymptomsList(lastDaysArrayList);
                dataSetName+=": Symptomy.\n";
                break;
        }

        maxValue = getString(R.string.max_value) + getMaxValuesElementName(xLabels,yValuesCountedElementsList);
        maxValueOfShownData.setText(maxValue);
        description.setXOffset(100);
        description.setYOffset(160f);
        description.setTextSize(15);
        description.setTextColor(getContext().getColor(R.color.colorAccent));
        List<BarEntry> entries = getEntriesList(yValuesCountedElementsList);


        colors = new ArrayList<>(yValuesCountedElementsList.size());

        for(int i = 0; i < yValuesCountedElementsList.size(); i++){
            int colorValue = randomColor();
            colors.add(colorValue);
        }

        BarDataSet set = new BarDataSet(entries, dataSetName);
        set.setColors(colors);
        set.setValueTextColor(Color.WHITE);

        BarData data = new BarData(set);
        data.setBarWidth(0.8f);

        barChart.getAxisLeft().setAxisMaximum(100f);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        YAxis yAxis = barChart.getAxisRight();
        yAxis.setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1f);

        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        xAxis.setLabelRotationAngle(270f);

        MyCustomXAxisValueFormatter formatter = new MyCustomXAxisValueFormatter(argumentsStringArrayList);
        xAxis.setValueFormatter(formatter);
        xAxis.setDrawGridLines(false);

        Legend legend = barChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(8f);
        legend.setForm(Legend.LegendForm.CIRCLE);

        barChart.setPinchZoom(true);

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                int index = ((int) e.getX() - 1);
                Toast.makeText(getContext(), getContext().getString(R.string.el)+ " " + argumentsStringArrayList.get(index) + "\n" + getContext().getString(R.string.last_year_value) + yValuesCountedElementsList.get(index) +"%", Toast.LENGTH_LONG ).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.invalidate();

        if (selectedYearItem){
            drawPieChartDiagramDialog( argumentsStringArrayList, yValuesCountedElementsList, colors, dataSetName, kindOfArguments);
        }
    }

    private void drawPieChartDiagramDialog(final ArrayList<String> argumentsStringArrayList,
                                           final ArrayList<Float> yValuesCountedElementsList,
                                           ArrayList<Integer> colors, String dataSetName, int selection){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.pie_chart_dialog);
        dialog.create();

        PieChart pieChart = (PieChart) dialog.findViewById(R.id.pieChart);
        pieChart.clear();

        ArrayList<PieEntry> pieEntries = getPieEntriesList(yValuesCountedElementsList);
        PieDataSet set = new PieDataSet(pieEntries, dataSetName);
        set.setColors(colors);
        set.setSliceSpace(2);

        PieData data = new PieData(set);

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(8f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

        switch (selection){
            case 0:
                pieChart.setCenterText(getContext().getString(R.string.prod));
                break;
            case 1:
                pieChart.setCenterText(getContext().getString(R.string.med));
                break;
            case 2:
                pieChart.setCenterText(getContext().getString(R.string.sym));
                break;
        }

        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(20);
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);
        pieChart.setDrawSlicesUnderHole(true);

        pieChart.setData(data);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                int index = ((int) h.getX());
                Toast.makeText(getContext(), getContext().getString(R.string.el)+ " " + argumentsStringArrayList.get(index) + "\n" + getContext().getString(R.string.last_year_value) + yValuesCountedElementsList.get(index) +"%", Toast.LENGTH_LONG ).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.button7);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedYearItem = false;
                dialog.cancel();
            }
        });

        dialog.show();

    }
    private String getMaxValuesElementName(String[] names, ArrayList<Float> values){
        StringBuilder buffer = new StringBuilder();

        float maxValue = 0;
        for(Float value : values){
            if(value>maxValue){
                maxValue = value;
            }
        }

        for(int i = 0; i < values.size(); i++){
            if(values.get(i) == maxValue){
                buffer.append("\n");
                buffer.append(names[i]);
                buffer.append(";");
            }
        }
        return buffer.toString();
    }

    private int randomColor() {
        int r = (int) (0xff * Math.random());
        int g = (int) (0xff * Math.random());
        int b = (int) (0xff * Math.random());
        return Color.rgb(r, g, b);
    }

    private ArrayList<BarEntry> getEntriesList(final ArrayList<Float> countedElements){
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < countedElements.size(); i++){
            entries.add(new BarEntry(i+1, countedElements.get(i)));
        }
        return entries;
    }

    private ArrayList<PieEntry> getPieEntriesList(final ArrayList<Float> countedElements){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for(int i = 0; i < countedElements.size(); i++){
            entries.add(new PieEntry(countedElements.get(i), i));
        }
        return entries;
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
