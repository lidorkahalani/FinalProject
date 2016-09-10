package com.example.yosef.finalproject;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


//    private void displayListView() {
//
//        //Array list of countries
//        ArrayList<Country> categories = new ArrayList<Country>();
//        Country country = new Country("AFG","Afghanistan",false);
//        categories.add(country);
//        country = new Country("ALB","Albania",true);
//        categories.add(country);
//        country = new Country("DZA","Algeria",false);
//        categories.add(country);
//        country = new Country("ASM","American Samoa",true);
//        categories.add(country);
//        country = new Country("AND","Andorra",true);
//        categories.add(country);
//        country = new Country("AGO","Angola",false);
//        categories.add(country);
//        country = new Country("AIA","Anguilla",false);
//        categories.add(country);
//
//        //create an ArrayAdaptar from the String Array
//        dataAdapter = new MyCustomAdapter(this,
//                R.layout.country_info, categories);
//        ListView listView = (ListView) findViewById(R.id.listView1);
//        // Assign adapter to ListView
//        listView.setAdapter(dataAdapter);
//
//
//        listView.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // When clicked, show a toast with the TextView text
//                Country country = (Country) parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        "Clicked on Row: " + country.getName(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }
/*
class CategoryListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> categories;

    public CategoryListAdapter(Context context, int textViewResourceId,
                           ArrayList<String> countryList) {
        super(context, textViewResourceId, countryList);
        this.categories = new ArrayList<String>();
        this.categories.addAll(countryList);
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.country_info, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);

            holder.name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    String category = (String) cb.getTag();
                    Toast.makeText(getApplicationContext(),
                            "Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();
                    category.setSelected(cb.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String country = categories.get(position);
        holder.code.setText(" (" + country.getCode() + ")");
        holder.name.setText(country.getName());
        holder.name.setChecked(country.isSelected());
        holder.name.setTag(country);

        return convertView;

    }


    private void checkButtonClick() {


        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<String> countryList = dataAdapter.countryList;
                for (int i = 0; i < countryList.size(); i++) {
                    String country = countryList.get(i);
                    if (country.isSelected()) {
                        responseText.append("\n" + country.getName());
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();

            }
        });

    }
}

*/