package com.example.saurabh.tap_it;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomUsersAdapter extends ArrayAdapter<User> {
    public CustomUsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);    
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvCompany = (TextView) convertView.findViewById(R.id.tvCompany);
        // Populate the data into the template view using the data object
        tvName.setText(user.name);
         tvCompany.setText(user.company);
        // Return the completed view to render on screen
        return convertView;
    }
}
