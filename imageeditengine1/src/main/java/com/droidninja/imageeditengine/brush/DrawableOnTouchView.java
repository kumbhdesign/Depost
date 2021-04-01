package com.droidninja.imageeditengine.brush;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.droidninja.imageeditengine.R;

import static android.view.ViewAnimationUtils.createCircularReveal;

/**
 * Created by kumbh on 12/17/2018.
 */

public class DrawableOnTouchView extends FrameLayout {

    public interface OnActionListener{
        void OnDone(Bitmap bitmap);
        void show();
        void killSelf();
    }

    public interface  OnColorChangedListener{
        void onColorChanged(int color);
        void onStrokeWidthChanged(float strokeWidth);
        void onBrushChanged(int Brushid);
    }
    private OnActionListener actionListener;

    public void setActionListener(OnActionListener actionListener) {
        this.actionListener = actionListener;
    }
    private OnColorChangedListener colorChangedListener;

    public void setColorChangedListener(final OnColorChangedListener colorChangedListener) {
        this.colorChangedListener = colorChangedListener;
        fingerPaintView.setColorPickerChanged(new FingerPaintView.OnColorPickerChanged() {
            @Override
            public void onColorChanged(int color) {
                DrawableOnTouchView.this.colorChangedListener.onColorChanged(color);
            }

            @Override
            public void onStrokeWidthChanged(float strokeWidth) {
                DrawableOnTouchView.this.colorChangedListener.onStrokeWidthChanged(strokeWidth);
            }

            @Override
            public void onBrushChanged(int Brushid) {
                DrawableOnTouchView.this.colorChangedListener.onBrushChanged(Brushid);
            }
        });
    }

    private ImageButton undo, painterIcon,cancel;
    TextView done;
    private ShaderTextView deboss_brush;
    ImageView normal_brush,inner_brush,neon_brush,emboss_brush,blur_brush;
    public static ColorPicker colorPicker;
    private FingerPaintView fingerPaintView;
    private LinearLayout select_brush_frame,strokeWidth_frame,draw_action_layout,iui_seekbar_layout;
    FrameLayout undo_frame;
    private SeekBar strokeSeekbar;
    private float brushSeekBarPositionX;
    private float sliderProgress;
    private float minImageRectSize;
    LinearLayout stroke_width_status;
    private Context context;
    private TextView strokeWidthstatus;
    private FrameLayout main_frame;
    private ImageView onDoneIv;
    private static final int MAX_WIDTH=50;
    boolean controlsHidden=false;
    private FrameLayout canvasFrame;
    public DrawableOnTouchView(Context context) {
        super(context);
        init(context);
    }

    public DrawableOnTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawableOnTouchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    private void init(Context context){
        this.context=context;
        try {
            View layout=View.inflate(context, R.layout.drawable_view_layout,null);
            addView(layout);
            bindViews(layout);
            setClickables();
        }catch (Exception e){
            Log.e("onInit():",e.getMessage());
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    public  void attachCanvas(){
        LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        canvasFrame.addView(fingerPaintView,params);
    }

    public void attachCanvas(int width,int height){
        LayoutParams params=new LayoutParams(width,height, Gravity.CENTER);
        canvasFrame.addView(fingerPaintView,params);

    }
    public void hideActions(){
        draw_action_layout.setVisibility(GONE);
    }

    public void flipOnTouch(boolean isVisible){
        int v=isVisible?GONE:VISIBLE;
        colorPicker.setVisibility(v);
        iui_seekbar_layout.setVisibility(v);
        strokeWidth_frame.setVisibility(GONE);
        undo_frame.setVisibility(v);

    }
    public void clearFromLastWatcher(){
        fingerPaintView.clearFromLastWatcher();
        fingerPaintView.clearWatcerList();
    }
    public boolean hasDrawn(){
        return fingerPaintView.hasDrawn();
    }
    public void clearWatcherList(){
        fingerPaintView.clearWatcerList();

    }
    private void enableShader(ShaderTextView shaderTextView, int filterId){
        shaderTextView.setFilterId(filterId);
        shaderTextView.setRadius(16);
        shaderTextView.enableMask();
    }
    private void bindViews(View layout){
        undo=(ImageButton)layout.findViewById(R.id.undo_btn);
        undo.setImageResource(R.drawable.undo);
        painterIcon =(ImageButton)layout.findViewById(R.id.show_stroke_bar);
        painterIcon.setImageResource(R.drawable.ic_gestures);
        painterIcon.setClickable(false);
        painterIcon.setVisibility(GONE);

        normal_brush=(ImageView) layout.findViewById(R.id.normal_brush);

        neon_brush=(ImageView) layout.findViewById(R.id.neon_brush);
        inner_brush=(ImageView)layout.findViewById(R.id.inner_brush);

        blur_brush=(ImageView) layout.findViewById(R.id.blur_brush);
//        enableShader(blur_brush, BrushType.BRUSH_BLUR);

        emboss_brush=(ImageView)layout.findViewById(R.id.emboss_brush);

        deboss_brush=(ShaderTextView)layout.findViewById(R.id.deboss_brush);
        enableShader(deboss_brush, BrushType.BRUSH_DEBOSS);

        colorPicker=(ColorPicker)layout.findViewById(R.id.color_picker);
        canvasFrame=(FrameLayout)layout.findViewById(R.id.canvas_frame);
        fingerPaintView=new FingerPaintView(context);

        float  location = context.getSharedPreferences("paint", Activity.MODE_PRIVATE).getFloat("last_color_location", 0.5f);
        fingerPaintView.setBrushColor(colorPicker.colorForLocation(location));
        fingerPaintView.setBrushStrokeWidth(12.f);

        select_brush_frame=(LinearLayout)layout.findViewById(R.id.brush_option_frame);
        strokeWidth_frame=(LinearLayout)layout.findViewById(R.id.stroke_width_layout);

        strokeSeekbar=(SeekBar)layout.findViewById(R.id.stroke_width_seekbar);
        strokeSeekbar.setMax(50);
        strokeSeekbar.setProgress(14);
        strokeSeekbar.setProgressDrawable(getResources().getDrawable(R.drawable.iui_seekbar_bg_empty));

        done=(TextView) layout.findViewById(R.id.draw_done);
        done.setText("Done");
        iui_seekbar_layout = (LinearLayout)layout.findViewById(R.id.iui_seekbar_layout);

        main_frame=(FrameLayout)layout.findViewById(R.id.draw_main_frame);
        draw_action_layout=(LinearLayout)layout.findViewById(R.id.draw_action_layout);
        onDoneIv=(ImageView)layout.findViewById(R.id.onDone_iv);
        undo_frame=(FrameLayout)layout.findViewById(R.id.undo_frame);

        showChecks();
    }
    public void setColorPicker(int color, float strokeWidth, int id){
        fingerPaintView.setBrushColor(color);
        fingerPaintView.setBrushStrokeWidth(strokeWidth);
        fingerPaintView.setBrushType(id);
    }

    public void setColorAttribute(ColorAttribute colorAttribute){
        colorPicker.setColorAttribute(colorAttribute);
    }

    public void hideChecks(){
//        cancel.setVisibility(GONE);
        done.setVisibility(GONE);
    }

    public void showChecks(){
//        cancel.setVisibility(VISIBLE);
        done.setVisibility(VISIBLE);
    }

    private void setClickables(){
        fingerPaintView.setUndoEmptyListener(new FingerPaintView.OnUndoEmptyListener() {
            @Override
            public void undoListEmpty() {
                undo.setAlpha(0.4f);
            }

            @Override
            public void redoListEmpty() {
            }

            @Override
            public void refillUndo() {
                undo.setAlpha(1.0f);
            }

            @Override
            public void OnUndoStarted() {
                // Toast.makeText(context,"Undo Started",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnUndoCompleted() {
            }

            @Override
            public void onTouchDown() {
                flipOnTouch(true);
            }

            @Override
            public void onTouchUp() {
                if(!controlsHidden)
                    flipOnTouch(false);
            }
        });

        undo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerPaintView.onUndo();
            }
        });

        painterIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                strokeWidth_frame.setVisibility(GONE);
            }
        });

        strokeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    Log.d("Stroke_Width:", String.valueOf(progress));
                    sliderProgress = (progress + 1) / minImageRectSize;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fingerPaintView.setBrushStrokeWidth(seekBar.getProgress());
            }
        });

        colorPicker.setColorPickerListener(new ColorPicker.ColorPickerListener() {
            @Override
            public void onBeganColorPicking() {

            }

            @Override
            public void onColorValueChanged(int color) {

            }

            @Override
            public void onFinishedColorPicking(int color) {

                fingerPaintView.setBrushColor(color);
            }

            @Override
            public void onSettingsPressed() {
                showBrushOptions();
            }
        });

        normal_brush.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                normal_brush.setImageDrawable(getResources().getDrawable(R.drawable.normal_bk));
                normal_brush.setPadding(0,0,0,0);
                fingerPaintView.setBrushType(BrushType.BRUSH_SOLID);
                showBrushOptions();

                neon_brush.setImageDrawable(getResources().getDrawable(R.drawable.thin));
                inner_brush.setImageDrawable(getResources().getDrawable(R.drawable.thick));
                blur_brush.setImageDrawable(getResources().getDrawable(R.drawable.blur));
                emboss_brush.setImageDrawable(getResources().getDrawable(R.drawable.pencil));
                emboss_brush.setPadding(10,10,10,10);
                blur_brush.setPadding(8,8,8,8);
                neon_brush.setPadding(10,10,10,10);
                inner_brush.setPadding(10,10,10,10);
                deboss_brush.setBackground(null);

                neon_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                inner_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
//                blur_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                emboss_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                deboss_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
            }
        });

        neon_brush.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                neon_brush.setImageDrawable(getResources().getDrawable(R.drawable.thinn_drw));
                neon_brush.setPadding(0,0,0,0);
                fingerPaintView.setBrushType(BrushType.BRUSH_NEON);
                showBrushOptions();

                normal_brush.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                normal_brush.setPadding(8,8,8,8);
                inner_brush.setImageDrawable(getResources().getDrawable(R.drawable.thick));
                inner_brush.setPadding(10,10,10,10);
                blur_brush.setImageDrawable(getResources().getDrawable(R.drawable.blur));
                blur_brush.setPadding(8,8,8,8);
                emboss_brush.setImageDrawable(getResources().getDrawable(R.drawable.pencil));
                emboss_brush.setPadding(10,10,10,10);
                deboss_brush.setBackground(null);

                normal_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                inner_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                blur_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                emboss_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                deboss_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
            }
        });

        inner_brush.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inner_brush.setImageDrawable(getResources().getDrawable(R.drawable.thick_drw));
                inner_brush.setPadding(0,0,0,0);
                fingerPaintView.setBrushType(BrushType.BRUSH_INNER);
                showBrushOptions();

                normal_brush.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                normal_brush.setPadding(8,8,8,8);
                neon_brush.setImageDrawable(getResources().getDrawable(R.drawable.thin));
                neon_brush.setPadding(10,10,10,10);
                blur_brush.setImageDrawable(getResources().getDrawable(R.drawable.blur));
                blur_brush.setPadding(8,8,8,8);
                emboss_brush.setImageDrawable(getResources().getDrawable(R.drawable.pencil));
                emboss_brush.setPadding(10,10,10,10);
                deboss_brush.setBackground(null);

                normal_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                neon_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                blur_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                emboss_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                deboss_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
            }
        });

        blur_brush.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                blur_brush.setImageDrawable(getResources().getDrawable(R.drawable.blur_bk));
                blur_brush.setPadding(0,0,0,0);
                fingerPaintView.setBrushType(BrushType.BRUSH_BLUR);
                showBrushOptions();

                normal_brush.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                normal_brush.setPadding(8,8,8,8);
                inner_brush.setImageDrawable(getResources().getDrawable(R.drawable.thick));
                neon_brush.setImageDrawable(getResources().getDrawable(R.drawable.thin));
                emboss_brush.setImageDrawable(getResources().getDrawable(R.drawable.pencil));
                emboss_brush.setPadding(10,10,10,10);
                neon_brush.setPadding(10,10,10,10);
                inner_brush.setPadding(10,10,10,10);

                deboss_brush.setBackground(null);

                normal_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                neon_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                inner_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                emboss_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                deboss_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
            }
        });

        emboss_brush.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                emboss_brush.setImageDrawable(getResources().getDrawable(R.drawable.pen_drw));
                emboss_brush.setPadding(0,0,0,0);
                emboss_brush.setScaleType(ImageView.ScaleType.CENTER_CROP);
                fingerPaintView.setBrushType(BrushType.BRUSH_EMBOSS);
                showBrushOptions();

                normal_brush.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                normal_brush.setPadding(8,8,8,8);
                inner_brush.setImageDrawable(getResources().getDrawable(R.drawable.thick));
                inner_brush.setPadding(10,10,10,10);
                blur_brush.setImageDrawable(getResources().getDrawable(R.drawable.blur));
                blur_brush.setPadding(8,8,8,8);
                neon_brush.setImageDrawable(getResources().getDrawable(R.drawable.thin));
                neon_brush.setPadding(10,10,10,10);
                deboss_brush.setBackground(null);

                normal_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                neon_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                inner_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                blur_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                deboss_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
            }
        });

        deboss_brush.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deboss_brush.setBackground(getResources().getDrawable(R.drawable.pen_drw));
                fingerPaintView.setBrushType(BrushType.BRUSH_DEBOSS);
                showBrushOptions();
                normal_brush.setBackground(null);
                inner_brush.setBackground(null);
                blur_brush.setBackground(null);
                emboss_brush.setBackground(null);
                neon_brush.setBackground(null);

                normal_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                neon_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                inner_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                blur_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
                emboss_brush.setBackground(getResources().getDrawable(R.drawable.share_back));
            }
        });


        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneIv.setImageBitmap(fingerPaintView.getmBitmap());
                fingerPaintView.setVisibility(GONE);
                if(actionListener!=null)actionListener.OnDone(fingerPaintView.getmBitmap());
                hideOnDone();
                hideActions();
            }
        });
    }

    @TargetApi(21)
    private void OnDoneAnimation(){
        int centerX = (int) (done.getX() + (done.getWidth() / 2));
        int centerY = (int) (done.getY() + (done.getHeight() / 2));
        done.setVisibility(GONE);
        Animator reveal = createCircularReveal(draw_action_layout, centerX, centerY, 0, getWidth() / 2f);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                draw_action_layout.setVisibility(GONE);
            }
        });
        reveal.start();
    }

    public Bitmap getBitmap(){
        return fingerPaintView.getmBitmap();
    }

    public void hideOnDone(){
        //OnDoneAnimation();
        controlsHidden=true;
        onDoneIv.setImageBitmap(fingerPaintView.getmBitmap());
        onDoneIv.setVisibility(VISIBLE);
        colorPicker.setVisibility(GONE);
        iui_seekbar_layout.setVisibility(GONE);
        strokeSeekbar.setVisibility(GONE);
        select_brush_frame.setVisibility(GONE);
        undo.setVisibility(GONE);
        strokeWidth_frame.setVisibility(GONE);
        main_frame.setBackgroundColor(Color.TRANSPARENT);
        draw_action_layout.setVisibility(GONE);
        fingerPaintView.setVisibility(GONE);
        painterIcon.setVisibility(GONE);
        hideActions();
    }

    public void makeNonClickable(boolean show){
        painterIcon.setClickable(true);
        undo_frame.setClickable(false);
        onDoneIv.setClickable(false);
        colorPicker.setClickable(show);
        select_brush_frame.setClickable(false);
        undo.setClickable(false);
        strokeWidth_frame.setClickable(false);
        draw_action_layout.setClickable(false);
        fingerPaintView.setClickable(show);
    }

    public void show(){
        controlsHidden=false;
        onDoneIv.setImageBitmap(null);
        onDoneIv.setVisibility(GONE);
        fingerPaintView.setVisibility(VISIBLE);
        colorPicker.setVisibility(VISIBLE);
        select_brush_frame.setVisibility(VISIBLE);
        undo.setVisibility(VISIBLE);
        strokeWidth_frame.setVisibility(GONE);
        painterIcon.setVisibility(VISIBLE);
        main_frame.setBackgroundColor(Color.TRANSPARENT);
    }

    private void showBrushOptions(){
        boolean show=select_brush_frame.getVisibility()!=VISIBLE;
        if(show) {
            select_brush_frame.setVisibility(VISIBLE);
        }else {
            select_brush_frame.setVisibility(VISIBLE);
        }
    }
}
