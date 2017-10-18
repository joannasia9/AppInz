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
    Spinner spinner;
    String selectedItem;
    String[] spinnerItems;
    GraphView graph;
    DataPoint[] points;
    ArrayList<Day> lastDaysArrayList, thisMonthDaysArrayList, lastTwelveMonthsDaysList;
    ArrayList<Double> countedEatenProducts;
    ArrayList<String> eatenProductsArrayList;
    String[] allEatenProductsList;
    Date currentDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View diaryFragment = inflater.inflate(R.layout.diary_statistics_fragment,container,false);

        spinner = (Spinner) diaryFragment.findViewById(R.id.spinner2);
        graph = (GraphView) diaryFragment.findViewById(R.id.graph);

        spinnerItems = getResources().getStringArray(R.array.spinner_items);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), spinnerItems);
        spinner.setAdapter(spinnerAdapter);

        currentDate = DateUtilities.getDate(DateUtilities.currentYear(), DateUtilities.currentMonth(), DateUtilities.currentDay());

        dayDao = new DayDao(getContext());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        graph.removeAllSeries();
                        drawGraph(0);
                        break;
                    case 1:
                        graph.removeAllSeries();
                        drawGraph(1);
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return diaryFragment;
    }

    private void drawGraph(int choice){
        switch (choice){
            case 0:
                lastDaysArrayList = dayDao.getLastSevenDays(currentDate);
                graph.setTitle(getString(R.string.last_week));
                break;
            case 1:
                lastDaysArrayList = dayDao.getCurrentMonthDays(currentDate);
                graph.setTitle(getString(R.string.last_month));
                break;
        }

        allEatenProductsList = dayDao.getAllEatenProductsList(lastDaysArrayList);
        eatenProductsArrayList = dayDao.getAllEatenProducts(lastDaysArrayList);
        countedEatenProducts = dayDao.countEverySingleEatenProducts(eatenProductsArrayList,lastDaysArrayList);

        points = convertToTableList(getDataPointsList(countedEatenProducts));


        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);


        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

       // graph.getViewport().setScalable(true);
        // graph.getLayoutParams().height = 100 * allEatenProductsList.length;

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(((int) data.getX() + (int) data.getY()) * 255/4, (int) Math.abs(data.getY() * 255/6), 50);
            }
        });
        series.setSpacing(40);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLUE);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(allEatenProductsList);


        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getViewport().setScalableY(true);

    }

    private ArrayList<DataPoint> getDataPointsList(final ArrayList<Double> countedEatenProducts){
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        for(int i = 0; i < countedEatenProducts.size(); i++){
            dataPoints.add(new DataPoint(i, countedEatenProducts.get(i)));
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

}
