package com.droidninja.imageeditengine.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.droidninja.imageeditengine.PhotoEditorFragment;
import com.droidninja.imageeditengine.R;
import com.droidninja.imageeditengine.brush.ColorPicker;
import com.droidninja.imageeditengine.utils.KeyboardHeightProvider;
import com.droidninja.imageeditengine.utils.MultiTouchListener;
import com.droidninja.imageeditengine.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import static com.droidninja.imageeditengine.PhotoEditorFragment.MODE_ADD_TEXT;
import static com.droidninja.imageeditengine.PhotoEditorFragment.MODE_NONE;
import static com.droidninja.imageeditengine.PhotoEditorFragment.addTextButton;
import static com.droidninja.imageeditengine.PhotoEditorFragment.back_color;
import static com.droidninja.imageeditengine.PhotoEditorFragment.back_iv;
import static com.droidninja.imageeditengine.PhotoEditorFragment.currentMode;
import static com.droidninja.imageeditengine.PhotoEditorFragment.paintButton;
import static com.droidninja.imageeditengine.PhotoEditorFragment.share;
import static com.droidninja.imageeditengine.PhotoEditorFragment.stickerButton;

public class PhotoEditorView extends FrameLayout implements ViewTouchListener, KeyboardHeightProvider.KeyboardHeightObserver {
    public static  RelativeLayout container;
    public static RecyclerView recyclerView;
    private int currentFont = 0;
    CustomPaintView customPaintView;
    private String folderName;
    public static ImageView imageView;
    private ImageView deleteView;
    private ViewTouchListener viewTouchListener;
    private View selectedView;
    private int selectViewIndex;
    private int left, right, top, bottom;
    TextView add_text_done_tv;
    public ColorPicker color_picker;
    private EditText inputTextET;
    private KeyboardHeightProvider keyboardHeightProvider;
    private float initialY;
    public static View containerView;
    LottieAnimationView animation_view;
    int mColorCode;
    AutofitTextView autofitTextView;
    public RequestQueue requestQueue;
    public LayoutInflater mLayoutInflater;
    boolean sticker_show = false;
    public static ImageView mainImageView;
    public static LottieAnimationView animationView;
    public static FrameLayout layout;
    String[] fonts;
    private Typeface typeface;
    ImageView imageButtonFontChanges, imageButtonAlignmentChanges, image_font;
    ArrayList<String> sticker;

    private static final int ZOOM = 2;
    private TextView txtText;
    private TextView tvemail;
    private int selectTextId;


    public PhotoEditorView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PhotoEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PhotoEditorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(final Context context, AttributeSet attrs, int defStyle) {
        View view = inflate(getContext(), R.layout.photo_editor_view, null);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainImageView = view.findViewById(R.id.image_iv);
        mainImageView.setAdjustViewBounds(true);
        imageButtonFontChanges = view.findViewById(R.id.imageButtonFontChanges);
        image_font = view.findViewById(R.id.font_bold);
        imageButtonAlignmentChanges = view.findViewById(R.id.imageButtonAlignmentChanges);
        animationView = view.findViewById(R.id.animation_view);
        color_picker = view.findViewById(R.id.color_picker);
        add_text_done_tv = view.findViewById(R.id.add_text_done_tv);
        fonts = getResources().getStringArray(R.array.fonts);

        layout = view.findViewById(R.id.layout);
        animation_view = view.findViewById(R.id.animation_view);
        container = view.findViewById(R.id.container);
        containerView = view.findViewById(R.id.container_view);
        recyclerView = view.findViewById(R.id.recyclerview);
        inputTextET = view.findViewById(R.id.add_text_et);
        inputTextET.setCursorVisible(true);
        inputTextET.setFocusable(true);
//    customPaintView = view.findViewById(R.id.paint_view);

        requestQueue = Volley.newRequestQueue(getContext());
        sticker = new ArrayList<>();

        keyboardHeightProvider = new KeyboardHeightProvider((Activity) getContext());
        keyboardHeightProvider.setKeyboardHeightObserver(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        View rootViewText = mLayoutInflater.inflate(R.layout.view_text_layout, null);
        View rootView1Text = mLayoutInflater.inflate(R.layout.view_email_layout, null);
        txtText = rootViewText.findViewById(R.id.tvPhotoEditorText);
        tvemail = rootView1Text.findViewById(R.id.tvEmailEditorText);

//        txtText.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                paintButton.setVisibility(View.GONE);
//                color_picker.setVisibility(VISIBLE);
//                add_text_done_tv.setVisibility(VISIBLE);
//                imageButtonFontChanges.setVisibility(VISIBLE);
//                image_font.setVisibility(VISIBLE);
//                imageButtonAlignmentChanges.setVisibility(VISIBLE);
//                recyclerView.setVisibility(GONE);
//            }
//        });
//
//        tvemail.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                paintButton.setVisibility(View.GONE);
//                color_picker.setVisibility(VISIBLE);
//                add_text_done_tv.setVisibility(VISIBLE);
//                imageButtonFontChanges.setVisibility(VISIBLE);
//                image_font.setVisibility(VISIBLE);
//                imageButtonAlignmentChanges.setVisibility(VISIBLE);
//                recyclerView.setVisibility(GONE);
//            }
//        });

        sticker_list();

        view.post(new Runnable() {
            @Override
            public void run() {
            }
        });

        inputTextET.post(new Runnable() {
            @Override
            public void run() {
                initialY = inputTextET.getY();
            }
        });

        imageButtonFontChanges.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                inputTextET.post(new Runnable() {


                    @Override
                    public void run() {
                        currentFont++;
                        if (currentFont >= fonts.length) currentFont = 0;
                        String path = fonts[currentFont];
                        typeface = Typeface.createFromAsset(context.getAssets(), path);
                        inputTextET.setTypeface(typeface);


                        if(selectTextId == 1)
                        {
                            txtText.setTypeface(typeface);
                        }
                        else if(selectTextId == 2)
                        {
                            tvemail.setTypeface(typeface);
                        }
                        Log.d("FontInfo", path);
                        imageButtonAlignmentChanges.setVisibility(VISIBLE);
                    }
                });
            }
        });
        image_font.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                inputTextET.setTypeface(typeface, Typeface.BOLD);
              if(selectTextId == 1)
              {
                  txtText.setTypeface(typeface, Typeface.BOLD);
              }
              else if(selectTextId == 2)
              {
                  tvemail.setTypeface(typeface, Typeface.BOLD);
              }
            }
        });

        imageButtonAlignmentChanges.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                changeAlignment(v);
            }
        });

        color_picker.setColorPickerListener(new ColorPicker.ColorPickerListener() {
            @Override
            public void onBeganColorPicking() {
            }

            @Override
            public void onColorValueChanged(int color) {
            }

            @Override
            public void onFinishedColorPicking(int color) {

                inputTextET.setTextColor(color);
                if (selectTextId == 1) {
                    txtText.setTextColor(color);
                } else if (selectTextId == 2) {
                    tvemail.setTextColor(color);
                }
                String path = fonts[0];
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), path);
            }

            @Override
            public void onSettingsPressed() {
            }
        });


        add_text_done_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Status", String.valueOf(selectedView));
                if (selectedView != null) {
                    ((AutofitTextView) selectedView).setText(inputTextET.getText());
                    ((AutofitTextView) selectedView).setTextColor(inputTextET.getTextColors());
                    ((AutofitTextView) selectedView).setTypeface(inputTextET.getTypeface());
                    ((AutofitTextView) selectedView).setTextAlignment(inputTextET.getTextAlignment());
                    Utility.hideSoftKeyboard((Activity) getContext());
                } else {
                    createText(inputTextET.getText().toString(), inputTextET.getTypeface());
                    Utility.hideSoftKeyboard((Activity) getContext());
                }

                inputTextET.setVisibility(INVISIBLE);
                imageButtonFontChanges.setVisibility(GONE);
                image_font.setVisibility(GONE);
                imageButtonAlignmentChanges.setVisibility(GONE);
                color_picker.setVisibility(GONE);
                add_text_done_tv.setVisibility(GONE);
                stickerButton.setVisibility(VISIBLE);
                addTextButton.setVisibility(VISIBLE);
                paintButton.setVisibility(VISIBLE);
                share.setVisibility(VISIBLE);
                back_color.setVisibility(GONE);
                back_iv.setVisibility(VISIBLE);
                PhotoEditorFragment.currentMode = MODE_NONE;
            }
        });
        addView(view);
    }
  public void changeAlignment(View view) {
    int alignment = txtText.getTextAlignment();
//    container.setGravity(Gravity.CENTER | Gravity.BOTTOM);

      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      layoutParams.gravity=Gravity.LEFT;
      txtText.setLayoutParams(layoutParams);


//    txtText.setGravity(Gravity.CENTER | Gravity.BOTTOM);

//    if (alignment == View.TEXT_ALIGNMENT_CENTER) {
//      inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//      txtText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//      inputTextET.setGravity(Gravity.RIGHT);
//      inputTextET.setGravity(Gravity.CENTER_VERTICAL);
//        txtText.setGravity(Gravity.RIGHT);
//        txtText.setGravity(Gravity.CENTER_VERTICAL);
//      imageButtonAlignmentChanges.setImageResource(R.drawable.font_right);
//    } else if(alignment == View.TEXT_ALIGNMENT_TEXT_END) {
//      inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//      inputTextET.setGravity(Gravity.LEFT);
//      inputTextET.setGravity(Gravity.CENTER_VERTICAL);
//        txtText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//        txtText.setGravity(Gravity.LEFT);
//        txtText.setGravity(Gravity.CENTER_VERTICAL);
//      imageButtonAlignmentChanges.setImageResource(R.drawable.font_left);
//    }else if(alignment == View.TEXT_ALIGNMENT_TEXT_START) {
//      inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//      imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//      inputTextET.setGravity(Gravity.CENTER);
//    }
  }
//    public void changeAlignment(View view) {
//        int alignment = inputTextET.getTextAlignment();
//        int textAlignment = txtText.getGravity();
//        int emailAlignment = tvemail.getGravity();
//
//
////    if(selectTextId == 1)
////    {
////        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
////        txtText.setLayoutParams(layoutParams);
////        imageButtonAlignmentChanges.setImageResource(R.drawable.font_right);
////
////    }
////    else {
////        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
////        layoutParams.gravity=Gravity.CENTER;
////        tvemail.setLayoutParams(layoutParams);
////        imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
////    }
//
//
////        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
////                layoutParams.gravity=Gravity.RIGHT;
////                txtText.setLayoutParams(layoutParams);
////        tvemail.setLayoutParams(layoutParams);
//
//        if (selectTextId == 1) {
//
//            if ( textAlignment ==  8388659) {
//                if (selectTextId == 1) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    txtText.setGravity(80);
//
//                    txtText.setLayoutParams(layoutParams);
//
//
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_right);
//                } else if (selectTextId == 2) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=2;
//
//                    tvemail.setLayoutParams(layoutParams);
//
//
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_right);
//                } else {
//                    inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//                    inputTextET.setGravity(Gravity.RIGHT);
//                    inputTextET.setGravity(Gravity.CENTER_VERTICAL);
//
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_right);
//                }
//
//            } else if (textAlignment == 80 ) {
//
//                if (selectTextId == 1) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=Gravity.CENTER;
//
//                    txtText.setGravity(17);
//                    txtText.setTextAlignment(1);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_left);
//                } else if (selectTextId == 2) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=Gravity.START;
//
//                    tvemail.setLayoutParams(layoutParams);
//                    tvemail.setTextAlignment(1);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_left);
//                } else {
//                    inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//                    inputTextET.setGravity(Gravity.LEFT);
//                    inputTextET.setGravity(Gravity.CENTER_VERTICAL);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_left);
//                }
//
//            } else if ( textAlignment == 17 ) {
//                if (selectTextId == 1) {
////                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
////                    layoutParams.gravity=Gravity.LEFT;
////                    txtText.setLayoutParams(layoutParams);
//                    txtText.setGravity(3);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                } else if (selectTextId == 2) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity= 4;
//                    tvemail.setLayoutParams(layoutParams);
//                    tvemail.setTextAlignment(4);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                } else {
//                    inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                    inputTextET.setGravity(Gravity.CENTER);
//                }
//
//
//            }
//            else if (textAlignment == 3 ) {
//                if (selectTextId == 1) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=Gravity.RIGHT;
//                    txtText.setLayoutParams(layoutParams);
//                    txtText.setGravity(5);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                } else if (selectTextId == 2) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=3;
//                    tvemail.setTextAlignment(3);
//                    tvemail.setGravity(3);
//                    tvemail.setTextAlignment(3);
//                    tvemail.setLayoutParams(layoutParams);
//
//
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                } else {
//                    txtText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                    txtText.setGravity(Gravity.CENTER);
//                }
//
//
//            }
//
//
//        }
//        else  if(selectTextId == 2)
//        {
//
//            if (emailAlignment == 8388659) {
//                if (selectTextId == 1) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=2;
//                    txtText.setTextAlignment(2);
//                    txtText.setLayoutParams(layoutParams);
//
//
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_right);
//                } else if (selectTextId == 2) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=Gravity.CENTER;
//
//                    tvemail.setLayoutParams(layoutParams);
//
//
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_right);
//                } else {
//                    inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//                    inputTextET.setGravity(Gravity.RIGHT);
//                    inputTextET.setGravity(Gravity.CENTER_VERTICAL);
//
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_right);
//                }
//
//            } else if (emailAlignment == 2) {
//
//                if (selectTextId == 1) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=1;
//
//                    txtText.setLayoutParams(layoutParams);
//                    txtText.setTextAlignment(1);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_left);
//                } else if (selectTextId == 2) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=1;
//
//                    tvemail.setLayoutParams(layoutParams);
//                    tvemail.setTextAlignment(1);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_left);
//                } else {
//                    inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//                    inputTextET.setGravity(Gravity.LEFT);
//                    inputTextET.setGravity(Gravity.CENTER_VERTICAL);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_left);
//                }
//
//            } else if ( emailAlignment == 3) {
//                if (selectTextId == 1) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=4;
//                    txtText.setLayoutParams(layoutParams);
//                    txtText.setTextAlignment(4);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                } else if (selectTextId == 2) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity= 4;
//                    tvemail.setLayoutParams(layoutParams);
//                    tvemail.setTextAlignment(4);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                } else {
//                    inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                    inputTextET.setGravity(Gravity.CENTER);
//                }
//
//
//            }
//            else if (emailAlignment == 4) {
//                if (selectTextId == 1) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=3;
//                    txtText.setLayoutParams(layoutParams);
//                    txtText.setTextAlignment(3);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                } else if (selectTextId == 2) {
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.gravity=3;
//                    tvemail.setTextAlignment(3);
//                    tvemail.setGravity(3);
//                    tvemail.setTextAlignment(3);
//                    tvemail.setLayoutParams(layoutParams);
//
//
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                } else {
//                    txtText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                    imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
//                    txtText.setGravity(Gravity.CENTER);
//                }
//
//
//            }
//        }
//
//
//
//
//    }

    public void addImage(Bitmap desiredImage, String path) {


        View rootView = mLayoutInflater.inflate(R.layout.img_layout, null);
        final ImageView imageView = rootView.findViewById(R.id.imgPhotoEditorImage);

        if (path != null) {
            Picasso.get().load(path).into(imageView);
        } else {
            imageView.setImageBitmap(desiredImage);
        }
        Random random = new Random();
        int randomNumber = random.nextInt(10) + 65;
        imageView.setPadding(100, 200, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView, container, this.imageView, true, this, layoutParams);
        multiTouchListener.setOnMultiTouchListener(new MultiTouchListener.OnMultiTouchListener() {
            @Override
            public void
            onRemoveViewListener(View removedView) {
                selectTextId = 3;
                container.removeView(removedView);
                imageView.setImageDrawable(null);

                selectedView = null;
            }
        });

        rootView.setOnTouchListener(multiTouchListener);
        container.addView(rootView);


    }


    public void setColor(int selectedColor) {
        customPaintView.setColor(selectedColor);
    }

    public int getColor() {
        mColorCode = getResources().getColor(R.color.white);
        return mColorCode;
    }

    public Bitmap getPaintBit() {
        return customPaintView.getPaintBit();
    }

    public void hidePaintView() {
        containerView.bringToFront();
    }

    //text mode methods
    public void setImageView(ImageView imageView, ImageView deleteButton, ViewTouchListener viewTouchListener) {
        this.imageView = imageView;
        this.deleteView = deleteButton;
        this.viewTouchListener = viewTouchListener;
    }

    public void setTextColor(int selectedColor) {
        AutofitTextView autofitTextView = null;
        if (selectedView != null) {
            autofitTextView = (AutofitTextView) selectedView;
            autofitTextView.setTextColor(selectedColor);
            ;
        } else {
            View view = getViewChildAt(selectViewIndex);
            if (view != null && view instanceof TextView) {
                autofitTextView = (AutofitTextView) view;
                autofitTextView.setTextColor(selectedColor);
            }
        }
        inputTextET.setTextColor(selectedColor);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addText() {
        inputTextET.setVisibility(VISIBLE);
        color_picker.setVisibility(VISIBLE);
        add_text_done_tv.setVisibility(VISIBLE);
        imageButtonFontChanges.setVisibility(VISIBLE);
        image_font.setVisibility(VISIBLE);
        imageButtonAlignmentChanges.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);
    containerView.bringToFront();
        inputTextET.setText(null);
        inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Utility.showSoftKeyboard((Activity) getContext(), inputTextET);
        createText(inputTextET.getText().toString(), inputTextET.getTypeface());
    }


    public void festivalAddText() {
        color_picker.setVisibility(VISIBLE);
        add_text_done_tv.setVisibility(VISIBLE);
        imageButtonFontChanges.setVisibility(VISIBLE);
        image_font.setVisibility(VISIBLE);
        imageButtonAlignmentChanges.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);
//    containerView.bringToFront();

        Utility.showSoftKeyboard((Activity) getContext(), inputTextET);
        createText(inputTextET.getText().toString(), inputTextET.getTypeface());
    }

//    public void setEmailText(String text, int left, int top) {
//        View rootViewText = mLayoutInflater.inflate(R.layout.view_email_layout, null);
//        tvemail = rootViewText.findViewById(R.id.tvEmailEditorText);
////        final FrameLayout frmBorder = rootViewText.findViewById(R.id.frmBorder);
//        tvemail.setText(text);
//        tvemail.setPadding(left, 300, 0, 0);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
//        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView, container, this.imageView, true, this, layoutParams);
//        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
//            @Override
//            public void onClick(View currentView) {
//                selectTextId = 2;
//                color_picker.setVisibility(VISIBLE);
//                add_text_done_tv.setVisibility(GONE);
//                imageButtonFontChanges.setVisibility(VISIBLE);
//                image_font.setVisibility(VISIBLE);
//                imageButtonAlignmentChanges.setVisibility(VISIBLE);
//                recyclerView.setVisibility(GONE);
//                selectedView = currentView;
//                selectViewIndex = currentView.getId();
//            }
//
//            @Override
//            public void onLongClick() {
//
//            }
//
//
//        });
//
//        tvemail.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                selectTextId = 2;
//                color_picker.setVisibility(VISIBLE);
//                add_text_done_tv.setVisibility(GONE);
//                imageButtonFontChanges.setVisibility(VISIBLE);
//                image_font.setVisibility(VISIBLE);
//                imageButtonAlignmentChanges.setVisibility(VISIBLE);
//                recyclerView.setVisibility(GONE);
//                selectedView = view;
//                selectViewIndex = view.getId();
//                return false;
//            }
//        });
//
//        rootViewText.setOnTouchListener(multiTouchListener);
//        container.addView(rootViewText);
//    }


//    public void setText(String text, int left, int top) {
//        View rootViewText = mLayoutInflater.inflate(R.layout.view_text_layout, null);
//
//
//        txtText = rootViewText.findViewById(R.id.tvPhotoEditorText);
//
////        final FrameLayout frmBorder = rootViewText.findViewById(R.id.frmBorder);
//        txtText.setText(text);
//        txtText.setPadding(left, top, 0, 0);
//        txtText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView, container, this.imageView, true, this, layoutParams);
//        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
//            @Override
//            public void onClick(View currentView) {
//                selectTextId = 1;
//                color_picker.setVisibility(VISIBLE);
//                add_text_done_tv.setVisibility(GONE);
//                imageButtonFontChanges.setVisibility(VISIBLE);
//                image_font.setVisibility(VISIBLE);
//                imageButtonAlignmentChanges.setVisibility(VISIBLE);
//                recyclerView.setVisibility(GONE);
//                selectedView = currentView;
//                selectViewIndex = currentView.getId();
//            }
//
//            @Override
//            public void onLongClick() {
//
//            }
//
//
//        });
//        txtText.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                selectTextId = 1;
//                color_picker.setVisibility(VISIBLE);
//                add_text_done_tv.setVisibility(GONE);
//                imageButtonFontChanges.setVisibility(VISIBLE);
//                image_font.setVisibility(VISIBLE);
//                imageButtonAlignmentChanges.setVisibility(VISIBLE);
//                recyclerView.setVisibility(GONE);
//                selectedView = view;
//                selectViewIndex = view.getId();
//                return false;
//            }
//        });
//
//        rootViewText.setOnTouchListener(multiTouchListener);
//        container.addView(rootViewText,layoutParams);
//    }

    public void hideTextMode() {
        Utility.hideSoftKeyboard((Activity) getContext());
        inputTextET.setVisibility(INVISIBLE);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
        containerView.setOnTouchListener(l);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createText(String text, Typeface typeface) {
        autofitTextView = (AutofitTextView) LayoutInflater.from(getContext()).inflate(R.layout.text_editor, null);
        autofitTextView.setId(container.getChildCount());
        autofitTextView.setText(text);
        autofitTextView.setTypeface(typeface);
        autofitTextView.setTextColor(inputTextET.getCurrentTextColor());
        autofitTextView.setTypeface(inputTextET.getTypeface());
        autofitTextView.setTextAlignment(inputTextET.getTextAlignment());
        autofitTextView.setTextSize(30);
        autofitTextView.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView, container, this.imageView, true, this, layoutParams);
        multiTouchListener.setOnMultiTouchListener(new MultiTouchListener.OnMultiTouchListener() {
            @Override
            public void onRemoveViewListener(View removedView) {
                container.removeView(removedView);
                inputTextET.setText(null);
                inputTextET.setVisibility(INVISIBLE);
                selectedView = null;
            }
        });

        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {
                if (currentView != null) {
                    selectedView = currentView;
                    selectViewIndex = currentView.getId();
                    inputTextET.setVisibility(VISIBLE);
                    inputTextET.setText(((AutofitTextView) currentView).getText());
                    ((AutofitTextView) currentView).setText("");
                    inputTextET.setSelection(inputTextET.getText().length());
                    color_picker.setVisibility(VISIBLE);
                    currentMode = MODE_ADD_TEXT;
//          visibleView();
                    add_text_done_tv.setVisibility(VISIBLE);
                    imageButtonAlignmentChanges.setVisibility(VISIBLE);
                    imageButtonFontChanges.setVisibility(VISIBLE);
                    image_font.setVisibility(VISIBLE);
                    Log.i("ViewNum", ":" + selectViewIndex + " " + ((AutofitTextView) currentView).getText());
                }
                Utility.showSoftKeyboard((Activity) getContext(), inputTextET);
            }

            @Override
            public void onLongClick() {
            }
        });

        autofitTextView.setOnTouchListener(multiTouchListener);

        container.addView(autofitTextView, layoutParams);

        selectViewIndex = container.getChildAt(container.getChildCount() - 1).getId();
        selectedView = null;
    }

    @Override
    public void onStartViewChangeListener(View view) {
        Utility.hideSoftKeyboard((Activity) getContext());
        if (viewTouchListener != null) {
            viewTouchListener.onStartViewChangeListener(view);
        }
    }

    @Override
    public void onStopViewChangeListener(View view) {
        if (viewTouchListener != null) {
            viewTouchListener.onStopViewChangeListener(view);
        }
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {

    }

    private View getViewChildAt(int index) {
        if (index > container.getChildCount() - 1) {
            return null;
        }
        return container.getChildAt(index);
    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {
        if (height == 0) {
            inputTextET.setY(initialY);
            inputTextET.requestLayout();
        } else {

            float newPosition = initialY - height;
            inputTextET.setY(newPosition);
            inputTextET.requestLayout();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        keyboardHeightProvider.close();
    }

    public void showStickers(String stickersFolder) {
        if (sticker_show == false) {
            recyclerView.setVisibility(View.VISIBLE);
            sticker_list();
            sticker_show = true;
        } else {
            recyclerView.setVisibility(View.GONE);
            sticker_show = false;
        }
        containerView.bringToFront();
        inputTextET.setVisibility(GONE);
        Utility.hideSoftKeyboard((Activity) getContext());
    }


    public void getImage() {
//   URL url = null;
//   try {
//     url = new URL("https://www.kumbhdesign.in/mobile-app/depost/api/assets/company_logo/723/IMG-20200520-WA0005.jpg");
//   } catch (MalformedURLException e) {
//     e.printStackTrace();
//   }
//   Bitmap bmp = null;
//   try {
//     bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//   } catch (IOException e) {
//     e.printStackTrace();
//   }
//   String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bmp, "Image Description", null);
        final AutofitTextView stickerImageView = (AutofitTextView) LayoutInflater.from(getContext()).inflate(R.layout.sticker_view, null);
        sticker_show = false;

        stickerImageView.setId(container.getChildCount());

        stickerImageView.setMaxTextSize(1000);
        stickerImageView.setText("jdhjshdhjshd");
        stickerImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView, container, this.imageView, true, this, layoutParams);
//   multiTouchListener.setOnMultiTouchListener(new MultiTouchListener.OnMultiTouchListener() {
//     @Override public void
//     onRemoveViewListener(View removedView) {
//       container.removeView(removedView);
//       stickerImageView.setText(null);
//       stickerImageView.setVisibility(INVISIBLE);
//       selectedView = null;
//     }
//   });

//   multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
//     @Override public void onClick(View currentView) {
//       if(currentView!=null) {
//         selectedView = currentView;
//         selectViewIndex = currentView.getId();
//       }
//       Utility.showSoftKeyboard((Activity) getContext(), inputTextET);
//     }
//
//     @Override
//     public void onLongClick() {
//     }
//   });
        stickerImageView.setOnTouchListener(multiTouchListener);
        container.addView(stickerImageView, layoutParams);
//   imageView.setImageBitmap(bmp);
    }

    public void hideStickers() {
        recyclerView.setVisibility(GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onItemClick(String path) {
        recyclerView.setVisibility(GONE);
        final AutofitTextView stickerImageView = (AutofitTextView) LayoutInflater.from(getContext()).inflate(R.layout.sticker_view, null);
        sticker_show = false;
        stickerImageView.setText(path);
        stickerImageView.setId(container.getChildCount());

        stickerImageView.setTextSize(56);
        stickerImageView.setMaxTextSize(1000);
        stickerImageView.setText(path);
        stickerImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView, container, this.imageView, true, this, layoutParams);
        multiTouchListener.setOnMultiTouchListener(new MultiTouchListener.OnMultiTouchListener() {
            @Override
            public void
            onRemoveViewListener(View removedView) {
                container.removeView(removedView);
                stickerImageView.setText(null);
                stickerImageView.setVisibility(INVISIBLE);
                selectedView = null;
            }
        });

        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {
                if (currentView != null) {
                    selectedView = currentView;
                    selectViewIndex = currentView.getId();
                }
                Utility.showSoftKeyboard((Activity) getContext(), inputTextET);
            }

            @Override
            public void onLongClick() {
            }
        });

        stickerImageView.setOnTouchListener(multiTouchListener);
        container.addView(stickerImageView, layoutParams);
    }

    public void reset() {
        container.removeAllViews();
        customPaintView.reset();
        invalidate();
    }

    public void sticker_list() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        EmojiAdapter emojiAdapter = new EmojiAdapter();
        recyclerView.setAdapter(emojiAdapter);
    }

    public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {

        ArrayList<String> emojisList = getEmojis(getContext());

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_emoji, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.txtEmoji.setText(emojisList.get(position));
        }

        @Override
        public int getItemCount() {
            return emojisList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtEmoji;

            ViewHolder(View itemView) {
                super(itemView);
                txtEmoji = itemView.findViewById(R.id.txtEmoji);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick(emojisList.get(getLayoutPosition()));
                        recyclerView.setVisibility(GONE);

                        //dismiss();
                    }
                });
            }
        }
    }

    public static ArrayList<String> getEmojis(Context context) {
        ArrayList<String> convertedEmojiList = new ArrayList<>();
        String[] emojiList = context.getResources().getStringArray(R.array.photo_editor_emoji);
        for (String emojiUnicode : emojiList) {
            convertedEmojiList.add(convertEmoji(emojiUnicode));
        }
        return convertedEmojiList;
    }

    private static String convertEmoji(String emoji) {
        String returnedEmoji;
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            returnedEmoji = new String(Character.toChars(convertEmojiToInt));
        } catch (NumberFormatException e) {
            returnedEmoji = "";
        }
        return returnedEmoji;
    }
}