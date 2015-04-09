package com.xtracker.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtracker.android.R;
import com.xtracker.android.activities.TrackActivity;
import com.xtracker.android.objects.Track;

import java.util.ArrayList;

public class TracksListAdapter extends RecyclerView.Adapter<TracksListAdapter.ViewHolder> {

    private ArrayList<Track> tracks;
    private Context context;


    public TracksListAdapter(Context context, ArrayList<Track> tracks) {
        this.tracks = tracks;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tracks_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Track track = tracks.get(i);
        viewHolder.title.setText(track.getTitle());

    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    @Override
    public int getItemViewType(int position) {
        Track track = tracks.get(position);
        return (int) track.getTrackId();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView image;
        TextView length;
        TextView date;

        public ViewHolder(View v) {
            super(v);
            this.title = (TextView) v.findViewById(R.id.trackTitle);
            this.length = (TextView) v.findViewById(R.id.trackLength);
            this.date = (TextView) v.findViewById(R.id.trackDate);
            this.image = (ImageView) v.findViewById(R.id.trackImage);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            long trackId = tracks.get(getAdapterPosition()).getTrackId();
            Intent intent = new Intent(context, TrackActivity.class);
            intent.putExtra("TRACK_ID", trackId);
            context.startActivity(intent);
        }
    }

}
