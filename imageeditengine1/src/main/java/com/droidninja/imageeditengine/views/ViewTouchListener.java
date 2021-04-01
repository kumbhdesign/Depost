package com.droidninja.imageeditengine.views;

import android.view.MotionEvent;
import android.view.View;

public interface ViewTouchListener {
    boolean onTouchEvent(MotionEvent motionEvent);
    void onStartViewChangeListener(View view);
  void onStopViewChangeListener(View view);

    void onEditTextChangeListener(View rootView, String text, int colorCode);
}
