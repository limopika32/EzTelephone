package com.limo.ezTelephone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;


class xAdapter extends ArrayAdapter<teleLog> {
    private final LayoutInflater mLayoutInflater;

    protected xAdapter(Context context){
        super(context, 0);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if(convertView == null){
            view = mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null);
        }

        teleLog tl = getItem(position);
        TextView textView1 = view.findViewById(android.R.id.text1);
        TextView textView2 = view.findViewById(android.R.id.text2);

        textView1.setText(tl.getName());
        textView2.setText(tl.getType()+" \t\t"+tl.getDatef());

        return view;
    }
}
