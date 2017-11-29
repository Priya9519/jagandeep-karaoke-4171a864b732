package com.example.jagandeep.igotvkaraoke.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jagandeep.igotvkaraoke.DataModel.VideoOnDemandInformation;
import com.example.jagandeep.igotvkaraoke.NetworkClasses.CallBacks;
import com.example.jagandeep.igotvkaraoke.R;

import java.util.ArrayList;

import io.github.kobakei.centereddrawablebutton.CenteredDrawableButton;

/**
 * Created by jagandeep on 9/4/17.
 */

public class KaraokeRecyclerAdapter extends RecyclerView.Adapter<KaraokeRecyclerAdapter.KaraokeHolder> {

    Context mContext;
    ArrayList<VideoOnDemandInformation> data;

    CallBacks listner;

    public KaraokeRecyclerAdapter(Context mContext, ArrayList<VideoOnDemandInformation> data, CallBacks listnr) {
        this.mContext = mContext;
        this.data = data;
        listner = listnr;
    }


    @Override
    public KaraokeRecyclerAdapter.KaraokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.karaoke_recycler_adapter, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Event", "event");
            }
        });
        return new KaraokeRecyclerAdapter.KaraokeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(KaraokeRecyclerAdapter.KaraokeHolder holder, final int position) {
        if (position==0){
            holder.serialNumber.getTextView().setText("#");
            holder.songName.getTextView().setText("SONG");
            holder.artist.getTextView().setText("ARTIST");
            holder.year.getTextView().setText("YEAR");

            holder.songName.setGravity(Gravity.CENTER);
            holder.artist.setGravity(Gravity.CENTER);

            holder.songName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    listner.sortItems(1);
                }
            });
            holder.artist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    listner.sortItems(2);
                }
            });
            holder.year.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    listner.sortItems(3);
                }
            });
        }
        else {
            holder.serialNumber.getTextView().setText(position + "");
            holder.songName.getTextView().setText(data.get(position-1).getTitle());
            holder.artist.getTextView().setText(data.get(position-1).getArtist());
            holder.year.getTextView().setText("2017");

            holder.serialNumber.getTextView().setTypeface(null, Typeface.NORMAL);
            holder.songName.getTextView().setTypeface(null, Typeface.NORMAL);
            holder.artist.getTextView().setTypeface(null, Typeface.NORMAL);
            holder.year.getTextView().setTypeface(null, Typeface.NORMAL);


            holder.songName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.callbackObserver(position-1);
                }
            });
            holder.artist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.callbackObserver(position-1);
                }
            });
            holder.year.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.callbackObserver(position-1);
                }
            });
            holder.serialNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.callbackObserver(position-1);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size()+1 : 0);
    }

    public class KaraokeHolder extends RecyclerView.ViewHolder {

        public CenteredDrawableButton serialNumber;
        public CenteredDrawableButton songName;
        public CenteredDrawableButton artist;
        public CenteredDrawableButton year;

        public KaraokeHolder(View view) {
            super(view);
            serialNumber = (CenteredDrawableButton) view.findViewById(R.id.serial_number);
            songName = (CenteredDrawableButton) view.findViewById(R.id.song_name);
            artist = (CenteredDrawableButton) view.findViewById(R.id.artist);
            year = (CenteredDrawableButton) view.findViewById(R.id.year);


        }

    }
}
