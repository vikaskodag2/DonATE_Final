package com.sdl.app.tempdonate.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdl.app.tempdonate.R;
import com.sdl.app.tempdonate.Retrofit.NGOList;

import java.text.ParseException;
import java.util.List;

/**
 * Created by vishvanatarajan on 24/10/17.
 */

public class MydonationsAdapter extends RecyclerView.Adapter<MydonationsAdapter.MyViewHolder> {

    private List<NGOList> ngoinfos;

    public MydonationsAdapter(List<NGOList> ngoinfos) {
        this.ngoinfos = (ngoinfos);
    }


    @Override
    public MydonationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_mydonations, parent, false);

        return new MydonationsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MydonationsAdapter.MyViewHolder holder, int position) {
        holder.type.setText("Food");
        try {
            holder.date.setText("Date : " + ngoinfos.get(position).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.amount.setText("Count : " + ngoinfos.get(position).getNumber());
        holder.userto.setText("To : " + ngoinfos.get(position).getUserTo());
    }

    @Override
    public int getItemCount() {
        return ngoinfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView userto, date, type, amount;

        public MyViewHolder(View itemView) {
            super(itemView);

            type = (TextView) itemView.findViewById(R.id.donated_item);
            amount = (TextView) itemView.findViewById(R.id.donated_amount);
            userto = (TextView) itemView.findViewById(R.id.donated_to_name);
            date = (TextView) itemView.findViewById(R.id.donate_date);
        }
    }
}
