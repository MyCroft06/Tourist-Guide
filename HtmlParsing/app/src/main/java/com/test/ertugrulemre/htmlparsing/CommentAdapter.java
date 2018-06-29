package com.test.ertugrulemre.htmlparsing;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Comment> mComment;

    public CommentAdapter(Activity activity, int simple_list_item_1, ArrayList<Comment> mComment) {

        this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mComment = mComment;

    }

    @Override
    public int getCount() {
        return mComment.size();
    }

    @Override
    public Object getItem(int position) {
        return mComment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.list_comment, null);
        TextView name = (TextView) convertView.findViewById(R.id.name_txt);
        TextView comment = (TextView) convertView.findViewById(R.id.comment_txt);
        ImageView img_6 = (ImageView) convertView.findViewById(R.id.imageView6);
        ImageView img_7 = (ImageView) convertView.findViewById(R.id.imageView7);
        ImageView img_8 = (ImageView) convertView.findViewById(R.id.imageView8);
        ImageView img_9 = (ImageView) convertView.findViewById(R.id.imageView9);
        ImageView img_10 = (ImageView) convertView.findViewById(R.id.imageView10);
        TextView dates = (TextView) convertView.findViewById(R.id.date_txt);

        Comment yol = mComment.get(position);
        name.setText(yol.getName());
        comment.setText(yol.isComment());
        dates.setText(yol.isDate());
        String at = (yol.isRating());
        switch (at) {
            case "1":
                img_6.setImageResource(R.drawable.star_fill);
                break;
            case "2":
                img_6.setImageResource(R.drawable.star_fill);
                img_7.setImageResource(R.drawable.star_fill);
                break;
            case "3":
                img_6.setImageResource(R.drawable.star_fill);
                img_7.setImageResource(R.drawable.star_fill);
                img_8.setImageResource(R.drawable.star_fill);
                break;
            case "4":
                img_6.setImageResource(R.drawable.star_fill);
                img_7.setImageResource(R.drawable.star_fill);
                img_8.setImageResource(R.drawable.star_fill);
                img_9.setImageResource(R.drawable.star_fill);
                break;
            case "5":
                img_6.setImageResource(R.drawable.star_fill);
                img_7.setImageResource(R.drawable.star_fill);
                img_8.setImageResource(R.drawable.star_fill);
                img_9.setImageResource(R.drawable.star_fill);
                img_10.setImageResource(R.drawable.star_fill);
                break;

        }

        return convertView;
    }
}
