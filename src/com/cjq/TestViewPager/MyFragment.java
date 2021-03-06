package com.cjq.TestViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by android on 2015/5/25.
 */
public class MyFragment extends Fragment {

    private View view;
    private Handler mHandler=new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment,container,false);
        this.view = view;
        MyLinearLayout layout = (MyLinearLayout) view.findViewById(R.id.image_publish);
        ScrollView scroll = (ScrollView) view.findViewById(R.id.scroller);
        scroll.setSmoothScrollingEnabled(true);
        layout.setListener(new MyLinearLayout.ImageAddListener() {
            @Override
            public void imageAdded() {
                scroll.smoothScrollTo(0, 9999999);
            }
        });

        return view;
    }

    public Fragment setColor(int color){
        if(view!=null)
            view.setBackgroundColor(color);
        else
            new Thread(){
                @Override
                public void run() {
                    while (view==null){
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setBackgroundColor(color);
                        }
                    });
                }
            }.start();
        return this;
    }
}
