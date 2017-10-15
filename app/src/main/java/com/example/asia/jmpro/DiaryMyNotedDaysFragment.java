package com.example.asia.jmpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by asia on 15/10/2017.
 *
 */

public class DiaryMyNotedDaysFragment extends Fragment {
    TextView title;
    ListView allDaysListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View diaryFragment = inflater.inflate(R.layout.fragment_substitutes,container,false);

        title = (TextView) diaryFragment.findViewById(R.id.textView23);
        title.setText(getString(R.string.saved_days));

        allDaysListView = (ListView) diaryFragment.findViewById(R.id.dedicatedSubstitutesListView);

        return diaryFragment;
    }
}
