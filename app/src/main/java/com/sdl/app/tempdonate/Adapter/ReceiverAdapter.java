package com.sdl.app.tempdonate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdl.app.tempdonate.Activities.HomeActivity;
import com.sdl.app.tempdonate.Fragments.ReceiverListFragment;
import com.sdl.app.tempdonate.R;
import com.sdl.app.tempdonate.Retrofit.APIClient;
import com.sdl.app.tempdonate.Retrofit.APIService;
import com.sdl.app.tempdonate.Retrofit.NGOList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vishvanatarajan on 24/10/17.
 */

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverAdapter.MyViewHolder> {

    SharedPreferences sharedPreferences;
    private List<NGOList> ngoInfos;
    private Context context;

    APIService apiInterface = APIClient.getClient().create(APIService.class);

    public ReceiverAdapter(List<NGOList> ngoInfos, Context context) {
        this.ngoInfos = ngoInfos;
        this.context = context;
    }

    @Override
    public ReceiverAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_ngo_choose, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiverAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(ngoInfos.get(position).getName());
        holder.qty.setText(ngoInfos.get(position).getAmount()+"");

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = context.getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");

                //sender info
                NGOList sendinfo = new NGOList();
                //check with if userfrom is required //IMP
                //sendinfo.userFrom = getName();
                sendinfo.setUserTo(ngoInfos.get(position).getName());
                sendinfo.setAddress(ReceiverListFragment.getLoc());
                sendinfo.setNumber(ReceiverListFragment.getNoofpeople());
                sendinfo.setItems(ReceiverListFragment.getFood_list());

                Log.d("ReceiverAdapter:", ""+ReceiverListFragment.getNoofpeople());
                Log.d("ReceiverAdapter:", ReceiverListFragment.getLoc());
                Log.d("ReceiverAdapter:", String.valueOf(ReceiverListFragment.getFood_list()));

                Call<List<NGOList>> call = apiInterface.sendDonorInfo(sendinfo, token);

                call.enqueue(new Callback<List<NGOList>>() {
                    @Override
                    public void onResponse(Call<List<NGOList>> call, Response<List<NGOList>> response) {
                        Toast.makeText(context, "Thanks for Donating!!", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", ngoInfos.get(position).getName() + ":::" + position);

                        //write code to go to home screen.
                        Intent intent = new Intent(context, HomeActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<List<NGOList>> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(context, "error in sending data!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return ngoInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, qty;
        Button button;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.org_receive_name);
            qty = (TextView) itemView.findViewById(R.id.org_receive_qty);
            button = (Button) itemView.findViewById(R.id.toDonate);
        }
    }
}
