package com.sdl.app.tempdonate.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sdl.app.tempdonate.R;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class FoodFragment extends Fragment {

    private EditText fooditem, number;
    private ArrayList<String> food_list;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private Button add, location;
    private int cnt = 0;
    private int noofpeople;

    public FoodFragment() {
        // Required empty public constructor
    }

    public static FoodFragment newInstance() {
        FoodFragment fragment = new FoodFragment();
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
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        number = (EditText) view.findViewById(R.id.no_people_feed);
        fooditem = (EditText) view.findViewById(R.id.fooditem_user);
        add = (Button) view.findViewById(R.id.add_food_in_list);
        location = (Button) view.findViewById(R.id.button_add_location);
        listView = (ListView) view.findViewById(R.id.donate_fooditem_list);
        food_list = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.fooditem_listview, R.id.txtview_for_foodlist, food_list);
        listView.setAdapter(adapter);
        final ArrayList<String> listfood = new ArrayList<>();
        number.requestFocus();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateFood())
                    return;

                cnt++;
                String item = " " + cnt + ". " + fooditem.getText().toString();
                listfood.add(fooditem.getText().toString());
                food_list.add(item);
                fooditem.setText("");
                adapter.notifyDataSetChanged();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validate())
                    return;

                noofpeople = Integer.parseInt(number.getText().toString().trim());
                Bundle bundle = new Bundle();
                bundle.putString("noofpeople", String.valueOf(noofpeople));
                bundle.putStringArrayList("foodList", food_list);

                //call places auto complete fragment.
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_home, LocationFragment.newInstance(bundle))
                        .addToBackStack(null)
                        .commit();
            }
        });


        return view;
    }

    private boolean validate() {
        boolean valid = true;

        noofpeople = Integer.parseInt(number.getText().toString().trim());
        if(noofpeople <= 0) {
            number.setError("Number of people cannot be zero");
            number.requestFocus();
            valid = false;
        }
        if(food_list.isEmpty()) {
            fooditem.setError("You have not entered any food item!");
            fooditem.requestFocus();
            valid = false;
        }
        return valid;
    }

    private boolean validateFood() {
        boolean valid = true;

        String foodItemCheck = fooditem.getText().toString();
        if(!Pattern.compile("^[ A-Za-z]+$").matcher(foodItemCheck).matches() || foodItemCheck.length() < 3) {
            fooditem.setError("Not a valid food item");
            fooditem.requestFocus();
            valid = false;
        }
        return valid;
    }
}
