package com.sdl.app.tempdonate.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sdl.app.tempdonate.Adapter.ReceiverAdapter;
import com.sdl.app.tempdonate.R;
import com.sdl.app.tempdonate.Retrofit.APIClient;
import com.sdl.app.tempdonate.Retrofit.APIService;
import com.sdl.app.tempdonate.Retrofit.NGOList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiverListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ReceiverAdapter adapter;
    private List<NGOList> info;
    private APIService apiInterface;
    List<NGOList> finallist;

    private static int noofpeople;
    private static String loc;
    private static ArrayList<String> food_list;

    public static ArrayList<String> getFood_list() {
        return food_list;
    }

    public static void setFood_list(ArrayList<String> food_list) {
        ReceiverListFragment.food_list = food_list;
    }

    public static int getNoofpeople() {
        return noofpeople;
    }

    public static void setNoofpeople(int noofpeople) {
        ReceiverListFragment.noofpeople = noofpeople;
    }

    public static String getLoc() {
        return loc;
    }

    public static void setLoc(String loc) {
        ReceiverListFragment.loc = loc;
    }

    public ReceiverListFragment() {
        // Required empty public constructor
    }

    public static ReceiverListFragment newInstance(Bundle bundle) {
        if(bundle.getString("noofpeople") != null && bundle.getString("Location") != null && bundle.getStringArrayList("foodList") != null) {
            noofpeople = Integer.parseInt(bundle.getString("noofpeople"));
            loc = bundle.getString("Location");
            food_list = bundle.getStringArrayList("foodList");
        }
        ReceiverListFragment fragment = new ReceiverListFragment();
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
        View view = inflater.inflate(R.layout.fragment_receiver_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.ngo_receiver_list);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Log.d("LOCATION_TAG: ", loc);
        Log.d("PEOPLE_TAG: ", ""+noofpeople);
        Log.d("FOOD_TAG: ", food_list.get(0).toString());

        apiInterface = APIClient.getClient().create(APIService.class);

        Call<List<NGOList>> call = apiInterface.getOrgList();

        call.enqueue(new Callback<List<NGOList>>() {
            @Override
            public void onResponse(Call<List<NGOList>> call, Response<List<NGOList>> response) {
                Log.e("imp", "onResponse: ");
                finallist = response.body();
                Context ct = getContext();
                adapter = new ReceiverAdapter(finallist, ct);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<NGOList>> call, Throwable t) {
                Log.e("oyeoye", "onFailure: ");
                t.printStackTrace();
                Toast.makeText(getContext(), "Error in fetching content!", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
