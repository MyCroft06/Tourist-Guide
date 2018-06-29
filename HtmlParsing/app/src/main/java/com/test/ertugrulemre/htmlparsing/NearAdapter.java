package com.test.ertugrulemre.htmlparsing;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NearAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private ArrayList<Near> nNear;

    public NearAdapter(Activity activity, int simple_list_item_1, ArrayList<Near> nNear) {

        this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.nNear = nNear;

    }

    @Override
    public int getCount() {
        return nNear.size();
    }

    @Override
    public Object getItem(int position) {
        return nNear.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.list_near, null);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView comment = (TextView) convertView.findViewById(R.id.distance);
        TextView dates = (TextView) convertView.findViewById(R.id.duration);

        Near yol = nNear.get(position);
        name.setText(yol.getName());
        comment.setText(yol.getDistance());
        dates.setText(yol.getDuration());

        return convertView;
    }
}
