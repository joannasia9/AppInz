package com.example.asia.jmpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.SubstituteRealm;
import com.example.asia.jmpro.viewholders.AllergenItemViewHolder;

import java.util.ArrayList;

/**
 * Created by asia on 06/10/2017.
 *
 */

public class SimpleSubstitutesListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SubstituteRealm> substitutesList;

    public SimpleSubstitutesListAdapter(Context context, ArrayList<SubstituteRealm> substitutesList) {
        this.context = context;
        this.substitutesList = substitutesList;
    }

    @Override
    public int getCount() {
        return substitutesList.size();
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
        AllergenItemViewHolder viewHolder;

        if(item == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            item = inflater.inflate(R.layout.single_alergen_item,parent,false);
            viewHolder = new AllergenItemViewHolder(item);
            item.setTag(viewHolder);
        } else {
            viewHolder = (AllergenItemViewHolder) item.getTag();
        }

        SubstituteRealm model = substitutesList.get(position);
        viewHolder.textView.setText(model.getName());
        viewHolder.imageView.setImageResource(R.drawable.single_substitute);

        return item;
    }

    public void updateAdapter(ArrayList<SubstituteRealm> list){
        this.substitutesList = list;
        notifyDataSetChanged();
    }

}
