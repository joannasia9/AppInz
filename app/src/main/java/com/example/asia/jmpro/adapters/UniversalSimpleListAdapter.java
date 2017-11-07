package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.Medicine;
import com.example.asia.jmpro.data.Product;
import com.example.asia.jmpro.data.Symptom;
import com.example.asia.jmpro.logic.DrawableResourceExtrator;
import com.example.asia.jmpro.viewholders.AllergenItemViewHolder;

import java.util.ArrayList;

/**
 * Created by asia on 16/10/2017.
 *
 */

public class UniversalSimpleListAdapter extends BaseAdapter {
    private ArrayList<Product> listOfProducts;
    private ArrayList<Medicine> listOfMedicines;
    private ArrayList<String> listOfNotes;
    private ArrayList<Symptom> listOfSymptoms;
    private ArrayList<String> selectedItems;
    private Context context;
    private int REQUEST_CODE;
    public static final int REQUEST_CODE_PRODUCTS = 1;
    public static final int REQUEST_CODE_MEDICINES = 2;
    public static final int REQUEST_CODE_SYMPTOMS = 3;
    public static final int REQUEST_CODE_NOTES= 4;


    public UniversalSimpleListAdapter(Context context, int REQUEST_CODE) {
        this.context = context;
        this.REQUEST_CODE = REQUEST_CODE;
    }

    @Override
    public int getCount() {
        switch (REQUEST_CODE){
            case REQUEST_CODE_PRODUCTS:
                return listOfProducts.size();
            case REQUEST_CODE_MEDICINES:
                return listOfMedicines.size();
            case REQUEST_CODE_SYMPTOMS:
                return listOfSymptoms.size();
            case REQUEST_CODE_NOTES:
                return listOfNotes.size();
            default:
                return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View singleRow = convertView;
        AllergenItemViewHolder allergenItemViewHolder;

        if (singleRow == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            singleRow = inflater.inflate(R.layout.single_alergen_item, parent, false);
            allergenItemViewHolder = new AllergenItemViewHolder(singleRow);
            singleRow.setTag(allergenItemViewHolder);
        } else {
            allergenItemViewHolder = (AllergenItemViewHolder) singleRow.getTag();
        }

        int recommendedResId = DrawableResourceExtrator.getResIdFromAttribute(context,R.attr.recommended);
        int toRecommendResId = DrawableResourceExtrator.getResIdFromAttribute(context, R.attr.to_recommend);

        switch (REQUEST_CODE) {
            case REQUEST_CODE_PRODUCTS:
                if (listOfProducts.size() != 0) {
                    Product productModel = listOfProducts.get(position);
                    allergenItemViewHolder.textView.setText(productModel.getName());

                    if (selectedItems.contains(productModel.getName())) {
                        allergenItemViewHolder.imageView.setImageResource(recommendedResId);
                    } else {
                        allergenItemViewHolder.imageView.setImageResource(toRecommendResId);
                    }

                } else {
                    allergenItemViewHolder.textView.setText(R.string.no_results_to_show);
                }
                break;

            case REQUEST_CODE_MEDICINES:
                if (listOfMedicines.size() != 0) {
                    Medicine medicineModel = listOfMedicines.get(position);
                    allergenItemViewHolder.textView.setText(medicineModel.getName());

                    if (selectedItems.contains(medicineModel.getName())) {
                        allergenItemViewHolder.imageView.setImageResource(recommendedResId);
                    } else {
                        allergenItemViewHolder.imageView.setImageResource(toRecommendResId);
                    }

                } else {
                    allergenItemViewHolder.textView.setText(R.string.no_results_to_show);
                }
                break;

            case REQUEST_CODE_SYMPTOMS:
                if (listOfSymptoms.size() != 0) {
                    Symptom symptomModel = listOfSymptoms.get(position);
                    allergenItemViewHolder.textView.setText(symptomModel.getName());

                    if (selectedItems.contains(symptomModel.getName())) {
                        allergenItemViewHolder.imageView.setImageResource(recommendedResId);
                    } else {
                        allergenItemViewHolder.imageView.setImageResource(toRecommendResId);
                    }
                } else {
                    allergenItemViewHolder.textView.setText(R.string.no_results_to_show);
                }
                break;

            case REQUEST_CODE_NOTES:
                if (listOfNotes.size() != 0) {
                    String noteModel = listOfNotes.get(position);
                    allergenItemViewHolder.textView.setText(noteModel);

                    if (selectedItems.contains(noteModel)) {
                        allergenItemViewHolder.imageView.setImageResource(recommendedResId);
                    } else {
                        allergenItemViewHolder.imageView.setImageResource(toRecommendResId);
                    }
                } else {
                    allergenItemViewHolder.textView.setText(R.string.no_results_to_show);
                }
                break;

            default:
                break;
        }

        return singleRow;
    }


    public void updateProductsAdapter(ArrayList<Product> list, ArrayList<String> selectedItems){
        this.listOfProducts = list;
        this.selectedItems = selectedItems;
        notifyDataSetChanged();
    }

    public void updateMedicinesAdapter(ArrayList<Medicine> list, ArrayList<String> selectedItems){
        this.listOfMedicines = list;
        this.selectedItems = selectedItems;
        notifyDataSetChanged();
    }

    public void updateSymptomsAdapter(ArrayList<Symptom> list, ArrayList<String> selectedItems){
        this.listOfSymptoms = list;
        this.selectedItems = selectedItems;
        notifyDataSetChanged();
    }

    public void updateNotesAdapter(ArrayList<String> notesList, ArrayList<String> selectedItems){
        this.listOfNotes = notesList;
        this.selectedItems = selectedItems;
        notifyDataSetChanged();
    }

    public void setListOfProducts(ArrayList<Product> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public void setListOfMedicines(ArrayList<Medicine> listOfMedicines) {
        this.listOfMedicines = listOfMedicines;
    }

    public void setListOfNotes(ArrayList<String> listOfNotes) {
        this.listOfNotes = listOfNotes;
    }

    public void setListOfSymptoms(ArrayList<Symptom> listOfSymptoms) {
        this.listOfSymptoms = listOfSymptoms;
    }

    public void setSelectedItems(ArrayList<String> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
