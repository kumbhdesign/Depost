package com.droidninja.imageeditengine.utils;

import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.droidninja.imageeditengine.views.ViewTouchListener;

  public class MultiTouchListener implements OnTouchListener {
  private static final int INVALID_POINTER_ID = -1;
  private final GestureDetector mGestureListener;
  private final ViewTouchListener viewTouchListener;
  private boolean isRotateEnabled = true;
  private boolean isTranslateEnabled = true;
  private boolean isScaleEnabled = true;
  private float minimumScale = 0.5f;
  private float maximumScale = 10.0f;
  private int mActivePointerId = INVALID_POINTER_ID;
  private float mPrevX, mPrevY, mPrevRawX, mPrevRawY;
  private ScaleGestureDetector mScaleGestureDetector;

  private int[] location = new int[2];
  private Rect outRect;
  private View deleteView;
  private ImageView photoEditImageView;
  private RelativeLayout parentView;

    public MultiTouchListener(ViewTouchListener viewTouchListener) {
      mGestureListener = new GestureDetector(new GestureListener());
      mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
      this.viewTouchListener = viewTouchListener;
    }

    private OnMultiTouchListener onMultiTouchListener;
  private OnGestureControl mOnGestureControl;
  private boolean mIsTextPinchZoomable;
  private View currentView;

  RelativeLayout.LayoutParams parms;
  int startwidth;
  int startheight;
  float dx = 0, dy = 0, x = 0, y = 0;
  float angle = 0;
  float scalediff;

  private static final int NONE = 0;
  private static final int DRAG = 1;
  private static final int ZOOM = 2;
  private int mode = NONE;
  private float oldDist = 1f;
  private float d = 0f;
  private float newRot = 0f;

  public MultiTouchListener(@Nullable View deleteView, RelativeLayout parentView,
                              ImageView photoEditImageView, boolean isTextPinchZoomable,
                              ViewTouchListener viewTouchListener, RelativeLayout.LayoutParams params) {
    mIsTextPinchZoomable = isTextPinchZoomable;
    this.viewTouchListener = viewTouchListener;
    mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
    mGestureListener = new GestureDetector(new GestureListener());
    this.deleteView = deleteView;
    this.parentView = parentView;
    this.photoEditImageView = photoEditImageView;
    this.parms = params;
    if (deleteView != null) {
      outRect = new Rect(deleteView.getLeft(), deleteView.getTop(), deleteView.getRight(),
              deleteView.getBottom());
    } else {
      outRect = new Rect(0, 0, 0, 0);
    }
  }


  private static float adjustAngle(float degrees) {
    if (degrees > 180.0f) {
      degrees -= 360.0f;
    } else if (degrees < -180.0f) {
      degrees += 360.0f;
    }

    return degrees;
  }

  private static void move(View view, MultiTouchListener.TransformInfo info) {
    computeRenderOffset(view, info.pivotX, info.pivotY);
    adjustTranslation(view, info.deltaX, info.deltaY);

    float scale = view.getScaleX() * info.deltaScale;
    scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale));
    view.setScaleX(scale);
    view.setScaleY(scale);

    float rotation = adjustAngle(view.getRotation() + info.deltaAngle);
    view.setRotation(rotation);
  }

  private static void adjustTranslation(View view, float deltaX, float deltaY) {
    float[] deltaVector = { deltaX, deltaY };
    view.getMatrix().mapVectors(deltaVector);
    view.setTranslationX(view.getTranslationX() + deltaVector[0]);
    view.setTranslationY(view.getTranslationY() + deltaVector[1]);
  }

  private static void computeRenderOffset(View view, float pivotX, float pivotY) {
    if (view.getPivotX() == pivotX && view.getPivotY() == pivotY) {
      return;
    }

    float[] prevPoint = { 0.0f, 0.0f };
    view.getMatrix().mapPoints(prevPoint);

    view.setPivotX(pivotX);
    view.setPivotY(pivotY);

    float[] currPoint = { 0.0f, 0.0f };
    view.getMatrix().mapPoints(currPoint);

    float offsetX = currPoint[0] - prevPoint[0];
    float offsetY = currPoint[1] - prevPoint[1];

    view.setTranslationX(view.getTranslationX() - offsetX);
    view.setTranslationY(view.getTranslationY() - offsetY);
  }

  @Override public boolean onTouch(View view, MotionEvent event) {
    this.currentView = view;
    mScaleGestureDetector.onTouchEvent(view, event);
    mGestureListener.onTouchEvent(event);

    if (!isTranslateEnabled) {
      return true;
    }

    int action = event.getAction();

    int x = (int) event.getRawX();
    int y = (int) event.getRawY();

    switch (action & event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        mPrevX = event.getX();
        mPrevY = event.getY();
        mPrevRawX = event.getRawX();
        mPrevRawY = event.getRawY();
        mActivePointerId = event.getPointerId(0);
        if (deleteView != null) {
          deleteView.setVisibility(View.VISIBLE);
        }
        view.bringToFront();
        firePhotoEditorSDKListener(view, true);
        break;
      case MotionEvent.ACTION_MOVE:
        int pointerIndexMove = event.findPointerIndex(mActivePointerId);
        if (pointerIndexMove != -1) {
          float currX = event.getX(pointerIndexMove);
          float currY = event.getY(pointerIndexMove);
          if (!mScaleGestureDetector.isInProgress()) {
            adjustTranslation(view, currX - mPrevX, currY - mPrevY);
          }
        }
        break;
      case MotionEvent.ACTION_CANCEL:
        mActivePointerId = INVALID_POINTER_ID;
        break;

      case MotionEvent.ACTION_UP:
        mActivePointerId = INVALID_POINTER_ID;
        if (deleteView != null && isViewInBounds(deleteView,(int) x, y,true)) {
          if (onMultiTouchListener != null)
            onMultiTouchListener.onRemoveViewListener(view);
        } else if (!isViewInBounds(photoEditImageView, x, y,true)) {
          view.animate().translationY(0).translationY(0);
        }
        if (deleteView != null) {
          deleteView.setVisibility(View.GONE);
        }
        firePhotoEditorSDKListener(view, false);
        break;

      case MotionEvent.ACTION_POINTER_UP:
        int pointerIndexPointerUp = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        int pointerId = event.getPointerId(pointerIndexPointerUp);
        if (pointerId == mActivePointerId) {
          int newPointerIndex = pointerIndexPointerUp == 0 ? 1 : 0;
          mPrevX = event.getX(newPointerIndex);
          mPrevY = event.getY(newPointerIndex);
          mActivePointerId = event.getPointerId(newPointerIndex);
          view.animate().translationY(0).translationY(0);
        }
        break;
    }
    return true;
  }

  private float spacing(MotionEvent event) {
    float x = event.getX(0) - event.getX(1);
    float y = event.getY(0) - event.getY(1);
    return (float) Math.sqrt(x * x + y * y);
  }

  private float rotation(MotionEvent event) {
    double delta_x = (event.getX(0) - event.getX(1));
    double delta_y = (event.getY(0) - event.getY(1));
    double radians = Math.atan2(delta_y, delta_x);
    return (float) Math.toDegrees(radians);
  }

  private void firePhotoEditorSDKListener(View view, boolean isStart) {
    if (view instanceof TextView || view instanceof ImageView) {

      if (viewTouchListener != null) {
        if (isStart) {
          viewTouchListener.onStartViewChangeListener(view);
        } else {
          viewTouchListener.onStopViewChangeListener(view);
        }
      }
    }
  }

  private boolean isViewInBounds(View view, int x, int y, boolean getLocationFromScreen) {
    view.getDrawingRect(outRect);
    view.getLocationOnScreen(location);
    Log.i("outRect:", outRect.toString());
    if (getLocationFromScreen) {
      outRect.offset(location[0], location[1]);
    }
    Log.i("viewbOunds:", outRect.toString() + " x:" + x + " y:" + y);
    return outRect.contains(x, y);
  }

  public void setOnMultiTouchListener(MultiTouchListener.OnMultiTouchListener onMultiTouchListener) {
    this.onMultiTouchListener = onMultiTouchListener;
  }

  public class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    private float mPivotX;
    private float mPivotY;
    private Vector2D mPrevSpanVector = new Vector2D();

    @Override public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
      mPivotX = detector.getFocusX();
      mPivotY = detector.getFocusY();
      mPrevSpanVector.set(detector.getCurrentSpanVector());
      return mIsTextPinchZoomable;
    }

    @Override public boolean onScale(View view, ScaleGestureDetector detector) {
      MultiTouchListener.TransformInfo info = new MultiTouchListener.TransformInfo();
      info.deltaScale = isScaleEnabled ? detector.getScaleFactor() : 1.0f;
      info.deltaAngle = isRotateEnabled ? Vector2D.getAngle(mPrevSpanVector, detector.getCurrentSpanVector()) : 0.0f;
      info.deltaX = isTranslateEnabled ? detector.getFocusX() - mPivotX : 0.0f;
      info.deltaY = isTranslateEnabled ? detector.getFocusY() - mPivotY : 0.0f;
      info.pivotX = mPivotX;
      info.pivotY = mPivotY;
      info.minimumScale = minimumScale;
      info.maximumScale = maximumScale;
      move(view, info);
      return !mIsTextPinchZoomable;
    }
  }

  public static class TransformInfo {
    float deltaX;
    float deltaY;
    float deltaScale;
    public float deltaAngle;
    float pivotX;
    float pivotY;
    float minimumScale;
    float maximumScale;
  }

  public interface OnMultiTouchListener {
    void onRemoveViewListener(View removedView);
  }

  public interface OnGestureControl {
    void onClick(View currentView);

    void onLongClick();
  }

  public void setOnGestureControl(OnGestureControl onGestureControl) {
    mOnGestureControl = onGestureControl;
  }

  public final class GestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override public boolean onSingleTapUp(MotionEvent e) {
      if (mOnGestureControl != null) {
        mOnGestureControl.onClick(currentView);
      }
      return true;
    }

    @Override public void onLongPress(MotionEvent e) {
      super.onLongPress(e);
      if (mOnGestureControl != null) {
        mOnGestureControl.onLongClick();
      }
    }
  }
}