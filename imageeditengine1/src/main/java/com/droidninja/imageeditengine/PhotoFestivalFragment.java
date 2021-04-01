package com.droidninja.imageeditengine;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.droidninja.imageeditengine.brush.ColorPicker;
import com.droidninja.imageeditengine.brush.DrawableOnTouchView;
import com.droidninja.imageeditengine.model.UserData;
import com.droidninja.imageeditengine.utils.MultiTouchListener;
import com.droidninja.imageeditengine.utils.SessionDataManager;
import com.droidninja.imageeditengine.views.PhotoEditorView;
import com.droidninja.imageeditengine.views.ViewTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.view.View.VISIBLE;
import static com.droidninja.imageeditengine.ImageEditor.Builder.FestivalName;
import static com.droidninja.imageeditengine.ImageEditor.Builder.UserName;
import static com.droidninja.imageeditengine.views.PhotoEditorView.animationView;
import static com.droidninja.imageeditengine.views.PhotoEditorView.container;
import static com.droidninja.imageeditengine.views.PhotoEditorView.imageView;
import static com.droidninja.imageeditengine.views.PhotoEditorView.layout;
import static com.droidninja.imageeditengine.views.PhotoEditorView.mainImageView;
import static com.droidninja.imageeditengine.views.PhotoEditorView.recyclerView;

public class PhotoFestivalFragment extends BaseFragment implements View.OnClickListener, ViewTouchListener {

    public static ImageView stickerButton;
    public static ImageView addTextButton;
    public static Button share;
    public static ImageView imgshoot;
    PhotoEditorView photoEditorView;
    public static ImageView paintButton;
    ImageView deleteButton;
    private Typeface typeface;
    public static ImageView back_color;
    View toolbarLayout;
    Button doneBtn;
    String[] fonts;
    public ColorPicker color_picker;
    private TextView txtText;
    private TextView tvemail;
    private int selectTextId;
    public static ImageView back_iv;
    private Bitmap mainBitmap;
    RelativeLayout re_layout;
    public LayoutInflater mLayoutInflater;
    public static final int MODE_NONE = 0;
    public static int MODE_PAINT = 1;
    public static final int MODE_ADD_TEXT = 2;
    public static final int MODE_STICKER = 3;
    public static final int MODE_SHARE = 4;
    public static final int MODE_BACK_COLOR = 5;

    String TAG = "PhotoEditor_Fragment";
    public static int currentMode;
    private Bitmap originalBitmap;
    private int currentFont = 0;
    int[] textSizes;
    private View selectedView;
    private int selectViewIndex;
    ImageView imageButtonFontChanges, imageButtonAlignmentChanges, image_font, color_icon;
    float scalediff;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = ZOOM;
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    boolean isRotateEnabled = true;
    byte[] byteArray;
    Bitmap bmp;
    SessionDataManager shared_pr;
    //SessionManager shred_pr;

    // private ImageView imageView;
    private ViewTouchListener viewTouchListener;

    private static final int ANIMATION_DURATION = 180;

    AnimatorSet animatorSet;
    Matrix m = new Matrix();
    Float scale = 1f;
    private DrawableOnTouchView drawableOnTouchView;
    private UserData user;

    public static PhotoFestivalFragment newInstance(String imagePath) {


        Bundle bundle = new Bundle();

        PhotoFestivalFragment photoEditorFragment = new PhotoFestivalFragment();
        bundle.putString(ImageEditor.EXTRA_IMAGE_PATH, imagePath);
        photoEditorFragment.setArguments(bundle);
        return photoEditorFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public PhotoFestivalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_festival, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @SuppressLint("ClickableViewAccessibility")
    protected void initView(View view) {

        addTextButton = view.findViewById(R.id.add_text_btn);
        share = view.findViewById(R.id.share);
        deleteButton = view.findViewById(R.id.delete_view);
//        RecyclerView rvColor = view.findViewById(R.id.rvColors);
        color_picker = view.findViewById(R.id.color_picker1);
        photoEditorView = view.findViewById(R.id.photo_editor_view);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        //height 1378
        //width 720

        int dpValur = (int) Math.round(1200 / getActivity().getResources().getDisplayMetrics().density);
        int width = dpValur;
        int height = dpValur;
        Toast.makeText(getActivity(), "dp" + width, Toast.LENGTH_LONG).show();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        photoEditorView.setLayoutParams(layoutParams);
        mLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        photoEditorView.setMinimumWidth(displayMetrics.widthPixels);
        paintButton = view.findViewById(R.id.paint_btn);
        toolbarLayout = view.findViewById(R.id.toolbar_layout);
        back_iv = view.findViewById(R.id.back_iv);
        re_layout = view.findViewById(R.id.re_layout);
        back_color = view.findViewById(R.id.back_color);
        imgshoot = view.findViewById(R.id.imgshoot);
        textSizes = getResources().getIntArray(R.array.fontSize);
        shared_pr = new SessionDataManager(getActivity());
        imageButtonFontChanges = view.findViewById(R.id.imageButtonFontChanges1);
        image_font = view.findViewById(R.id.font_bold1);
        imageButtonAlignmentChanges = view.findViewById(R.id.imageButtonAlignmentChanges1);
        color_icon = view.findViewById(R.id.color_icon);
        View rootViewText = mLayoutInflater.inflate(R.layout.view_text_layout, null);
        View rootView1Text = mLayoutInflater.inflate(R.layout.view_email_layout, null);
        txtText = rootViewText.findViewById(R.id.tvPhotoEditorText);
        tvemail = rootView1Text.findViewById(R.id.tvEmailEditorText);
        getData();
        photoEditorView.setImageView(mainImageView, null, this);
        if (getArguments() != null && getActivity() != null && getActivity().getIntent() != null) {
            final String imagePath = getArguments().getString(ImageEditor.EXTRA_IMAGE_PATH);
            animationView.setVisibility(VISIBLE);
            Glide.with(this)
                    .asBitmap()
                    .load(imagePath)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            animationView.setVisibility(View.GONE);
                            if (resource != null) {
                                Palette p = Palette.from(resource).generate();
                                // Use generated instance
                                layout.setBackgroundColor(p.getDominantColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)));
                            }
                            return false;
                        }
                    })
                    .into(mainImageView);
            fonts = getResources().getStringArray(R.array.fonts);
            Intent intent = getActivity().getIntent();
//            setVisibility(addTextButton, intent.getBooleanExtra(ImageEditor.EXTRA_IS_TEXT_MODE, false));
//            setVisibility(paintButton, intent.getBooleanExtra(ImageEditor.EXTRA_IS_PAINT_MODE, false));


            mainImageView.setMaxHeight(height - 100);
            addTextButton.setOnClickListener(this);
            paintButton.setOnClickListener(this);
            back_iv.setOnClickListener(this);
            back_color.setOnClickListener(this);
            share.setOnClickListener(this);
//            back_iv.setOnClickListener(this);
//            back_color.setOnClickListener(this);
            mainImageView.setOnTouchListener(new View.OnTouchListener() {
                Matrix matrix = new Matrix();
                Matrix savedMatrix = new Matrix();

                float angle = 0;

                static final int NONE = 0;
                static final int DRAG = 1;
                static final int ZOOM = 2;
                int mode = NONE;

                PointF start = new PointF();
                PointF mid = new PointF();
                float oldDist = 1f;
                String savedItemClicked;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ImageView view = (ImageView) v;
                    dumpEvent(event);

                    // Handle touch events here...
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            savedMatrix.set(matrix);
                            start.set(event.getX(), event.getY());
                            Log.d(TAG, "mode=DRAG");
                            mode = DRAG;
                            break;

                        case MotionEvent.ACTION_POINTER_DOWN:
                            oldDist = spacing(event);
                            Log.d(TAG, "oldDist=" + oldDist);
                            if (oldDist > 10f) {
                                savedMatrix.set(matrix);
                                midPoint(mid, event);
                                mode = ZOOM;
                                Log.d(TAG, "mode=ZOOM");
                            }
                            d = rotation(event);
                            break;

                        case MotionEvent.ACTION_UP:

                        case MotionEvent.ACTION_POINTER_UP:
                            mode = NONE;
                            Log.d(TAG, "mode=NONE");
                            break;

                        case MotionEvent.ACTION_MOVE:

                            if (mode == DRAG) {
                                matrix.set(savedMatrix);
                                matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                            } else if (mode == ZOOM) {
                                float newDist = spacing(event);
                                Log.d(TAG, "newDist=" + newDist);
                                if (newDist > 10f) {
                                    matrix.set(savedMatrix);
                                    float scale = newDist / oldDist;
                                    matrix.postScale(scale, scale, mid.x, mid.y);
                                }
                            }
                            break;
                    }
                    view.setImageMatrix(matrix);
                    return true;
                }
            });


        }
        color_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_picker.setVisibility(VISIBLE);
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

                if (selectTextId == 1) {
                    txtText.setTextColor(color);
                } else if (selectTextId == 2) {
                    tvemail.setTextColor(color);
                }

            }

            @Override
            public void onSettingsPressed() {
            }
        });
        imageButtonFontChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                currentFont++;
                if (currentFont >= fonts.length) currentFont = 0;
                String path = fonts[currentFont];
                typeface = Typeface.createFromAsset(getActivity().getAssets(), path);


                if (selectTextId == 1) {
                    txtText.setTypeface(typeface);
                } else if (selectTextId == 2) {
                    tvemail.setTypeface(typeface);
                }
                Log.d("FontInfo", path);
                imageButtonAlignmentChanges.setVisibility(VISIBLE);
            }


        });
        image_font.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (selectTextId == 1) {
                    txtText.setTypeface(typeface, Typeface.BOLD);
                } else if (selectTextId == 2) {
                    tvemail.setTypeface(typeface, Typeface.BOLD);
                }
            }
        });


    }

    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }


    @Override
    public void onClick(final View view) {
        int id = view.getId();
        if (id == R.id.back_iv) {
            showSaveDialog();
        } else if (id == R.id.share) {
            setMode(MODE_SHARE);
        } else if (id == R.id.back_color) {
            setMode(MODE_BACK_COLOR);
        } else if (id == R.id.add_text_btn) {
            back_iv.setVisibility(View.GONE);
            paintButton.setVisibility(View.GONE);
            addTextButton.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            back_color.setVisibility(View.GONE);
            setMode(MODE_ADD_TEXT);
        } else if (id == R.id.paint_btn) {
            setMode(MODE_PAINT);
        }
        if (currentMode != MODE_NONE) {
            photoEditorView.animate().scaleX(1f);
            photoEditorView.animate().scaleY(1f);
        } else {
        }
    }


    protected void setMode(int mode) {
        if (currentMode != mode) {
            onModeChanged(mode);
        } else {
            mode = MODE_NONE;
            onModeChanged(mode);
        }
        this.currentMode = mode;


    }

    private void onAddTextMode(boolean status) {
        if (status) {
            photoEditorView.festivalAddText();
        } else {
            photoEditorView.hideTextMode();
        }
    }

    protected void onModeChanged(int currentMode) {

        Log.i(ImageEditActivity.class.getSimpleName(), "CM: " + currentMode);
        onAddTextMode(currentMode == MODE_ADD_TEXT);
        onPaintMode(currentMode == MODE_PAINT);
        onshareMode(currentMode == MODE_SHARE);
        onback_color(currentMode == MODE_BACK_COLOR);

        if (currentMode == MODE_ADD_TEXT) {
        } else {
        }
    }

    private void onback_color(boolean status) {
        if (status) {

        }
    }

    private void onPaintMode(boolean status) {
        if (status) {
            recyclerView.setVisibility(View.GONE);
            paintButton.setVisibility(View.GONE);
            stickerButton.setVisibility(View.GONE);
            addTextButton.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            back_iv.setVisibility(View.GONE);
            back_color.setVisibility(View.GONE);
            try {
                paintButton.setBackground(getResources().getDrawable(R.drawable.circle_color));
                drawableOnTouchView = new DrawableOnTouchView(getActivity());
                drawableOnTouchView.setActionListener(new DrawableOnTouchView.OnActionListener() {

                    @Override
                    public void OnDone(Bitmap bitmap) {
                        drawableOnTouchView.makeNonClickable(false);
                        paintButton.setVisibility(VISIBLE);
                        stickerButton.setVisibility(VISIBLE);
                        addTextButton.setVisibility(VISIBLE);
                        share.setVisibility(VISIBLE);
                        back_iv.setVisibility(VISIBLE);
                        back_color.setVisibility(View.GONE);
                        paintButton.setBackground(null);
                        paintButton.setBackground(getResources().getDrawable(R.drawable.share_back));
                        return;
                    }

                    @Override
                    public void show() {
                        drawableOnTouchView.makeNonClickable(true);
                    }

                    @Override
                    public void killSelf() {
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

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;

                photoEditorView.addView(drawableOnTouchView, params);

                drawableOnTouchView.attachCanvas(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

                stickerButton.setClickable(true);
                addTextButton.setClickable(true);
                back_iv.setClickable(true);
                paintButton.setClickable(true);
                back_color.setClickable(true);

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage());
            }
        } else {
            paintButton.setBackground(null);
            paintButton.setBackground(getResources().getDrawable(R.drawable.share_back));

            if (drawableOnTouchView != null) {
                drawableOnTouchView.hideOnDone();
            }
        }
    }

    private void onshareMode(boolean status) {
        if (status) {
            String text = null;
            Log.d("fest_name_img", FestivalName);
            if (FestivalName.equalsIgnoreCase("congratulation")) {
                text = "\n\n" + UserName + " Wishing you a" + " " + FestivalName.replace("+", " ") + " \n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://is.gd/FUEfj6";
            } else if (FestivalName.equalsIgnoreCase("loveu")) {
                text = "\n\n" + UserName + " say " + "LOVE U" + " \n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://is.gd/FUEfj6";
            } else {
                text = "\n\n" + UserName + " Wishing you a Happy" + " " + FestivalName.replace("+", " ") + " \n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://is.gd/FUEfj6";
            }
            //text = "\n\n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://goo.gl/SU7Xbp";

            recyclerView.setVisibility(View.GONE);
            Bitmap bitmap = Bitmap.createBitmap(photoEditorView.getWidth(), photoEditorView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            photoEditorView.draw(canvas);
//
//        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.watermark_img);
//            bitmap1 = mark(bitmap1, "Hello");

//            imgshoot.setImageBitmap(bitmap1);

//            String bitmapPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "Depost", null);
//            Uri bitmapUri = Uri.parse(bitmapPath);


            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "IMG_" + Calendar.getInstance().getTime(), null);

            Uri uri = Uri.parse(path);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(intent, "Share Image"));


//            String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.png'").format(new Date());
//            String savedImagePath = SaveToStorageUtil.save(bitmap, getContext(),fileName);
//            ImageScannerAdapter adapter = new ImageScannerAdapter(getContext());
//            adapter.scanImage(savedImagePath);
//            ImageProcessor.getInstance().resetModificationFlag();
//            File file = new File(savedImagePath);
//
//            Uri contentUri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
//
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setDataAndType(contentUri,"image/png");
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(contentUri, getContext().getContentResolver().getType(contentUri));
//            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
//            intent.putExtra(Intent.EXTRA_TEXT, text);
//            startActivity(Intent.createChooser(intent, "Share"));

        }
    }

    public Bitmap mark(Bitmap src, String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onStartViewChangeListener(final View view) {

    }

    @Override
    public void onStopViewChangeListener(View view) {
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {
    }


    @SuppressLint("ClickableViewAccessibility")
    public static void visibleView() {
        paintButton.setVisibility(View.GONE);
        stickerButton.setVisibility(View.GONE);
        addTextButton.setVisibility(View.GONE);
        share.setVisibility(View.GONE);
        back_iv.setVisibility(View.GONE);
        back_color.setVisibility(View.GONE);
        paintButton.setBackground(null);
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you want to exit without saving image ?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onshareMode(true);
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
                getActivity().finish();
            }
        });
        builder.create().show();
    }

    private void getData() {
        String userId = shared_pr.getUserId();
        //  Log.d("DATA_URL","https://api.qwant.com/api/search/images?count=50&q="+festival+"+backgrounds&t=images&safesearch=1&locale=en_US&uiv=4");
//        pb_setimage.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, "https://www.kumbhdesign.in/mobile-app/depost/api/profile-update/" + userId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
//                    pb_setimage.setVisibility(View.GONE);

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("user_data");
                    int error = jsonObject2.getInt("error");
                    if (error == 0) {

                        JSONObject userData = jsonObject2.getJSONObject("user");
                        user = new UserData(userData.getString("facebook_url"), userData.getString("company_email"), userData.getString("company_logo_path"), userData.getString("website_url"), userData.getString("instagram_url"), userData.getString("linkedin_url"), userData.getString("mobile_number"));
                        Toast.makeText(getActivity(), user.getCompanyEmail(), Toast.LENGTH_LONG);
                        Log.v("userData", user.getCompanyEmail());

                    }

                    photoEditorView.addImage(null, user.getCompany_logo_path());
                    setText(user.getCompanyEmail(), 20, 50);
                    setEmailText(user.getFacebookUrl(), 20, 80);
//                    photoEditorView.addImage(null,user.getCompany_logo_path());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pb_setimage.setVisibility(View.GONE);
                        Log.d("Error", error.toString());
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public void setText(String text, int left, int top) {
        View rootViewText = mLayoutInflater.inflate(R.layout.view_text_layout, null);


        txtText = rootViewText.findViewById(R.id.tvPhotoEditorText);

//        final FrameLayout frmBorder = rootViewText.findViewById(R.id.frmBorder);
        txtText.setText(text);
        txtText.setPadding(left, top, 0, 0);
        txtText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        MultiTouchListener multiTouchListener = new MultiTouchListener(null, container, imageView, true, this, layoutParams);
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {
                selectTextId = 1;
                color_icon.setVisibility(VISIBLE);
                imageButtonFontChanges.setVisibility(VISIBLE);
                image_font.setVisibility(VISIBLE);
                imageButtonAlignmentChanges.setVisibility(VISIBLE);
                selectedView = currentView;
                selectViewIndex = currentView.getId();
            }

            @Override
            public void onLongClick() {

            }


        });
        txtText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                selectTextId = 1;
                color_icon.setVisibility(VISIBLE);

                imageButtonFontChanges.setVisibility(VISIBLE);
                image_font.setVisibility(VISIBLE);
                imageButtonAlignmentChanges.setVisibility(VISIBLE);

                selectedView = view;
                selectViewIndex = view.getId();
                return false;
            }
        });

        rootViewText.setOnTouchListener(multiTouchListener);
        container.addView(rootViewText, layoutParams);
    }

    public void setEmailText(String text, int left, int top) {
        View rootViewText = mLayoutInflater.inflate(R.layout.view_email_layout, null);
        tvemail = rootViewText.findViewById(R.id.tvEmailEditorText);
//        final FrameLayout frmBorder = rootViewText.findViewById(R.id.frmBorder);
        tvemail.setText(text);
        tvemail.setPadding(left, 300, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
        MultiTouchListener multiTouchListener = new MultiTouchListener(null, container, imageView, true, this, layoutParams);
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {
                selectTextId = 2;
                color_icon.setVisibility(VISIBLE);

                imageButtonFontChanges.setVisibility(VISIBLE);
                image_font.setVisibility(VISIBLE);
                imageButtonAlignmentChanges.setVisibility(VISIBLE);

                selectedView = currentView;
                selectViewIndex = currentView.getId();
            }

            @Override
            public void onLongClick() {

            }


        });

        tvemail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                selectTextId = 2;
                color_icon.setVisibility(VISIBLE);

                imageButtonFontChanges.setVisibility(VISIBLE);
                image_font.setVisibility(VISIBLE);
                imageButtonAlignmentChanges.setVisibility(VISIBLE);

                selectedView = view;
                selectViewIndex = view.getId();
                return false;
            }
        });

        rootViewText.setOnTouchListener(multiTouchListener);
        container.addView(rootViewText);
    }


}
