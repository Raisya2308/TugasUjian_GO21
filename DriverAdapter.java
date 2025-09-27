package com.example.ujian;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.VH> {
    ArrayList<Driver> list;
    DriverListener listener;

    public interface DriverListener {
        void onEdit(Driver d);
        void onDelete(Driver d);
        void onStartTrip(Driver d);
    }

    public DriverAdapter(ArrayList<Driver> list, DriverListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(ArrayList<Driver> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_driver, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Driver d = list.get(position);
        holder.tvName.setText(d.getName());
        holder.tvVehicle.setText(d.getVehicle());
        holder.tvPhone.setText(d.getPhone());
        holder.tvStatus.setText(d.getStatus());
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(d));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(d));
        holder.btnTrip.setOnClickListener(v -> listener.onStartTrip(d));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvVehicle, tvPhone, tvStatus;
        Button btnEdit, btnDelete, btnTrip;
        public VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvVehicle = itemView.findViewById(R.id.tvVehicle);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnTrip = itemView.findViewById(R.id.btnTrip);
        }
    }
}