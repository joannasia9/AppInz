package com.example.asia.jmpro.logic;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by asia on 19/10/2017.
 *
 */

public class MyCustomXAxisValueFormatter implements IAxisValueFormatter {
    private ArrayList<String> values;

    public MyCustomXAxisValueFormatter(ArrayList<String> values) {
        this.values = values;
    }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = (int) value;
        if(index < values.size() + 1) {
            return values.get(index - 1);
        } else return null;
    }
}
