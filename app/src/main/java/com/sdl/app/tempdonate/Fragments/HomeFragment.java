package com.sdl.app.tempdonate.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sdl.app.tempdonate.Adapter.ViewPagerAdapter;
import com.sdl.app.tempdonate.R;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private ViewPager viewPager;
    private Button donate;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private Timer timer;
    private int CUR_IMAGE = 0, NUM_IMAGES = 4;
    private final long DELAY_MS = 5000; // delay in milliseconds before image has to slide
    private final long PERIOD_MS = 4000; // time in milliseconds between image sliding.

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        donate = (Button) view.findViewById(R.id.button_donate);
        tabLayout = (TabLayout) view.findViewById(R.id.image_dots);

        tabLayout.setupWithViewPager(viewPager, true);
        viewPagerAdapter = new ViewPagerAdapter(getActivity());
        viewPager.setAdapter(viewPagerAdapter);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if(CUR_IMAGE == NUM_IMAGES) {
                    CUR_IMAGE = 0;
                }
                viewPager.setCurrentItem(CUR_IMAGE++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FoodFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_home, FoodFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}
