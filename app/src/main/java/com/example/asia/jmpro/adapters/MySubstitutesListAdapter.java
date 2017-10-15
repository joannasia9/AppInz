package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.data.db.SubstituteDao;
import com.example.asia.jmpro.models.Allergen;
import com.example.asia.jmpro.viewholders.AllergenWithSubstitutesViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asia on 06/10/2017.
 *
 */

public class MySubstitutesListAdapter extends BaseAdapter {
    private Context context;
    private List<Allergen> userAllergens;

    public MySubstitutesListAdapter(Context context, List<Allergen> userAllergens) {
        this.context = context;
        this.userAllergens = userAllergens;
    }

    @Override
    public int getCount() {
        return userAllergens.size();
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

            item = inflater.inflate(R.layout.single_allergens_substitutes_list_item,parent,false);
            viewHolder = new AllergenWithSubstitutesViewHolder(item);
            item.setTag(viewHolder);
        } else {
            viewHolder = (AllergenWithSubstitutesViewHolder) item.getTag();
        }

        Allergen model = userAllergens.get(position);
        viewHolder.allergenName.setText(model.getName());

        SubstituteDao substituteDao = new SubstituteDao(context);

        ArrayList<SubstituteRealm> allergenSubstitutes = substituteDao.getAllAllergensSubstituteList(model.getName());
        StringBuilder builder = new StringBuilder();

        if(allergenSubstitutes.size()!= 0) {
            for (SubstituteRealm substituteRealm : allergenSubstitutes) {
                builder.append(substituteRealm.getName()).append("\n");
            }
        } else builder.append(context.getResources().getString(R.string.no_results));

        viewHolder.allergensSubstitutes.setText(builder.toString());

        return item;
    }
}
