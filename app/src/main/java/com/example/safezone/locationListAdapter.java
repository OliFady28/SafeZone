package com.example.safezone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class locationListAdapter extends RecyclerView.Adapter<locationListAdapter.locationListViewHolder> {

    private List<locationList> LocationList;

    public locationListAdapter(List<locationList> locationList) {
        this.LocationList = locationList;
    }

    public class locationListViewHolder extends RecyclerView.ViewHolder {
        TextView locationName , visitorsCounter,positiveVisitorsCounter;

        public locationListViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName) ;
            visitorsCounter = itemView.findViewById(R.id.visitorsCounter);
            positiveVisitorsCounter = itemView.findViewById(R.id.positiveVisitorsCounter);
        }
    }

    @NonNull
    @Override
    public locationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_info_list, parent, false);
        locationListViewHolder viewfinder = new locationListViewHolder(v);
        return viewfinder;
    }

    @Override
    public void onBindViewHolder(@NonNull locationListViewHolder holder, int position) {

        locationList obj = LocationList.get(position);
        holder.locationName.setText(obj.getLocationName());
        holder.visitorsCounter.setText(obj.getVisitorsCounter());
        holder.positiveVisitorsCounter.setText(obj.getPositiveVisitorsCounter());
    }



    @Override
    public int getItemCount() {
        return LocationList.size();
    }
}
