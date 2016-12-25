package com.myApplication.yosef.finalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yosef on 25-Dec-16.
 */

public class MyViewHolder extends ArrayAdapter<Series> {

    private Context context;
    private int layoutResourceId;
    private List<Series> seriesList;

    private Boolean setBoolenList=false;

    public MyViewHolder(Context context, int resource, List<Series> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.seriesList=objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final SingleLayout holder;
        View rowView=convertView;
        Log.i("TEST getView", "inside getView position " + position);


        if (rowView == null) {
            Log.e("TEST getView", "inside if with position " + position);
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.single_chose_series, parent, false);
            holder=new SingleLayout();
            holder.chosenList=new boolean [seriesList.size()];
            holder.checkBox=(CheckBox) rowView.findViewById(R.id.checkBox);


            rowView.setTag(holder);
        }else
            holder=(SingleLayout)rowView.getTag();

        //keep the position of the checkBox
        RelativeLayout.LayoutParams checkBoxLayout =
                (RelativeLayout.LayoutParams) holder.checkBox.getLayoutParams();
        checkBoxLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        checkBoxLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        holder.checkBox.setLayoutParams(checkBoxLayout);

        Series oneSeries = seriesList.get(position);
        if(oneSeries.getCategory_id()>9)
            holder.checkBox.setTextColor(context.getResources().getColor(R.color.red));

        holder.checkBox.setText(oneSeries.getCategory_name());

        if (setBoolenList)
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    holder.chosenList[position] = isChecked;
                }
            });

        return rowView;

    }

    private static class SingleLayout {
        CheckBox checkBox;
        boolean []chosenList;
    }

    public int getViewTypeCount(){
        if(getCount()!=0)
            return getCount();
        else
            return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
