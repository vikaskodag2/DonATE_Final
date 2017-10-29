package com.sdl.app.tempdonate.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sdl.app.tempdonate.R;

import static com.sdl.app.tempdonate.R.id.image_in_Viewpager;

/**
 * Created by vishvanatarajan on 20/10/17.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private int[] images = {R.drawable.carousel_pic_one, R.drawable.carousel_pic_two, R.drawable.carousel_pic_three, R.drawable.carousel_pic_four};
    private LayoutInflater layoutInflater;
    private Context context;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.carousel_swipe, container, false);
        ImageView imageView = (ImageView) view.findViewById(image_in_Viewpager);
        imageView.setImageResource(images[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
