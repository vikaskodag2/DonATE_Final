package com.sdl.app.tempdonate.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private String donationLocation;
    private Button location;
    private Bundle secondBundle;

    private TextView locationTV;

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
        locationTV = (TextView) view.findViewById(R.id.textView3);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                donationLocation = place.getName().toString();
                if(!validate())
                    return;
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(getActivity(), "An error occurred: "+status, Toast.LENGTH_SHORT).show();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validate())
                    return;
                secondBundle.putString("noofpeople", countpeople);
                secondBundle.putStringArrayList("foodList", food_list);
                secondBundle.putString("Location", donationLocation);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_home, ReceiverListFragment.newInstance(secondBundle))
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }


    private boolean validate() {
        boolean valid = true;
        if(donationLocation == null || donationLocation.isEmpty() || donationLocation.equalsIgnoreCase(" ")) {
            locationTV.setError("Not a valid location!");
            location.setEnabled(false);
            valid = false;
        }
        else
            location.setEnabled(true);
        return valid;
    }
}
