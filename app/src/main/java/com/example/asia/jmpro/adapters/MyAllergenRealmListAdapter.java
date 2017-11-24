package com.example.asia.jmpro.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.AllergenRealm;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.data.db.SubstituteDao;
import com.example.asia.jmpro.logic.DrawableResourceExtrator;
import com.example.asia.jmpro.viewholders.AllergenWithSubstitutesViewHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.example.asia.jmpro.R.attr.centerColor;

/**
 * Created by asia on 06/10/2017.
 */

public class MyAllergenRealmListAdapter extends BaseAdapter {

    private Context context;
    private List<AllergenRealm> allergensList;
    private ArrayList<String> selectedAllergensList;

    @Override
    public int getCount() {
        return allergensList.size();
    }

    public MyAllergenRealmListAdapter(Context context, List<AllergenRealm> allergensList, ArrayList<String> selectedAllergensList) {
        this.context = context;
        this.allergensList = allergensList;
        this.selectedAllergensList = selectedAllergensList;
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
        View item = convertView;
        AllergenWithSubstitutesViewHolder viewHolder;

        if(item == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.single_allergens_substitutes_list_item, parent, false);

            viewHolder = new AllergenWithSubstitutesViewHolder(item);
            item.setTag(viewHolder);
        } else {
            viewHolder = (AllergenWithSubstitutesViewHolder) item.getTag();
        }

        AllergenRealm model = allergensList.get(position);
        viewHolder.allergenName.setText(model.getAllergenName());

        SubstituteDao substituteDao = new SubstituteDao(context);

        ArrayList<SubstituteRealm> allergenSubstitutes = substituteDao.getAllAllergensSubstituteList(model.getAllergenName());
        StringBuilder builder = new StringBuilder();
        builder.append(context.getString(R.string.substitutes)).append(": \n");

        if(allergenSubstitutes.size()!= 0) {
            for (SubstituteRealm substituteRealm : allergenSubstitutes) {
                builder.append(substituteRealm.getName()).append("\n");
            }
        } else builder.append(context.getString(R.string.no_results));

        viewHolder.allergensSubstitutes.setText(builder.toString());

        if(selectedAllergensList.contains(model.getAllergenName())){
            viewHolder.layout.setBackgroundColor(context.getColor(R.color.colorWhite));
        }  else {
            viewHolder.layout.setBackgroundColor(context.getColor(R.color.cardview_shadow_end_color));
        }

        return item;
    }

    public void updateAdapter(List<AllergenRealm> allAllergensList, ArrayList<String> selectedAllergensList){
        this.selectedAllergensList = selectedAllergensList;
        this.allergensList = allAllergensList;
        notifyDataSetChanged();
    }

}
