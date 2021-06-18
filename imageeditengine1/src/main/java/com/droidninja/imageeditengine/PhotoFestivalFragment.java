package com.droidninja.imageeditengine;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
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
import com.droidninja.imageeditengine.model.ListTemplate;
import com.droidninja.imageeditengine.model.ResponseTemplates;
import com.droidninja.imageeditengine.model.UserData;
import com.droidninja.imageeditengine.utils.MultiTouchListener;
import com.droidninja.imageeditengine.utils.SessionDataManager;
import com.droidninja.imageeditengine.views.PhotoEditorView;
import com.droidninja.imageeditengine.views.ViewTouchListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;

import static android.view.View.VISIBLE;
import static com.droidninja.imageeditengine.ImageEditor.Builder.FestivalName;
import static com.droidninja.imageeditengine.ImageEditor.Builder.UserName;
import static com.droidninja.imageeditengine.views.PhotoEditorView.animationView;
import static com.droidninja.imageeditengine.views.PhotoEditorView.container;
import static com.droidninja.imageeditengine.views.PhotoEditorView.imageView;
import static com.droidninja.imageeditengine.views.PhotoEditorView.layout;
import static com.droidninja.imageeditengine.views.PhotoEditorView.mainImageView;
import static com.droidninja.imageeditengine.views.PhotoEditorView.progressBar;
import static com.droidninja.imageeditengine.views.PhotoEditorView.recyclerView;

public class PhotoFestivalFragment extends BaseFragment implements View.OnClickListener, ViewTouchListener, AdapterView.OnItemSelectedListener {

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
    private TextView txtEmail;
    private TextView tvAddress, tvMobile, tvTagline, tvWebsite;
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
    RecyclerView rvColor;
    ListTemplate template;
    ImageView logoImage;
    String TAG = "PhotoEditor_Fragment";
    public static int currentMode;
    private Bitmap originalBitmap;
    private int currentFont = 0;
    int[] textSizes;
    View selectedView;
    int selectViewIndex;
    ImageView imageButtonFontChanges, imageButtonAlignmentChanges, image_font, color_icon, imageItalic, fbImage, twitter, linkedin,instagram;
    float scalediff;
    SeekBar seekBar1;
    RelativeLayout relativeLayoutSocial;
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
    CardView cardView;
    ImageView imgcloseEmail, imgcloseMob, imgclodetag, imgcloseWebsute, imgcloseAddress, imgcloselogo, imgsocilaLogo;


    //SessionManager shred_pr;
    Spinner spinner;
    // private ImageView imageView;
    private ViewTouchListener viewTouchListener;

    private static final int ANIMATION_DURATION = 180;

    AnimatorSet animatorSet;
    Matrix m = new Matrix();
    Float scale = 1f;
    private DrawableOnTouchView drawableOnTouchView;
    private UserData user;
    String postId;
    String templateId;
    int densityData;

    public static PhotoFestivalFragment newInstance(String imagePath, String postId, String templateId) {


        Bundle bundle = new Bundle();

        PhotoFestivalFragment photoEditorFragment = new PhotoFestivalFragment();
        bundle.putString(ImageEditor.EXTRA_IMAGE_PATH, imagePath);
        bundle.putString(ImageEditor.EXTRA_HAS_POSTID, postId);
        bundle.putString(ImageEditor.EXTRA_HAS_TEMPLATEID, templateId);
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
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                color_picker.setVisibility(View.GONE);
//                cardView.setVisibility(View.INVISIBLE);
                txtEmail.setBackgroundResource(0);
                tvAddress.setBackgroundResource(0);
                tvMobile.setBackgroundResource(0);
                tvWebsite.setBackgroundResource(0);
                tvTagline.setBackgroundResource(0);
                relativeLayoutSocial.setBackgroundResource(0);
                logoImage.setBackgroundResource(0);
                imgclodetag.setVisibility(View.GONE);

                imgcloseEmail.setVisibility(View.GONE);

                imgcloseAddress.setVisibility(View.GONE);

                imgcloseMob.setVisibility(View.GONE);

                imgcloseWebsute.setVisibility(View.GONE);

                imgsocilaLogo.setVisibility(View.GONE);

                imgcloselogo.setVisibility(View.GONE);

                rvColor.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                seekBar1.setVisibility(View.GONE);
                return false;

            }
        });
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
        seekBar1 = view.findViewById(R.id.seekBar1);
        share = view.findViewById(R.id.share);
        cardView = view.findViewById(R.id.card);
        deleteButton = view.findViewById(R.id.delete_view);
//        RecyclerView rvColor = view.findViewById(R.id.rvColors);
        color_picker = view.findViewById(R.id.color_picker1);
        photoEditorView = view.findViewById(R.id.photo_editor_view);
        rvColor = view.findViewById(R.id.rvColor);
        postId = getArguments().getString(ImageEditor.EXTRA_HAS_POSTID);
        templateId = getArguments().getString(ImageEditor.EXTRA_HAS_TEMPLATEID);
        //height 1378
        //width 720
        densityData = (int) Math.round(getActivity().getResources().getDisplayMetrics().density);
        Log.v("densityData",densityData+"");
//        Toast.makeText(getActivity(),densityData,Toast.LENGTH_LONG).show();
        int dpValur = (int) Math.round(1200 / densityData);
        int imageViewSize = (int) ((float)  getActivity().getResources().getDisplayMetrics().widthPixels * 1.8);

        int width = getActivity().getResources().getDisplayMetrics().widthPixels-50;
        int   height = getActivity().getResources().getDisplayMetrics().heightPixels/2 ;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);


        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        photoEditorView.setLayoutParams(layoutParams);
        mLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        photoEditorView.setMinimumWidth(displayMetrics.widthPixels);
        paintButton = view.findViewById(R.id.paint_btn);
        toolbarLayout = view.findViewById(R.id.toolbar_layout);
        spinner = (Spinner) view.findViewById(R.id.spinner);

        back_iv = view.findViewById(R.id.back_iv);
        re_layout = view.findViewById(R.id.re_layout);
        back_color = view.findViewById(R.id.back_color);
        imgshoot = view.findViewById(R.id.imgshoot);
        textSizes = getResources().getIntArray(R.array.fontSize);
        shared_pr = new SessionDataManager(getActivity());
        imageButtonFontChanges = view.findViewById(R.id.imageButtonFontChanges1);
        image_font = view.findViewById(R.id.font_bold1);
        imageButtonAlignmentChanges = view.findViewById(R.id.imageButtonAlignmentChanges1);
        imageItalic = view.findViewById(R.id.imageItalic);
        color_icon = view.findViewById(R.id.color_icon);
        View rootViewText = mLayoutInflater.inflate(R.layout.view_text_layout, null);
        View rootView1Text = mLayoutInflater.inflate(R.layout.view_email_layout, null);
        View rootViewMobileText = mLayoutInflater.inflate(R.layout.view_mobile_layout, null);
        View rootViewTagText = mLayoutInflater.inflate(R.layout.view_tag_layout, null);
        View rootViewWebsite = mLayoutInflater.inflate(R.layout.view_website_layout, null);
        txtEmail = rootViewText.findViewById(R.id.tvPhotoEditorText);
        imgcloseEmail = rootViewText.findViewById(R.id.imgPhotoEditorClose);
        imgcloseAddress = rootView1Text.findViewById(R.id.imgPhotoEditorClose);
        imgcloseMob = rootViewMobileText.findViewById(R.id.imgPhotoEditorClose);
        imgcloseWebsute = rootViewWebsite.findViewById(R.id.imgPhotoEditorClose);
        imgclodetag = rootViewTagText.findViewById(R.id.imgPhotoEditorClose);


        tvAddress = rootView1Text.findViewById(R.id.tvEmailEditorText);
        tvMobile = rootViewMobileText.findViewById(R.id.tvMobileEditorText);
        tvTagline = rootViewTagText.findViewById(R.id.tvTagLine);
        tvWebsite = rootViewWebsite.findViewById(R.id.tvWebsite);
        View rootView = mLayoutInflater.inflate(R.layout.img_layout, null);
        logoImage = rootView.findViewById(R.id.imgPhotoEditorImage);
        View rootViewSocial = mLayoutInflater.inflate(R.layout.social_layout, null);
        relativeLayoutSocial = rootViewSocial.findViewById(R.id.rl_social);
        fbImage = rootViewSocial.findViewById(R.id.social_fb);
        twitter = rootViewSocial.findViewById(R.id.social_twitter);
        linkedin = rootViewSocial.findViewById(R.id.social_linkedin);
        instagram = rootViewSocial.findViewById(R.id.social_insta);
        imgcloselogo = rootView.findViewById(R.id.imgPhotoEditorClose);
        imgsocilaLogo = rootViewSocial.findViewById(R.id.imgPhotoEditorClose);
        photoEditorView.setImageView(mainImageView, null, this);
//        photoEditorView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                container.removeView(view);
//                return false;
//            }
//        });
        photoEditorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                txtEmail.setBackgroundResource(0);
                tvAddress.setBackgroundResource(0);
                tvMobile.setBackgroundResource(0);
                tvTagline.setBackgroundResource(0);
                tvWebsite.setBackgroundResource(0);
                relativeLayoutSocial.setBackgroundResource(0);
                seekBar1.setVisibility(View.GONE);
                logoImage.setBackgroundResource(0);
                imgclodetag.setVisibility(View.GONE);

                imgcloseEmail.setVisibility(View.GONE);

                imgcloseAddress.setVisibility(View.GONE);

                imgcloseMob.setVisibility(View.GONE);

                imgcloseWebsute.setVisibility(View.GONE);

                imgsocilaLogo.setVisibility(View.GONE);

                imgcloselogo.setVisibility(View.GONE);



                return false;
            }
        });
        if (getArguments() != null && getActivity() != null && getActivity().getIntent() != null) {
            final String imagePath = getArguments().getString(ImageEditor.EXTRA_IMAGE_PATH);
            postId = getArguments().getString(ImageEditor.EXTRA_HAS_POSTID);
            templateId = getArguments().getString(ImageEditor.EXTRA_HAS_TEMPLATEID);
            getTemplateDetail();
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
            spinner.setOnItemSelectedListener(this);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.text_spinner, fonts) {

                private View initView(int position, View convertView,
                                      ViewGroup parent) {
                    // It is used to set our custom view.
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.text_spinner, parent, false);
                    }

                    TextView textViewName = convertView.findViewById(R.id.tv_spinner);
                    Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), fonts[position]);

                    textViewName.setTypeface(externalFont);
                    textViewName.setText(fonts[position]);

                    // It is used the name to the TextView when the
                    // current item is not null.

                    return convertView;
                }

                public View getView(int position, View convertView, ViewGroup parent) {
                    return initView(position, convertView, parent);

                }


                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);

                    Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), fonts[position]);
                    ((TextView) v).setTypeface(externalFont);


                    return v;
                }
            };

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);


//            mainImageView.setMaxHeight(height - 100);
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


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvColor.setLayoutManager(layoutManager);
        rvColor.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                if (selectTextId == 1) {
                    txtEmail.setTextColor(colorCode);
                } else if (selectTextId == 2) {
                    tvAddress.setTextColor(colorCode);
                } else if (selectTextId == 3) {
                    tvMobile.setTextColor(colorCode);
                } else if (selectTextId == 4) {
                    tvWebsite.setTextColor(colorCode);
                } else if (selectTextId == 5) {
                    tvTagline.setTextColor(colorCode);
                } else if (selectTextId == 6) {
                    fbImage.setColorFilter(colorCode);
                    twitter.setColorFilter(colorCode);
                    linkedin.setColorFilter(colorCode);
                    instagram.setColorFilter(colorCode);

                }
            }
        });
        rvColor.setAdapter(colorPickerAdapter);


        color_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvColor.setVisibility(VISIBLE);
                seekBar1.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
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
                    txtEmail.setTextColor(color);
                } else if (selectTextId == 2) {
                    tvAddress.setTextColor(color);
                } else if (selectTextId == 3) {
                    tvMobile.setTextColor(color);
                } else if (selectTextId == 4) {
                    tvWebsite.setTextColor(color);
                } else if (selectTextId == 5) {
                    tvTagline.setTextColor(color);
                }

            }

            @Override
            public void onSettingsPressed() {
            }
        });
        imageButtonFontChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                spinner.setVisibility(VISIBLE);
                seekBar1.setVisibility(View.GONE);
                rvColor.setVisibility(View.GONE);

//                imageButtonAlignmentChanges.setVisibility(VISIBLE);
            }


        });
        image_font.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (selectTextId == 1) {
                    txtEmail.setTypeface(typeface, Typeface.BOLD);
                } else if (selectTextId == 2) {
                    tvAddress.setTypeface(typeface, Typeface.BOLD);
                } else if (selectTextId == 3) {
                    tvMobile.setTypeface(typeface, Typeface.BOLD);
                } else if (selectTextId == 4) {
                    tvWebsite.setTypeface(typeface, Typeface.BOLD);
                } else if (selectTextId == 5) {
                    tvTagline.setTypeface(typeface, Typeface.BOLD);
                }
            }
        });

        imageItalic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (selectTextId == 1) {
                    txtEmail.setTypeface(typeface, Typeface.ITALIC);
                } else if (selectTextId == 2) {
                    tvAddress.setTypeface(typeface, Typeface.ITALIC);
                } else if (selectTextId == 3) {
                    tvMobile.setTypeface(typeface, Typeface.ITALIC);
                } else if (selectTextId == 4) {
                    tvWebsite.setTypeface(typeface, Typeface.ITALIC);
                } else if (selectTextId == 5) {
                    tvTagline.setTypeface(typeface, Typeface.ITALIC);
                }
            }
        });

        imageButtonAlignmentChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar1.setVisibility(VISIBLE);
                spinner.setVisibility(View.GONE);
                rvColor.setVisibility(View.GONE);
            }
        });
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (selectTextId == 1) {
                    txtEmail.setTextSize(i);
                } else if (selectTextId == 2) {
                    tvAddress.setTextSize(i);
                } else if (selectTextId == 3) {
                    tvMobile.setTextSize(i);
                } else if (selectTextId == 4) {
                    tvWebsite.setTextSize(i);
                } else if (selectTextId == 5) {
                    tvTagline.setTextSize(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
//            String text = null;
//            Log.d("fest_name_img", FestivalName);
//            if (FestivalName.equalsIgnoreCase("congratulation")) {
//                text = "\n\n" + UserName + " Wishing you a" + " " + FestivalName.replace("+", " ") + " \n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://is.gd/FUEfj6";
//            } else if (FestivalName.equalsIgnoreCase("loveu")) {
//                text = "\n\n" + UserName + " say " + "LOVE U" + " \n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://is.gd/FUEfj6";
//            } else {
//                text = "\n\n" + UserName + " Wishing you a Happy" + " " + FestivalName.replace("+", " ") + " \n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://is.gd/FUEfj6";
//            }
//            //text = "\n\n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://goo.gl/SU7Xbp";
            txtEmail.setBackgroundResource(0);
            tvAddress.setBackgroundResource(0);
            tvMobile.setBackgroundResource(0);
            tvWebsite.setBackgroundResource(0);
            tvTagline.setBackgroundResource(0);
            relativeLayoutSocial.setBackgroundResource(0);
            logoImage.setBackgroundResource(0);
            imgclodetag.setVisibility(View.GONE);

            imgcloseEmail.setVisibility(View.GONE);

            imgcloseAddress.setVisibility(View.GONE);

            imgcloseMob.setVisibility(View.GONE);

            imgcloseWebsute.setVisibility(View.GONE);

            imgsocilaLogo.setVisibility(View.GONE);

            imgcloselogo.setVisibility(View.GONE);


            rvColor.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            seekBar1.setVisibility(View.GONE);
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
//            intent.putExtra(Intent.EXTRA_TEXT, text);
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
        progressBar.setVisibility(VISIBLE);
        String userId = shared_pr.getUserId();

        //  Log.d("DATA_URL","https://api.qwant.com/api/search/images?count=50&q="+festival+"+backgrounds&t=images&safesearch=1&locale=en_US&uiv=4");
//        pb_setimage.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, "https://www.kumbhdesign.in/mobile-app/depost/api/profile-update/" + userId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
//                   pb_setimage.setVisibility(View.GONE);

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("user_data");
                    int error = jsonObject2.getInt("error");
                    if (error == 0) {

                        JSONObject userData = jsonObject2.getJSONObject("user");
                        user = new UserData(userData.getString("facebook_url"), userData.getString("company_email"), userData.getString("company_logo_path"), userData.getString("website_url"), userData.getString("instagram_url"), userData.getString("linkedin_url"), userData.getString("mobile_number"), userData.getString("company_address"),userData.getString("twitter_url"));
                        Log.v("userData", user.getCompanyEmail());

                    }
                    if (user.getCompany_logo_path() != null && user.getCompany_logo_path().trim().length() > 0 && user.getCompany_logo_path().compareTo("null") != 0) {
                        addImage(0, user.getCompany_logo_path(), template.getTemplateLogoLeftPadding(), template.getTemplateLogoTopPadding(), densityData);

                    }
                    if (user.getCompanyEmail() != null && user.getCompanyEmail().trim().length() > 0 && user.getCompanyEmail().compareTo("null") != 0) {
                        setText("Email : " + user.getCompanyEmail(), Integer.parseInt(template.getTemplateEmailLeftPadding()), Integer.parseInt(template.getTemplateEmailTopPadding()));

                    }
                    if (user.getAddress() != null && user.getAddress().trim().length() > 0 && user.getAddress().compareTo("null") != 0) {
                        setAddressText(user.getAddress());
                    }
                    if (user.getMobileNumber() != null && user.getMobileNumber().trim().length() > 0 && user.getMobileNumber().compareTo("null") != 0 ) {
                        setMobileText("Phone : " + user.getMobileNumber(), Integer.parseInt(template.getTemplateMobileLeftPadding()), Integer.parseInt(template.getTemplateMobileTopPadding()));

                    }
                    if (user.getWebsiteUrl() != null && user.getWebsiteUrl().trim().length() > 0 && user.getWebsiteUrl().compareTo("null") != 0 ) {
                        setWebsiteText(user.getWebsiteUrl(), Integer.parseInt(template.getTemplateMobileLeftPadding()), Integer.parseInt(template.getTemplateMobileTopPadding()));

                    }
                    if (template.getTemplateTagline() != null && template.getTemplateTagline().trim().length() > 0 &&  template.getTemplateTagline().compareTo("null") != 0) {
                        setTagLineText(template.getTemplateTagline(), Integer.parseInt(template.getTemplateMobileLeftPadding()), Integer.parseInt(template.getTemplateMobileTopPadding()));

                    }


//                    setAddressText(user.getAddress(), Integer.parseInt(template.getTemplateAddressLeftPadding()), Integer.parseInt(template.getTemplateAddressTopPadding()));
                    addSocailImage(user, template.getTemplateSocialLeftPadding(), template.getTemplateSocialTopPadding(), densityData);
//                    photoEditorView.addImage(null,user.getCompany_logo_path());


                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
//                        pb_setimage.setVisibility(View.GONE);
                        Log.d("Error", error.toString());
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }


    private void getTemplateDetail() {

        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);


        // finally, execute the request
        Call<ResponseTemplates> call = service.postTemplates(postId, templateId);
        call.enqueue(new Callback<ResponseTemplates>() {
            @Override
            public void onResponse(Call<ResponseTemplates> call, retrofit2.Response<ResponseTemplates> response) {
                template = response.body().getResponse().getTemplate().getListTemplate();
                getData();
            }

            @Override
            public void onFailure(Call<ResponseTemplates> call, Throwable t) {

            }
        });

    }

    public void setText(String text, int left, int top) {
        final View rootViewText = mLayoutInflater.inflate(R.layout.view_text_layout, null);

        Random r = new Random();
//        int i1 = r.nextInt(8 - 1 + 1) + 1;
        txtEmail = rootViewText.findViewById(R.id.tvPhotoEditorText);
        int color = Color.parseColor(template.getTemplateThemeColor());
        txtEmail.setTextColor(color);
        imgcloseEmail = rootViewText.findViewById(R.id.imgPhotoEditorClose);
        imgcloseEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeView(rootViewText);
            }
        });
//        typeface = Typeface.createFromAsset(getActivity().getAssets(),fonts[i1]);
//      txtText.setTypeface(typeface);


        txtEmail.setText(text);
//        txtEmail.setPadding(left/densityData, top/densityData, 0, 0);
//        txtEmail.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 20, 0, 0);
        MultiTouchListener multiTouchListener = new MultiTouchListener(null, container, imageView, true, this, layoutParams);
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {
                selectTextId = 1;
                color_icon.setVisibility(VISIBLE);
                imageButtonFontChanges.setVisibility(VISIBLE);
                image_font.setVisibility(VISIBLE);
                txtEmail.setBackgroundResource(R.drawable.rounded_border_tv);
                tvAddress.setBackgroundResource(0);
                tvMobile.setBackgroundResource(0);
                logoImage.setBackgroundResource(0);
                relativeLayoutSocial.setBackgroundResource(0);
                tvTagline.setBackgroundResource(0);
                tvWebsite.setBackgroundResource(0);
                imageItalic.setVisibility(VISIBLE);
                imgcloseEmail.setVisibility(VISIBLE);
                cardView.setVisibility(VISIBLE);
                imgclodetag.setVisibility(View.GONE);
                imgcloseMob.setVisibility(View.GONE);
                imgcloseAddress.setVisibility(View.GONE);
                imgcloselogo.setVisibility(View.GONE);

                imgsocilaLogo.setVisibility(View.GONE);
                imgcloseWebsute.setVisibility(View.GONE);
                imgsocilaLogo.setVisibility(View.GONE);

//                imageButtonAlignmentChanges.setVisibility(VISIBLE);

                selectViewIndex = currentView.getId();
            }

            @Override
            public void onLongClick() {

            }


        });


        rootViewText.setOnTouchListener(multiTouchListener);
        container.addView(rootViewText, layoutParams);
    }

    public void setAddressText(String text) {
        final View rootViewText = mLayoutInflater.inflate(R.layout.view_email_layout, null);
        tvAddress = rootViewText.findViewById(R.id.tvEmailEditorText);
        imgcloseAddress = rootViewText.findViewById(R.id.imgPhotoEditorClose);
        imgcloseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeView(rootViewText);
            }
        });
//        final FrameLayout frmBorder = rootViewText.findViewById(R.id.frmBorder);
        tvAddress.setText(text);
//        Typeface typefaceData = Typeface.createFromFile(template.getTemplateFontFamilyPath());
//        tvemail.setTypeface(typefaceData);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        MultiTouchListener multiTouchListener = new MultiTouchListener(null, container, imageView, true, this, layoutParams);
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {
                selectTextId = 2;
                tvAddress.setBackgroundResource(R.drawable.rounded_border_tv);
                txtEmail.setBackgroundResource(0);
                tvMobile.setBackgroundResource(0);
                imgcloseAddress.setVisibility(VISIBLE);

                imgclodetag.setVisibility(View.GONE);
                imgcloseMob.setVisibility(View.GONE);
                imgcloseEmail.setVisibility(View.GONE);
                imgcloselogo.setVisibility(View.GONE);
                imgsocilaLogo.setVisibility(View.GONE);
                imgsocilaLogo.setVisibility(View.GONE);
                imgcloseWebsute.setVisibility(View.GONE);
                logoImage.setBackgroundResource(0);
                relativeLayoutSocial.setBackgroundResource(0);
                tvTagline.setBackgroundResource(0);
                tvWebsite.setBackgroundResource(0);
                cardView.setVisibility(VISIBLE);
//                color_icon.setVisibility(VISIBLE);

//                imageButtonFontChanges.setVisibility(VISIBLE);
//                image_font.setVisibility(VISIBLE);
//                imageItalic.setVisibility(VISIBLE);
//                imageButtonAlignmentChanges.setVisibility(VISIBLE);

                selectedView = currentView;
                selectViewIndex = currentView.getId();
            }

            @Override
            public void onLongClick() {

            }


        });


        rootViewText.setOnTouchListener(multiTouchListener);
        container.addView(rootViewText, layoutParams);
    }

    public void setMobileText(String text, int left, int top) {
        final View rootViewText = mLayoutInflater.inflate(R.layout.view_mobile_layout, null);


        tvMobile = rootViewText.findViewById(R.id.tvMobileEditorText);
        tvMobile.setText(text);
        imgcloseMob = rootViewText.findViewById(R.id.imgPhotoEditorClose);
        if (imgcloseMob != null) {
            imgcloseMob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    container.removeView(rootViewText);


                }
            });
        }
        int color = Color.parseColor(template.getTemplateThemeColor());
        tvMobile.setTextColor(color);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 120, 0, 0);
        MultiTouchListener multiTouchListener = new MultiTouchListener(null, container, imageView, true, this, layoutParams);
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {
                selectTextId = 3;
                tvMobile.setBackgroundResource(R.drawable.rounded_border_tv);
                tvAddress.setBackgroundResource(0);
                txtEmail.setBackgroundResource(0);
                tvTagline.setBackgroundResource(0);
                tvWebsite.setBackgroundResource(0);
                logoImage.setBackgroundResource(0);
                relativeLayoutSocial.setBackgroundResource(0);
                cardView.setVisibility(VISIBLE);
                imgsocilaLogo.setVisibility(View.GONE);
                imgcloseMob.setVisibility(VISIBLE);

                imgclodetag.setVisibility(View.GONE);
                imgcloseAddress.setVisibility(View.GONE);
                imgcloseEmail.setVisibility(View.GONE);
                imgcloselogo.setVisibility(View.GONE);

                imgsocilaLogo.setVisibility(View.GONE);
                imgcloseWebsute.setVisibility(View.GONE);


//                color_icon.setVisibility(VISIBLE);

//                imageButtonFontChanges.setVisibility(VISIBLE);
//                image_font.setVisibility(VISIBLE);
//                imageItalic.setVisibility(VISIBLE);
//                imageButtonAlignmentChanges.setVisibility(VISIBLE);

                selectedView = currentView;
                selectViewIndex = currentView.getId();
            }

            @Override
            public void onLongClick() {

            }


        });


        rootViewText.setOnTouchListener(multiTouchListener);
        container.addView(rootViewText, layoutParams);
    }

    public void setWebsiteText(String text, int left, int top) {
        final View rootViewText = mLayoutInflater.inflate(R.layout.view_website_layout, null);


        tvWebsite = rootViewText.findViewById(R.id.tvWebsite);
        tvWebsite.setText(text);
        imgcloseWebsute = rootViewText.findViewById(R.id.imgPhotoEditorClose);
        if (imgcloseWebsute != null) {
            imgcloseWebsute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    container.removeView(rootViewText);


                }
            });
        }
        int color = Color.parseColor(template.getTemplateThemeColor());
        tvWebsite.setTextColor(color);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        MultiTouchListener multiTouchListener = new MultiTouchListener(null, container, imageView, true, this, layoutParams);
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {
                selectTextId = 4;
                tvWebsite.setBackgroundResource(R.drawable.rounded_border_tv);
                tvAddress.setBackgroundResource(0);
                tvMobile.setBackgroundResource(0);
                tvTagline.setBackgroundResource(0);
                txtEmail.setBackgroundResource(0);
                logoImage.setBackgroundResource(0);
                cardView.setVisibility(VISIBLE);
                relativeLayoutSocial.setBackgroundResource(0);
                imgsocilaLogo.setVisibility(View.GONE);
                imgclodetag.setVisibility(View.GONE);
                imgcloseAddress.setVisibility(View.GONE);
                imgcloseEmail.setVisibility(View.GONE);
                imgcloseMob.setVisibility(View.GONE);
                imgcloselogo.setVisibility(View.GONE);

                imgsocilaLogo.setVisibility(View.GONE);
                imgcloseWebsute.setVisibility(VISIBLE);

//                color_icon.setVisibility(VISIBLE);

//                imageButtonFontChanges.setVisibility(VISIBLE);
//                image_font.setVisibility(VISIBLE);
//                imageItalic.setVisibility(VISIBLE);
//                imageButtonAlignmentChanges.setVisibility(VISIBLE);

                selectedView = currentView;
                selectViewIndex = currentView.getId();
            }

            @Override
            public void onLongClick() {

            }


        });


        rootViewText.setOnTouchListener(multiTouchListener);
        container.addView(rootViewText, layoutParams);
    }

    public void setTagLineText(String text, int left, int top) {
        final View rootViewText = mLayoutInflater.inflate(R.layout.view_tag_layout, null);


        tvTagline = rootViewText.findViewById(R.id.tvTagLine);
        tvTagline.setText(text);
        imgclodetag = rootViewText.findViewById(R.id.imgPhotoEditorClose);
        imgclodetag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeView(rootViewText);
            }
        });

        int color = Color.parseColor(template.getTemplateThemeColor());
        tvTagline.setTextColor(color);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 100, 0, 0);
        MultiTouchListener multiTouchListener = new MultiTouchListener(null, container, imageView, true, this, layoutParams);
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {
                selectTextId = 5;
                tvTagline.setBackgroundResource(R.drawable.rounded_border_tv);
                tvAddress.setBackgroundResource(0);
                tvMobile.setBackgroundResource(0);
                tvWebsite.setBackgroundResource(0);
                logoImage.setBackgroundResource(0);
                txtEmail.setBackgroundResource(0);
                relativeLayoutSocial.setBackgroundResource(0);
                cardView.setVisibility(VISIBLE);

                imgsocilaLogo.setVisibility(View.GONE);
                imgclodetag.setVisibility(VISIBLE);
                imgcloseAddress.setVisibility(View.GONE);
                imgcloseEmail.setVisibility(View.GONE);
                imgcloseMob.setVisibility(View.GONE);


                imgsocilaLogo.setVisibility(View.GONE);
                imgcloseWebsute.setVisibility(View.GONE);


//                color_icon.setVisibility(VISIBLE);

//                imageButtonFontChanges.setVisibility(VISIBLE);
//                image_font.setVisibility(VISIBLE);
//                imageItalic.setVisibility(VISIBLE);
//                imageButtonAlignmentChanges.setVisibility(VISIBLE);

                selectedView = currentView;
                selectViewIndex = currentView.getId();
            }

            @Override
            public void onLongClick() {

            }


        });


        rootViewText.setOnTouchListener(multiTouchListener);
        container.addView(rootViewText, layoutParams);
    }

    public void addImage(int desiredImage, String path, String templateLogoLeftPadding, String templateLogoTopPadding, int densityData) {
        Log.v("imagepath",path);

        final View rootView = mLayoutInflater.inflate(R.layout.img_layout, null);
        logoImage = rootView.findViewById(R.id.imgPhotoEditorImage);

        if (path != null) {
            Picasso.get().load(path).into(logoImage);
        } else {
            logoImage.setImageResource(desiredImage);
        }
        imgcloselogo = rootView.findViewById(R.id.imgPhotoEditorClose);
        imgcloselogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeView(rootView);
            }
        });

//        imageView.setPadding(Integer.parseInt(templateLogoLeftPadding) / densityData, Integer.parseInt(templateLogoTopPadding) / densityData, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        MultiTouchListener multiTouchListener = new MultiTouchListener(null, container, imageView, true, this, layoutParams);
        multiTouchListener.setOnMultiTouchListener(new MultiTouchListener.OnMultiTouchListener() {
            @Override
            public void
            onRemoveViewListener(View removedView) {
                selectTextId = 3;
                container.removeView(removedView);
                logoImage.setImageDrawable(null);

                selectedView = null;
            }
        });
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {

                selectTextId = 0;
                logoImage.setBackgroundResource(R.drawable.rounded_border_tv);
                txtEmail.setBackgroundResource(0);
                tvMobile.setBackgroundResource(0);
                tvAddress.setBackgroundResource(0);
                tvWebsite.setBackgroundResource(0);
                tvTagline.setBackgroundResource(0);
                relativeLayoutSocial.setBackgroundResource(0);


                imgcloseEmail.setVisibility(View.GONE);

                imgclodetag.setVisibility(View.GONE);
                imgcloseMob.setVisibility(View.GONE);
                imgcloseAddress.setVisibility(View.GONE);
                imgcloselogo.setVisibility(View.GONE);

                imgcloselogo.setVisibility(VISIBLE);
                imgcloseWebsute.setVisibility(View.GONE);

                imgsocilaLogo.setVisibility(View.GONE);
            }

            @Override
            public void onLongClick() {

            }


        });
        rootView.setOnTouchListener(multiTouchListener);
        container.addView(rootView);


    }

    public void addSocailImage(UserData user, String templateLogoLeftPadding, String templateLogoTopPadding, int densityData) {


        final View rootView = mLayoutInflater.inflate(R.layout.social_layout, null);
        relativeLayoutSocial = rootView.findViewById(R.id.rl_social);
        fbImage = rootView.findViewById(R.id.social_fb);
        linkedin = rootView.findViewById(R.id.social_linkedin);
        instagram = rootView.findViewById(R.id.social_insta);
        twitter = rootView.findViewById(R.id.social_twitter);
        imgsocilaLogo = rootView.findViewById(R.id.imgPhotoEditorClose);
        imgsocilaLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeView(rootView);
            }
        });

        if (user.getFacebookUrl() != null && user.getFacebookUrl().trim().length() > 0 && user.getFacebookUrl().compareTo("null") != 0  ) {

            fbImage.setVisibility(VISIBLE);
        }

        if (user.getLinkedinUrl() != null && user.getLinkedinUrl().trim().length() > 0 && user.getLinkedinUrl().compareTo("null") != 0 ) {
            linkedin = rootView.findViewById(R.id.social_linkedin);
            linkedin.setVisibility(VISIBLE);
        }
        if (user.getTwitter_url() != null && user.getTwitter_url().trim().length() > 0 && user.getTwitter_url().compareTo("null") != 0) {
            twitter = rootView.findViewById(R.id.social_twitter);
            twitter.setVisibility(VISIBLE);
        }

        if (user.getInstagramUrl() != null && user.getInstagramUrl().trim().length() > 0 && user.getInstagramUrl().compareTo("null") != 0) {
            instagram = rootView.findViewById(R.id.social_insta);
            instagram.setVisibility(VISIBLE);
        }

//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(40, 40);
//        layoutParams.gravity=Gravity.BOTTOM;
//        fbImag.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParam2s = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fbImage.getLayoutParams().height = 40;
        fbImage.getLayoutParams().width = 40;
        linkedin.getLayoutParams().height = 40;
        linkedin.getLayoutParams().width = 40;
        twitter.getLayoutParams().height = 40;
        twitter.getLayoutParams().width = 40;
        instagram.getLayoutParams().height = 40;
        instagram.getLayoutParams().width = 40;

        layoutParam2s.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParam2s.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        MultiTouchListener multiTouchListener = new MultiTouchListener(null, container, imageView, true, this, layoutParam2s);

        multiTouchListener.setOnMultiTouchListener(new MultiTouchListener.OnMultiTouchListener() {
            @Override
            public void
            onRemoveViewListener(View removedView) {
//                selectTextId = 3;

                container.removeView(removedView);
                imageView.setImageDrawable(null);

                selectedView = null;
            }
        });
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick(View currentView) {
                selectTextId = 6;
                relativeLayoutSocial.setBackgroundResource(R.drawable.rounded_border_tv);
                txtEmail.setBackgroundResource(0);
                tvMobile.setBackgroundResource(0);
                tvAddress.setBackgroundResource(0);
                tvWebsite.setBackgroundResource(0);
                tvTagline.setBackgroundResource(0);
                logoImage.setBackgroundResource(0);

                imgsocilaLogo.setVisibility(View.GONE);
                imgclodetag.setVisibility(View.GONE);
                imgcloseAddress.setVisibility(View.GONE);
                imgcloseEmail.setVisibility(View.GONE);
                imgcloseMob.setVisibility(View.GONE);


                imgsocilaLogo.setVisibility(VISIBLE);
                imgcloseWebsute.setVisibility(View.GONE);
                cardView.setVisibility(VISIBLE);
            }

            @Override
            public void onLongClick() {

            }
        });

        rootView.setOnTouchListener(multiTouchListener);
        container.addView(rootView, layoutParam2s);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), adapterView.getItemAtPosition(i).toString());
        if (selectTextId == 1) {
            txtEmail.setTypeface(externalFont);
        } else if (selectTextId == 2) {
            tvAddress.setTypeface(externalFont);
        } else if (selectTextId == 3) {
            tvMobile.setTypeface(externalFont);
        } else if (selectTextId == 4) {
            tvWebsite.setTypeface(externalFont);
        } else if (selectTextId == 5) {
            tvTagline.setTypeface(externalFont);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}




