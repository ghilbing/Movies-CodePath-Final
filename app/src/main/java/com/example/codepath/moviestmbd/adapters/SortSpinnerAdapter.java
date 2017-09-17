package com.example.codepath.moviestmbd.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.codepath.moviestmbd.R;
import com.example.codepath.moviestmbd.Sort;
import com.example.codepath.moviestmbd.fragments.MainFragment;

import java.util.List;

/**
 * Created by gretel on 9/16/17.
 */

public class SortSpinnerAdapter extends ArrayAdapter<Sort> implements SpinnerAdapter {

    private MainFragment mainFragment;

    public SortSpinnerAdapter(MainFragment mainFragment, Context context, List<Sort> options){
        super(context, android.R.layout.simple_spinner_item, options);
        this.mainFragment = mainFragment;
    }

  @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
      Sort option = getItem(position);
      if (convertView == null){
          convertView = mainFragment.getLayoutInflater(null).inflate(R.layout.sort_spinner_dropdown_item, null);
      }

      TextView textView = (TextView) convertView.findViewById(R.id.text);
      textView.setText(option.getText());

      return convertView;
  }
}
