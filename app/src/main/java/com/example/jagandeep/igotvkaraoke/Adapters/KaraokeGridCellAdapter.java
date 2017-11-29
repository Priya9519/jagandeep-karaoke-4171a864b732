package com.example.jagandeep.igotvkaraoke.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jagandeep.igotvkaraoke.R;

import java.util.ArrayList;

/**
 * Created by jagandeep on 9/5/17.
 */

public class KaraokeGridCellAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> dataArray;
    Uri imageurl;
    private static LayoutInflater inflater=null;

    public KaraokeGridCellAdapter(Context c, ArrayList<String> arr) {
        mContext = c;
        dataArray = arr;
        inflater = (LayoutInflater)c.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    public int getCount() {
        return dataArray.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        final KaraokeGridCellAdapter.Holder holder=new KaraokeGridCellAdapter.Holder();

        final View rowView = inflater.inflate(R.layout.karaoke_filter_grid, null);
        holder.textView=(TextView) rowView.findViewById(R.id.karaokeGridTextView);
        holder.textView.setText(dataArray.get(position));
        holder.textView.requestLayout();
        rowView.requestLayout();
        return rowView;
    }

    public class Holder
    {
        TextView textView;
    }

}
