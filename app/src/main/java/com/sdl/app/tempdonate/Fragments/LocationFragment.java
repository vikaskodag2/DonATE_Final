package com.sdl.app.tempdonate.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.sdl.app.tempdonate.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class LocationFragment extends Fragment {

    private static ArrayList<String> food_list;
    private static String countpeople;
    private Button location;
    private Bundle secondBundle;

    public LocationFragment() {
        // Required empty public constructor
    }

    public static LocationFragment newInstance(Bundle bundle) {
        LocationFragment fragment = new LocationFragment();
        if(bundle.getString("noofpeople")!=null && bundle.getStringArrayList("foodList") != null) {
            countpeople = bundle.getString("noofpeople");
            food_list = bundle.getStringArrayList("foodList");
            Log.d("Count of people:", countpeople);
            Log.d("Food list[0]: ", food_list.get(0));
        }
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
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        secondBundle = new Bundle();
        location = (Button) view.findViewById(R.id.location_btn);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());

                secondBundle.putString("Location","Pune");

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondBundle.putString("noofpeople", countpeople);
                secondBundle.putStringArrayList("foodList", food_list);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_home, ReceiverListFragment.newInstance(secondBundle))
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}
