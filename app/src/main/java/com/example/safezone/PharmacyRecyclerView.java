package com.example.safezone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PharmacyRecyclerView extends RecyclerView.Adapter<PharmacyRecyclerView.PharmacyHolder> implements Filterable {

    private Context context ;
    private ArrayList<Pharmacy> pharmacies =new ArrayList<Pharmacy>();
    private ArrayList<Pharmacy> pharmaciesFiltered =new ArrayList<Pharmacy>();

    public ArrayList<Pharmacy> getPharmacies() {

        return pharmacies;
    }

    public void setPharmacies(ArrayList<Pharmacy> pharmacies) {
        this.pharmacies = pharmacies;
        this.pharmaciesFiltered=pharmacies;
        notifyDataSetChanged();
    }

    public PharmacyRecyclerView(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public PharmacyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pharmacyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_recycler_view,parent,false);
        PharmacyHolder pharmacyHolder = new PharmacyHolder(pharmacyView);
        return pharmacyHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyHolder holder, int position) {
        holder.onBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return pharmaciesFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString();
                if(key.isEmpty()){
                    pharmaciesFiltered=pharmacies;
                }
                else {
                    ArrayList<Pharmacy> pharmaciesIsFilterd =new ArrayList<Pharmacy>();
                    for (Pharmacy row:pharmacies) {
                        if (row.getPharmacyName().toLowerCase().contains(key.toLowerCase())){
                            pharmaciesIsFilterd.add(row);
                        }
                    }
                    pharmaciesFiltered=pharmaciesIsFilterd;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=pharmaciesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                pharmaciesFiltered= (ArrayList<Pharmacy>) results.values;
                notifyDataSetChanged();

            }
        };



    }

    public class PharmacyHolder extends RecyclerView.ViewHolder {
        private ImageView pharmacyImg;
        private TextView pharmacyName;
        private TextView pharmacyDeliveryTime ;
        private AppCompatButton pharmacyCall;
        public PharmacyHolder(@NonNull View itemView) {
            super(itemView);
            pharmacyImg=itemView.findViewById(R.id.pharmacyImg);
            pharmacyName=itemView.findViewById(R.id.pharmacyName);
            pharmacyDeliveryTime=itemView.findViewById(R.id.pharmacyDeliveryTime);
            pharmacyCall=itemView.findViewById(R.id.pharmacyCall);
        }
        public void onBindViewHolder(PharmacyHolder holder, int position) {
            holder.pharmacyImg.setImageResource(pharmaciesFiltered.get(position).getPharmacyImg());
            holder.pharmacyDeliveryTime.setText("Delivers within "+pharmaciesFiltered.get(position).getPharmacyDeliveryTime()+" minutes");
            holder.pharmacyName.setText(pharmaciesFiltered.get(position).getPharmacyName());
            pharmacyCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent mapsActivity = new Intent(context,maps_activity.class);
                    mapsActivity.putExtra("pharmacyName",pharmaciesFiltered.get(position).getPharmacyName());
                    context.startActivity(mapsActivity);*/
                    Intent call = new Intent(Intent.ACTION_DIAL);
                    String number =pharmaciesFiltered.get(position).getHotline();
                    if (number.isEmpty()){
                        Toast.makeText(context, "Please Enter Number", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        call.setData(Uri.parse("tel:"+number));
                        context.startActivity(call);
                    }


                }
            });

        }
    }
}

