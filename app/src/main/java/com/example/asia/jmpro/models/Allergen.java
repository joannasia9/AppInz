package com.example.asia.jmpro.models;

/**
 * Created by asia on 31/08/2017.
 */

public class Allergen {
    private String name;
    private boolean isSelected;

    public Allergen(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
