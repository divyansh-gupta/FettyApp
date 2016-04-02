package com.myboi.fettyapp;

import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;

/**
 * Created by divy9677 on 4/2/16.
 */
public class CompositeOnClickListener implements OnClickListener {

    ArrayList<OnClickListener> listeners;

    public CompositeOnClickListener(OnClickListener... toComposite) {
        this();
        for (OnClickListener listener : toComposite) {
            addOnClickListener(listener);
        }
    }

    public CompositeOnClickListener() {
        listeners = new ArrayList<OnClickListener>();
    }

    public void addOnClickListener(OnClickListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onClick(View v) {
        for (OnClickListener listener : listeners) {
            listener.onClick(v);
        }
    }
}
