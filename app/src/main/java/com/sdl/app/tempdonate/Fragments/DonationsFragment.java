package com.sdl.app.tempdonate.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sdl.app.tempdonate.Adapter.MydonationsAdapter;
import com.sdl.app.tempdonate.R;
import com.sdl.app.tempdonate.Retrofit.APIClient;
import com.sdl.app.tempdonate.Retrofit.APIService;
import com.sdl.app.tempdonate.Retrofit.NGOList;

import java.text.ParseException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonationsFragment extends Fragment {

    SharedPreferences sharedPreferences;
    APIService apiInterface;
    MydonationsAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    public DonationsFragment() {
        // Required empty public constructor
    }

    public static DonationsFragment newInstance() {
        DonationsFragment fragment = new DonationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donations, container, false);

        sharedPreferences = getActivity().getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        recyclerView = (RecyclerView) view.findViewById(R.id.mydonatefood);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        apiInterface = APIClient.getClient().create(APIService.class);
        Call<List<NGOList>> call = apiInterface.getUserDonations(token);

        call.enqueue(new Callback<List<NGOList>>() {
            @Override
            public void onResponse(Call<List<NGOList>> call, Response<List<NGOList>> response) {
                List<NGOList> mydon = response.body();
                try {
                    Log.d("DonationsFragment:", ""+mydon.get(mydon.size()-1).getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                adapter = new MydonationsAdapter(mydon);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<NGOList>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Error in fetching data!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
