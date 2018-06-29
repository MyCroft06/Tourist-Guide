package com.test.ertugrulemre.htmlparsing;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class YerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Gezilecek> yerler;

    public YerAdapter(Activity activity, int simple_list_item_1, ArrayList<Gezilecek> yerler) {

        this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.yerler = yerler;

    }

    @Override
    public int getCount() {
        return yerler.size();
    }

    @Override
    public Object getItem(int position) {
        return yerler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.list_yer, null);
        TextView isim = (TextView) convertView.findViewById(R.id.isim);
        TextView aciklama = (TextView) convertView.findViewById(R.id.aciklama);
        //TextView adres = (TextView) convertView.findViewById(R.id.adres);
        //TextView location = (TextView) convertView.findViewById(R.id.location);
        Gezilecek yol = yerler.get(position);
        isim.setText(yol.getIsim());
        aciklama.setText(yol.isAciklama());
        //adres.setText("Adres : "+yol.isAdres());
        // location.setText("Location : "+yol.isLocation());

        return convertView;
    }
}
