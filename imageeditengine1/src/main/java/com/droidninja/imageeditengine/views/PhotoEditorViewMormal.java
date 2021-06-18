package com.droidninja.imageeditengine.views;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import java.util.ArrayList;

import static com.droidninja.imageeditengine.PhotoEditorFragment.MODE_ADD_TEXT;
import static com.droidninja.imageeditengine.PhotoEditorFragment.MODE_NONE;
import static com.droidninja.imageeditengine.PhotoEditorFragment.addTextButton;
import static com.droidninja.imageeditengine.PhotoEditorFragment.back_color;
import static com.droidninja.imageeditengine.PhotoEditorFragment.back_iv;
import static com.droidninja.imageeditengine.PhotoEditorFragment.currentMode;
import static com.droidninja.imageeditengine.PhotoEditorFragment.paintButton;
import static com.droidninja.imageeditengine.PhotoEditorFragment.share;
import static com.droidninja.imageeditengine.PhotoEditorFragment.stickerButton;

public class PhotoEditorViewMormal extends FrameLayout implements ViewTouchListener, KeyboardHeightProvider.KeyboardHeightObserver {
    RelativeLayout container;
    public static RecyclerView recyclerView;
    private int currentFont = 0;
    CustomPaintView customPaintView;
    private String folderName;
    public static ImageView imageView;
    private ImageView deleteView;
    private ViewTouchListener viewTouchListener;
    private View selectedView;
    private int selectViewIndex;
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
    boolean sticker_show = false;
    public static ImageView mainImageView;
    public static LottieAnimationView animationView;
    public static FrameLayout layout;
    String[] fonts;
    ImageView imageButtonFontChanges, imageButtonAlignmentChanges;
    ArrayList<String> sticker;

    private static final int ZOOM = 2;

    public PhotoEditorViewMormal(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PhotoEditorViewMormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PhotoEditorViewMormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(final Context context, AttributeSet attrs, int defStyle) {
        View view = inflate(getContext(), R.layout.frahment_noramal_pghoto, null);
        mainImageView = view.findViewById(R.id.image_iv);
        imageButtonFontChanges = view.findViewById(R.id.imageButtonFontChanges);
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
        customPaintView = view.findViewById(R.id.paint_view);

        requestQueue = Volley.newRequestQueue(getContext());
        sticker = new ArrayList<>();

        keyboardHeightProvider = new KeyboardHeightProvider((Activity) getContext());
        keyboardHeightProvider.setKeyboardHeightObserver(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

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
            public void onClick(View view) {
                inputTextET.post(new Runnable() {
                    @Override
                    public void run() {
                        currentFont++;
                        if (currentFont >= fonts.length) currentFont = 0;
                        String path = fonts[currentFont];
                        Typeface typeface = Typeface.createFromAsset(context.getAssets(), path);
                        inputTextET.setTypeface(typeface);
                        Log.d("FontInfo", path);
                        imageButtonAlignmentChanges.setVisibility(VISIBLE);
                    }
                });
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
        int alignment = inputTextET.getTextAlignment();
        if (alignment == View.TEXT_ALIGNMENT_CENTER) {
            inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            inputTextET.setGravity(Gravity.RIGHT);
            inputTextET.setGravity(Gravity.CENTER_VERTICAL);
            imageButtonAlignmentChanges.setImageResource(R.drawable.font_right);
        } else if (alignment == View.TEXT_ALIGNMENT_TEXT_END) {
            inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            inputTextET.setGravity(Gravity.LEFT);
            inputTextET.setGravity(Gravity.CENTER_VERTICAL);
            imageButtonAlignmentChanges.setImageResource(R.drawable.font_left);
        } else if (alignment == View.TEXT_ALIGNMENT_TEXT_START) {
            inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            imageButtonAlignmentChanges.setImageResource(R.drawable.font_center);
            inputTextET.setGravity(Gravity.CENTER);
        }
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
        imageButtonAlignmentChanges.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);
//    containerView.bringToFront();
        inputTextET.setText(null);
        inputTextET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Utility.showSoftKeyboard((Activity) getContext(), inputTextET);
        createText(inputTextET.getText().toString(), inputTextET.getTypeface());
    }

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