package com.kumbh.design.Epost.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.droidninja.imageeditengine.AnimationHelper;
import com.droidninja.imageeditengine.ImageEditActivity;
import com.droidninja.imageeditengine.brush.ColorPicker;
import com.droidninja.imageeditengine.brush.DrawableOnTouchView;
import com.droidninja.imageeditengine.utils.KeyboardHeightProvider;
import com.droidninja.imageeditengine.utils.MultiTouchListener;
import com.droidninja.imageeditengine.utils.Utility;
import com.droidninja.imageeditengine.views.AutofitTextView;
import com.droidninja.imageeditengine.views.PhotoEditorView;
import com.droidninja.imageeditengine.views.ViewTouchListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.util.ImageProcessor;
import com.kumbh.design.Epost.util.ImageScannerAdapter;
import com.kumbh.design.Epost.util.SaveToStorageUtil;
import com.kumbh.design.Epost.util.SessionManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.droidninja.imageeditengine.ImageEditor.Builder.FestivalName;
import static com.droidninja.imageeditengine.ImageEditor.Builder.UserName;
import static com.droidninja.imageeditengine.PhotoEditorFragment.MODE_ADD_TEXT;
import static com.droidninja.imageeditengine.PhotoEditorFragment.MODE_NONE;
import static com.droidninja.imageeditengine.PhotoEditorFragment.currentMode;

public class back_color extends AppCompatActivity implements ViewTouchListener, KeyboardHeightProvider.KeyboardHeightObserver {
    EditText mEditText;
    ImageView deleteButton;
    SessionManager shred_pr;
    boolean sticker_show = false;
    RelativeLayout mPictureView;
    ImageView back_iv,imageButtonChangeColor,imagebuttonsticker, imageButtonFontChanges, imageButtonFontChange,imageButtonpaint, imageButtonTextSize, imageButtonChangeAlignment;
    private DrawableOnTouchView drawableOnTouchView;
    ImageView imgshoot;
    private int currentTextSize = 0;
    private int currentColor = 0;
    private int currentFont = 0;
    View toolbarLayout;
    Button imageButtonSaveImage;
    FragmentHostCallback mHost;
    private PhotoEditorView photoEditorView;
    private View selectedView;
    private int selectViewIndex;
    SessionManager shared_pr;
    int actionId;
    private boolean isTextPinchZoomable;
    ArrayList<String> sticker;
    public RequestQueue requestQueue;
    private String folderName;
    boolean imagesticker;
    String festival,image_list,id;

    int[] textSizes, backgroundColors;
    String[] fonts;
    ImageView view;
    TextEditor textEditor;
    public ColorPicker colorPicker;
    private int mColorCode;
    TextView add_text_done_tv;
    AutofitTextView autofitTextView;
    ImageView deleteView;
    private ViewTouchListener viewTouchListener;
    ImageView imageview;
    RecyclerView recyclerView;
    InterstitialAd mInterstitialAd;
    com.droidninja.imageeditengine.views.AutofitTextView stickerImageView;

    @Override
    public void onStartViewChangeListener(View view) {
        if (currentMode != MODE_ADD_TEXT) {
            Log.i(ImageEditActivity.class.getSimpleName(), "onStartViewChangeListener" + "" + view.getId());
            imagebuttonsticker.setVisibility(View.GONE);
            imageButtonpaint.setVisibility(View.GONE);
            imageButtonSaveImage.setVisibility(View.GONE);
            imageButtonChangeColor.setVisibility(GONE);
            imagebuttonsticker.setVisibility(GONE);
            imageButtonFontChange.setVisibility(GONE);
            // back_iv.setVisibility(View.GONE);
            // back_color.setVisibility(View.GONE);
            AnimationHelper.animate(this, deleteButton, com.droidninja.imageeditengine.R.anim.fade_in_medium, View.VISIBLE, null);
        }
    }

    @Override
    public void onStopViewChangeListener(View view) {
        if (currentMode != MODE_ADD_TEXT) {
            Log.i(ImageEditActivity.class.getSimpleName(), "onStopViewChangeListener" + "" + view.getId());
            deleteButton.setVisibility(View.GONE);
            imagebuttonsticker.setVisibility(View.VISIBLE);
            imageButtonpaint.setVisibility(View.VISIBLE);
            imageButtonSaveImage.setVisibility(View.VISIBLE);
            imageButtonChangeColor.setVisibility(View.VISIBLE);
            imagebuttonsticker.setVisibility(VISIBLE);
            imageButtonFontChange.setVisibility(VISIBLE);
            AnimationHelper.animate(this, toolbarLayout, com.droidninja.imageeditengine.R.anim.fade_in_medium, View.VISIBLE, null);
        }
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {

    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

    }


    public interface TextEditor {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(com.droidninja.imageeditengine.R.layout.back_color);

        MobileAds.initialize(getApplicationContext(),"ca-app-pub-3875860493017976~4023292410");
        shared_pr = new SessionManager(getApplicationContext());
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("AC291303B2DCC0629689FF6F8ABD17E1").build();

        mInterstitialAd = new InterstitialAd(back_color.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3875860493017976/4217339585");

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {

                if(mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("Reason", String.valueOf(i));
            }
        });

        mInterstitialAd.loadAd(adRequest);

        Intent intent = getIntent();
        festival = intent.getStringExtra("festival");
        image_list = intent.getStringExtra("image_list");
        id = intent.getStringExtra("id");
        shred_pr = new SessionManager(this);

        mEditText = (EditText) findViewById(com.droidninja.imageeditengine.R.id.editText);
        toolbarLayout = findViewById(com.droidninja.imageeditengine.R.id.toolbar_layout);
        deleteButton = (ImageView) findViewById(com.droidninja.imageeditengine.R.id.delete_view);
        imageview = (ImageView) findViewById(R.id.imageview);

        mPictureView = (RelativeLayout) findViewById(com.droidninja.imageeditengine.R.id.pictureView);
        colorPicker = (ColorPicker) findViewById(com.droidninja.imageeditengine.R.id.color_picker);
        imageButtonpaint = (ImageView) findViewById(com.droidninja.imageeditengine.R.id.imageButtonpaint);
        imagebuttonsticker = (ImageView) findViewById(com.droidninja.imageeditengine.R.id.imagebuttonsticker);
        add_text_done_tv = (TextView)findViewById(com.droidninja.imageeditengine.R.id.add_text_done_tv);

        imageButtonChangeColor = (ImageView) findViewById(com.droidninja.imageeditengine.R.id.imageButtonChangeColor);
        imageButtonFontChange = (ImageView) findViewById(com.droidninja.imageeditengine.R.id.imageButtonFontChange);
        imageButtonTextSize = (ImageView) findViewById(com.droidninja.imageeditengine.R.id.imageButtonTextSize);
        imageButtonFontChanges = (ImageView) findViewById(com.droidninja.imageeditengine.R.id.imageButtonFontChanges);
        imageButtonChangeAlignment = (ImageView) findViewById(R.id.imageButtonChangeAlignment);
        imageButtonSaveImage = (Button) findViewById(com.droidninja.imageeditengine.R.id.imageButtonSaveImage);

        imgshoot = (ImageView)findViewById(com.droidninja.imageeditengine.R.id.imgshoot);
//        back_iv = (ImageView)findViewById(R.id.back_iv);
        recyclerView = (RecyclerView) findViewById(com.droidninja.imageeditengine.R.id.recyclerview);
        setImageView(imageview, deleteButton, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        requestQueue = Volley.newRequestQueue(getBaseContext());
        sticker = new ArrayList<>();

        backgroundColors = new int[]{com.droidninja.imageeditengine.R.drawable.back_drw_blue, com.droidninja.imageeditengine.R.drawable.back_drw_frz_green,
                com.droidninja.imageeditengine.R.drawable.back_drw_frz_orange,
                com.droidninja.imageeditengine.R.drawable.back_drw_black,
                com.droidninja.imageeditengine.R.drawable.back_drw_grad, com.droidninja.imageeditengine.R.drawable.back_drw_gray, com.droidninja.imageeditengine.R.drawable.back_drw_green, com.droidninja.imageeditengine.R.drawable.gradient};
        textSizes = getResources().getIntArray(com.droidninja.imageeditengine.R.array.fontSize);
        fonts = getResources().getStringArray(com.droidninja.imageeditengine.R.array.fonts);


        imageButtonChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(mPictureView);
            }
        });

        imageButtonFontChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFont(mPictureView);
            }
        });


        imageButtonFontChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddTextMode(true);
                imageButtonChangeColor.setVisibility(GONE);
                imagebuttonsticker.setVisibility(GONE);
                imageButtonFontChange.setVisibility(GONE);
                colorPicker.setVisibility(VISIBLE);
                imageButtonSaveImage.setVisibility(GONE);
                imageButtonpaint.setVisibility(GONE);
                mEditText.setVisibility(VISIBLE);
                mEditText.setCursorVisible(true);
                imageButtonFontChanges.setVisibility(VISIBLE);
                imageButtonChangeAlignment.setVisibility(VISIBLE);
                add_text_done_tv.setVisibility(VISIBLE);
            }
        });

        imageButtonTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextSize(mPictureView);
            }
        });

        add_text_done_tv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Log.d("selectedView", String.valueOf(selectedView));
                if (selectedView != null) {
                    ((AutofitTextView) selectedView).setText(mEditText.getText());
                    ((AutofitTextView) selectedView).setTextColor(mEditText.getTextColors());
                    ((AutofitTextView) selectedView).setTypeface(mEditText.getTypeface());
                    ((AutofitTextView) selectedView).setTextAlignment(mEditText.getTextAlignment());
//                    ((AutofitTextView) selectedView).setTextSize(mEditText.getTextSize());
                    Log.d("txtsize", String.valueOf(mEditText.getTextSize()));
                    Utility.hideSoftKeyboard(com.kumbh.design.Epost.Activity.back_color.this);
                } else {
                    createText(mEditText.getText().toString(),mEditText.getTypeface());
                    Utility.hideSoftKeyboard(com.kumbh.design.Epost.Activity.back_color.this);
                }

                Log.d("txtsize_t", String.valueOf(mEditText.getTextSize()));

                mEditText.setVisibility(INVISIBLE);
                imageButtonFontChanges.setVisibility(GONE);
                imageButtonChangeAlignment.setVisibility(GONE);
//                imageButtonAlignmentChanges.setVisibility(GONE);
                colorPicker.setVisibility(GONE);
                add_text_done_tv.setVisibility(GONE);
                imagebuttonsticker.setVisibility(View.VISIBLE);
                imageButtonFontChange.setVisibility(View.VISIBLE);
                imageButtonpaint.setVisibility(View.VISIBLE);
                imageButtonSaveImage.setVisibility(View.VISIBLE);
                imageButtonChangeColor.setVisibility(View.VISIBLE);
                currentMode =MODE_NONE;
            }
        });

        imageButtonChangeAlignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAlignment(v);
            }
        });

        imagebuttonsticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sticker_show == false) {
                    sticker.clear();
                    recyclerView.setVisibility(VISIBLE);
                    sticker_list();
                    sticker_show = true;
                }else{
                    recyclerView.setVisibility(GONE);
                    sticker_show = false;
                }
//                onStickerMode(true);
            }
        });

        imageButtonSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(mPictureView);
            }
        });

        imageButtonpaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPaintMode(true);
                imageButtonChangeColor.setVisibility(GONE);
                imagebuttonsticker.setVisibility(GONE);
                imageButtonFontChange.setVisibility(GONE);
                colorPicker.setVisibility(GONE);
                imageButtonSaveImage.setVisibility(GONE);
                imageButtonpaint.setVisibility(GONE);
                mEditText.setVisibility(GONE);
                imageButtonFontChanges.setVisibility(GONE);
                imageButtonChangeAlignment.setVisibility(GONE);
            }
        });

        colorPicker.setColorPickerListener(new ColorPicker.ColorPickerListener()
        {
            @Override
            public void onBeganColorPicking() {
            }

            @Override
            public void onColorValueChanged(int color) {
            }
            @Override
            public void onFinishedColorPicking(int color) {
                mColorCode = color;
                mEditText.setTextColor(color);
            }
            @Override
            public void onSettingsPressed() {
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void setImageView(ImageView imageView1, ImageView deleteButton, ViewTouchListener viewTouchListener) {
        this.imageview = imageView1;
        this.deleteView = deleteButton;
        this.viewTouchListener = viewTouchListener;
    }

    public void changeAlignment(View view) {
        int alignment = mEditText.getTextAlignment();
        if (alignment == View.TEXT_ALIGNMENT_CENTER) {
            mEditText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            mEditText.setGravity(Gravity.RIGHT);
            mEditText.setGravity(Gravity.CENTER_VERTICAL);
            imageButtonChangeAlignment.setImageResource(R.drawable.font_right);
        } else if(alignment == View.TEXT_ALIGNMENT_TEXT_END) {
            mEditText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            mEditText.setGravity(Gravity.LEFT);
            mEditText.setGravity(Gravity.CENTER_VERTICAL);
            imageButtonChangeAlignment.setImageResource(R.drawable.font_left);
        }else if(alignment == View.TEXT_ALIGNMENT_TEXT_START) {
            mEditText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            imageButtonChangeAlignment.setImageResource(R.drawable.font_center);
            mEditText.setGravity(Gravity.CENTER);
        }
    }

//    public class StickerListAdapter extends RecyclerView.Adapter<StickerListAdapter.ViewHolder>{
//
//        private List<String> stickers;
//        private Context context;
//
//        public StickerListAdapter(Context context, List<String> list) {
//            this.context = context;
//            stickers = list;
//        }
//
//        public void setData(List<String> stickersList) {
//            this.stickers = stickersList;
//            notifyDataSetChanged();
//        }
//
//        public void add(int position, String item) {
//            stickers.add(position, item);
//            notifyItemInserted(position);
//        }
//
//        public void remove(int position) {
//            stickers.remove(position);
//            notifyItemRemoved(position);
//        }
//
//
//        @NonNull
//        @Override
//        public StickerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_view, parent, false);
//            return new StickerListAdapter.ViewHolder(view);
//        }
//
//
//        @Override
//        public void onBindViewHolder(@NonNull StickerListAdapter.ViewHolder holder, final int position) {
//
//            final String path = sticker.get(position);
//            Log.d("Sticker_path",sticker.get(position));
//
////      Picasso.get().load(path).into((ImageView) holder.itemView);
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View view) {
//                    onItemClick(sticker.get(position));
//                }
//            });
//            Picasso.get().load(sticker.get(position)).into(((ImageView)holder.itemView));
//            // ((ImageView)holder.itemView).set(getImageFromAssetsFile(path));
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return sticker.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            ImageView sticker_image;
//            public ViewHolder(View v) {
//                super(v);
//                sticker_image = (ImageView)v.findViewById(R.id.sticker_img);
//            }
//        }
//    }

//    public void showStickers(String stickersFolder) {
//        mPictureView.bringToFront();
//        recyclerView.setVisibility(View.VISIBLE);
//        mEditText.setVisibility(View.GONE);
//        Utility.hideSoftKeyboard(back_color.this);
//        this.folderName = stickersFolder;
//        StickerListAdapter stickerListAdapter = (StickerListAdapter) recyclerView.getAdapter();
//        if(stickerListAdapter!=null){
//            stickerListAdapter.setData((getStickersList(stickersFolder)));
//        }
//    }

    public void hideStickers(){
        recyclerView.setVisibility(GONE);
    }

    private List<String> getStickersList(String folderName){
        AssetManager assetManager = getBaseContext().getAssets();
        try {
            String[] lists = assetManager.list(folderName);
            return  Arrays.asList(lists);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onItemClick(String path) {
        sticker_show = false;
        recyclerView.setVisibility(GONE);
        stickerImageView = (com.droidninja.imageeditengine.views.AutofitTextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.sticker_view_new, null);
        stickerImageView.setTextSize(56);
        stickerImageView.setMaxTextSize(1000);
        stickerImageView.setText(path);
        stickerImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        stickerImageView.setId(mPictureView.getChildCount());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView, mPictureView, this.imageview, true, this,layoutParams);
        multiTouchListener.setOnMultiTouchListener(new MultiTouchListener.OnMultiTouchListener() {
            @Override public void
            onRemoveViewListener(View removedView) {
                mPictureView.removeView(removedView);
                stickerImageView.setText(null);
                stickerImageView.setVisibility(INVISIBLE);
                selectedView = null;
            }
        });

        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override public void onClick(View currentView) {
                if(currentView!=null) {
                    selectedView = currentView;
                    selectViewIndex = currentView.getId();
                    if(currentMode == MODE_ADD_TEXT) {
                        add_text_done_tv.setVisibility(VISIBLE);
                        imageButtonFontChanges.setVisibility(VISIBLE);
                        imageButtonChangeAlignment.setVisibility(VISIBLE);
                        imageButtonChangeColor.setVisibility(GONE);
                        imagebuttonsticker.setVisibility(GONE);
                        imageButtonFontChange.setVisibility(GONE);
                        imageButtonSaveImage.setVisibility(GONE);
                        imageButtonpaint.setVisibility(GONE);
                    }
                }
//                Utility.showSoftKeyboard(back_color.this, mEditText);
            }

            @Override
            public void onLongClick() {
            }
        });

        stickerImageView.setOnTouchListener(multiTouchListener);

        //stickerImageView.setLayoutParams(layoutParams);
        mPictureView.addView(stickerImageView, layoutParams);

        selectViewIndex = mPictureView.getChildAt(mPictureView.getChildCount()-1).getId();
        selectedView = null;
    }

    private void createText(String text, Typeface typeface){
        currentMode = MODE_ADD_TEXT;
        autofitTextView = (AutofitTextView) LayoutInflater.from(getBaseContext()).inflate(com.droidninja.imageeditengine.R.layout.text_editor, null);
        autofitTextView.setId(mPictureView.getChildCount());
        autofitTextView.setText(text);
        autofitTextView.setTypeface(typeface);
        autofitTextView.setTextColor(mEditText.getCurrentTextColor());
        autofitTextView.setTypeface(mEditText.getTypeface());
        autofitTextView.setTextAlignment(mEditText.getTextAlignment());
        autofitTextView.setTextSize(30);
        autofitTextView.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView, mPictureView, this.imageview, true, this,layoutParams);
        multiTouchListener.setOnMultiTouchListener(new MultiTouchListener.OnMultiTouchListener() {
            @Override public void onRemoveViewListener(View removedView) {
                mPictureView.removeView(removedView);
                mEditText.setText(null);
                mEditText.setVisibility(INVISIBLE);
                selectedView = null;
            }
        });

        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override public void onClick(View currentView) {
                if(currentView!=null) {
                    selectedView = currentView;
                    selectViewIndex = currentView.getId();
                    mEditText.setVisibility(VISIBLE);
                    mEditText.setText(((AutofitTextView) currentView).getText());
                    ((AutofitTextView) currentView).setText("");
                    mEditText.setSelection(mEditText.getText().length());
                    colorPicker.setVisibility(VISIBLE);
                    currentMode = MODE_ADD_TEXT;
                    add_text_done_tv.setVisibility(VISIBLE);
                    imageButtonFontChanges.setVisibility(VISIBLE);
                    imageButtonChangeAlignment.setVisibility(VISIBLE);
                    imageButtonChangeColor.setVisibility(GONE);
                    imagebuttonsticker.setVisibility(GONE);
                    imageButtonFontChange.setVisibility(GONE);
                    imageButtonSaveImage.setVisibility(GONE);
                    imageButtonpaint.setVisibility(GONE);
                    Log.i("ViewNum", ":" + selectViewIndex + " " + ((AutofitTextView) currentView).getText());
                }
                Utility.showSoftKeyboard(com.kumbh.design.Epost.Activity.back_color.this, mEditText);
            }

            @Override public void onLongClick() {
            }
        });
        autofitTextView.setOnTouchListener(multiTouchListener);

        mPictureView.addView(autofitTextView, layoutParams);

        selectViewIndex = mPictureView.getChildAt(mPictureView.getChildCount()-1).getId();
        selectedView = null;
    }

    private void onPaintMode(boolean status) {
        if (status) {
            try {
                drawableOnTouchView = new DrawableOnTouchView(getBaseContext());
                drawableOnTouchView.setActionListener(new DrawableOnTouchView.OnActionListener() {

                    @Override
                    public void OnDone(Bitmap bitmap) {
                        drawableOnTouchView.makeNonClickable(false);
                        imageButtonpaint.setVisibility(VISIBLE);
                        imageButtonChangeColor.setVisibility(VISIBLE);
                        imagebuttonsticker.setVisibility(VISIBLE);
                        imageButtonFontChange.setVisibility(VISIBLE);
                        imageButtonSaveImage.setVisibility(VISIBLE);
                        imageButtonpaint.setVisibility(VISIBLE);
                        return;
                    }

                    @Override
                    public void show() {
                        drawableOnTouchView.makeNonClickable(true);
                    }

                    @Override
                    public void killSelf() {
                        //if(listener!=null)listener.killSelf(drawableOnTouchView.getBitmap());
                    }
                });

                drawableOnTouchView.setColorChangedListener(new DrawableOnTouchView.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                    }

                    @Override
                    public void onStrokeWidthChanged(float strokeWidth) {
                    }

                    @Override
                    public void onBrushChanged(int Brushid) {
                    }
                });

                FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.gravity= Gravity.CENTER;

                mPictureView.addView(drawableOnTouchView,params);

                drawableOnTouchView.attachCanvas(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);

            }catch (Exception e){
                Log.e("MainActivity",e.getMessage());
//                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        } else {
            //colorPicker.setVisibility(View.GONE);
            imageButtonpaint.setBackground(null);
            if(drawableOnTouchView != null) {
                drawableOnTouchView.hideOnDone();
            }
            //photoEditorView.hidePaintView();
            //photoEditorView.enableTouch(true);
//      paintEditView.setVisibility(View.GONE);
        }
    }

    public void changeColor(View view) {
        currentColor++;
        if (currentColor >= backgroundColors.length) currentColor = 0;
        int drawablename = backgroundColors[currentColor];

        ConstraintLayout rootView = (ConstraintLayout) findViewById(com.droidninja.imageeditengine.R.id.rootView);
        rootView.setBackgroundResource(drawablename);
        mPictureView.setBackgroundResource(drawablename);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setBackgroundDrawableResource(drawablename);
        }
    }

    public void changeFont(View view) {
        currentFont++;
        if (currentFont >= fonts.length) currentFont = 0;
        String path = fonts[currentFont];

        Typeface typeface = Typeface.createFromAsset(getAssets(), path);
        mEditText.setTypeface(typeface);
        Log.d("FontInfo", path);
    }

    public void changeTextSize(View view) {
        currentTextSize++;
        if (currentTextSize >= textSizes.length) currentTextSize = 0;

        int size = textSizes[currentTextSize];
        mEditText.setTextSize(size);
    }

    public void saveImage(View view) {
        String text = null;
        Log.d("Name",shared_pr.getName());
            text = "\n\n" + shared_pr.getName() + " Wishing you a Happy" +" \n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://is.gd/FUEfj6";

        Bitmap bitmap = Bitmap.createBitmap(mPictureView.getWidth(), mPictureView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mEditText.setCursorVisible(false);
        mPictureView.draw(canvas);

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), com.droidninja.imageeditengine.R.drawable.watermark_img);
        bitmap1 = mark(bitmap1, "Hello");

        imgshoot.setImageBitmap(bitmap1);

        String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.png'").format(new Date());
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
//
//        try {
//            bitmap.compress(Bitmap.CompressFormat.PNG, 95, new FileOutputStream(file));
//            Toast.makeText(back_color.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image Description", null);
        Uri uri = Uri.parse(path);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, "Share"));



//        String savedImagePath = SaveToStorageUtil.save(bitmap, back_color.this,fileName);
//        ImageScannerAdapter adapter = new ImageScannerAdapter(back_color.this);
//        adapter.scanImage(savedImagePath);
//        ImageProcessor.getInstance().resetModificationFlag();
//        File file = new File(savedImagePath);
//
//        Uri contentUri = FileProvider.getUriForFile(getBaseContext(),getBaseContext().getApplicationContext().getPackageName() + ".provider", file);
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setDataAndType(contentUri,"image/png");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
//        intent.putExtra(Intent.EXTRA_TEXT, text);
//        startActivity(Intent.createChooser(intent, "Share"));
    }

    public Bitmap mark(Bitmap src,String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        return result;
    }

    private void onAddTextMode(boolean status) {
        if (status) {
            addText();
        } else {
            hideTextMode();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addText() {
        mEditText.setVisibility(VISIBLE);
        colorPicker.setVisibility(VISIBLE);
        add_text_done_tv.setVisibility(VISIBLE);
        imageButtonFontChange.setVisibility(VISIBLE);
        mEditText.setText(null);
        mEditText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Utility.showSoftKeyboard(back_color.this, mEditText);
        createText(mEditText.getText().toString(),mEditText.getTypeface());
    }

    public void hideTextMode(){
        Utility.hideSoftKeyboard(back_color.this);
        mEditText.setVisibility(View.INVISIBLE);
    }


    public void sticker_list(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(back_color.this, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        EmojiAdapter emojiAdapter = new EmojiAdapter();
        recyclerView.setAdapter(emojiAdapter);
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you want to exit without saving image ?");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage(mPictureView);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(back_color.this,com.kumbh.design.Epost.Activity.Set_Images.class);
                i.putExtra("festival",festival);
                i.putExtra("image_list",image_list);
                i.putExtra("id",id);
                startActivity(i);
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if (sticker_show == true) {
            recyclerView.setVisibility(GONE);
            sticker_show = false;
        }else {
            showSaveDialog();
        }
    }

    public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {

        ArrayList<String> emojisList = getEmojis(back_color.this);

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(com.droidninja.imageeditengine.R.layout.row_emoji, parent, false);
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
                txtEmoji = itemView.findViewById(com.droidninja.imageeditengine.R.id.txtEmoji);

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
        String[] emojiList = context.getResources().getStringArray(com.droidninja.imageeditengine.R.array.photo_editor_emoji);
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
