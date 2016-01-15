package com.horn.workshop;

import android.view.View;

/**
 * Created by user on 14-01-2016.
 */
public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}