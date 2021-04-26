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
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.droidninja.imageeditengine.brush.DrawableOnTouchView;
import com.droidninja.imageeditengine.utils.Matrix3;
import com.droidninja.imageeditengine.views.PhotoEditorView;
import com.droidninja.imageeditengine.views.ViewTouchListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.droidninja.imageeditengine.ImageEditor.Builder.FestivalName;
import static com.droidninja.imageeditengine.ImageEditor.Builder.UserName;
import static com.droidninja.imageeditengine.views.PhotoEditorView.animationView;
import static com.droidninja.imageeditengine.views.PhotoEditorView.layout;
import static com.droidninja.imageeditengine.views.PhotoEditorView.mainImageView;
import static com.droidninja.imageeditengine.views.PhotoEditorView.recyclerView;

public class PhotoEditorFragment extends BaseFragment implements View.OnClickListener, ViewTouchListener {

    public static ImageView stickerButton;
    public static ImageView addTextButton;
    public static Button share;
    public static ImageView imgshoot;
    PhotoEditorView photoEditorView;
    public static ImageView paintButton;
    ImageView deleteButton;
    public static ImageView back_color;
    View toolbarLayout;
    Button doneBtn;
    public static ImageView back_iv;
    private Bitmap mainBitmap;
    RelativeLayout re_layout;

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

    float scalediff;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = ZOOM;
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    boolean isRotateEnabled = true;

    //SessionManager shred_pr;

    // private ImageView imageView;
    private ViewTouchListener viewTouchListener;

    private static final int ANIMATION_DURATION = 180;

    AnimatorSet animatorSet;
    Matrix m = new Matrix();
    Float scale = 1f;
    private DrawableOnTouchView drawableOnTouchView;

    public static PhotoEditorFragment newInstance(String imagePath) {
        Bundle bundle = new Bundle();
        bundle.putString(ImageEditor.EXTRA_IMAGE_PATH, imagePath);
        PhotoEditorFragment photoEditorFragment = new PhotoEditorFragment();
        photoEditorFragment.setArguments(bundle);
        return photoEditorFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public PhotoEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_editor, container, false);
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

    public void setImageBitmap(Bitmap bitmap) {
        mainImageView.setImageBitmap(bitmap);
        mainImageView.post(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    public void setImageWithRect(Rect rect) {
        mainBitmap = getScaledBitmap(getCroppedBitmap(getBitmapCache(originalBitmap), rect));
        mainImageView.setImageBitmap(mainBitmap);
    }

    private Bitmap getScaledBitmap(Bitmap resource) {
        int currentBitmapWidth = resource.getWidth();
        int currentBitmapHeight = resource.getHeight();
        int ivWidth = mainImageView.getWidth();
        int newHeight = (int) Math.floor(
                (double) currentBitmapHeight * ((double) ivWidth / (double) currentBitmapWidth));
        return Bitmap.createScaledBitmap(resource, ivWidth, newHeight, true);
    }

    private Bitmap getCroppedBitmap(Bitmap srcBitmap, Rect rect) {
        // Crop the subset from the original Bitmap.
        return Bitmap.createBitmap(srcBitmap,
                rect.left,
                rect.top,
                (rect.right - rect.left),
                (rect.bottom - rect.top));
    }

    public void reset() {
        photoEditorView.reset();
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initView(View view) {
        stickerButton = view.findViewById(R.id.stickers_btn);
        addTextButton = view.findViewById(R.id.add_text_btn);
        share = view.findViewById(R.id.share);
        deleteButton = view.findViewById(R.id.delete_view);
        photoEditorView = view.findViewById(R.id.photo_editor_view);
        paintButton = view.findViewById(R.id.paint_btn);
        toolbarLayout = view.findViewById(R.id.toolbar_layout);
        back_iv = view.findViewById(R.id.back_iv);
        re_layout = view.findViewById(R.id.re_layout);
        back_color = view.findViewById(R.id.back_color);
        imgshoot = view.findViewById(R.id.imgshoot);
        textSizes = getResources().getIntArray(R.array.fontSize);



//        photoEditorView.getImage();
        if (getArguments() != null && getActivity() != null && getActivity().getIntent() != null) {
            final String imagePath = getArguments().getString(ImageEditor.EXTRA_IMAGE_PATH);
            animationView.setVisibility(View.VISIBLE);
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

            Intent intent = getActivity().getIntent();
            setVisibility(addTextButton, intent.getBooleanExtra(ImageEditor.EXTRA_IS_TEXT_MODE, false));
            setVisibility(stickerButton, intent.getBooleanExtra(ImageEditor.EXTRA_IS_STICKER_MODE, false));
            setVisibility(paintButton, intent.getBooleanExtra(ImageEditor.EXTRA_IS_PAINT_MODE, false));

            photoEditorView.setImageView(mainImageView, deleteButton, this);
            stickerButton.setOnClickListener(this);
            addTextButton.setOnClickListener(this);
            paintButton.setOnClickListener(this);
            share.setOnClickListener(this);
            back_iv.setOnClickListener(this);
            back_color.setOnClickListener(this);
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
    }

    private static float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            degrees -= 360.0f;
        } else if (degrees < -180.0f) {
            degrees += 360.0f;
        }

        return degrees;
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

    protected void onModeChanged(int currentMode) {
        Log.i(ImageEditActivity.class.getSimpleName(), "CM: " + currentMode);
        onStickerMode(currentMode == MODE_STICKER);
        onAddTextMode(currentMode == MODE_ADD_TEXT);
        onPaintMode(currentMode == MODE_PAINT);
        onshareMode(currentMode == MODE_SHARE);
        onback_color(currentMode == MODE_BACK_COLOR);

        if (currentMode == MODE_ADD_TEXT) {
        } else {
        }
    }


    @Override
    public void onClick(final View view) {
        int id = view.getId();
        if (id == R.id.stickers_btn) {
            setMode(MODE_STICKER);
        } else if (id == R.id.back_color) {
            setMode(MODE_BACK_COLOR);
        } else if (id == R.id.add_text_btn) {
            back_iv.setVisibility(View.GONE);
            paintButton.setVisibility(View.GONE);
            stickerButton.setVisibility(View.GONE);
            addTextButton.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            back_color.setVisibility(View.GONE);
            setMode(MODE_ADD_TEXT);
        } else if (id == R.id.paint_btn) {
            setMode(MODE_PAINT);
        } else if (id == R.id.back_iv) {
            showSaveDialog();
        } else if (id == R.id.share) {
            setMode(MODE_SHARE);
        }
        if (currentMode != MODE_NONE) {
            photoEditorView.animate().scaleX(1f);
            photoEditorView.animate().scaleY(1f);
        } else {
        }
    }

    private void onback_color(boolean status) {
        if (status) {

        }
    }

    private void onAddTextMode(boolean status) {
        if (status) {
            photoEditorView.addText();
        } else {
            photoEditorView.hideTextMode();
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
                        paintButton.setVisibility(View.VISIBLE);
                        stickerButton.setVisibility(View.VISIBLE);
                        addTextButton.setVisibility(View.VISIBLE);
                        share.setVisibility(View.VISIBLE);
                        back_iv.setVisibility(View.VISIBLE);
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

    public void onStickerMode(boolean status) {
        Log.d("Status", String.valueOf(status));

        String folderName = getActivity().getIntent().getStringExtra(ImageEditor.EXTRA_STICKER_FOLDER_NAME);
        photoEditorView.showStickers(folderName);
    }

    private void onshareMode(boolean status) {
        if (status) {
            String text = null;
            Log.d("fest_name_img",FestivalName);
            if(FestivalName.equalsIgnoreCase("congratulation")){
                text = "\n\n" + UserName  + " Wishing you a" + " " + FestivalName.replace("+", " ") + " \n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://is.gd/FUEfj6";
            }else if(FestivalName.equalsIgnoreCase("loveu")){
                text = "\n\n" + UserName + " say " + "LOVE U" + " \n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://is.gd/FUEfj6";
            }else {
                text = "\n\n" + UserName + " Wishing you a Happy" + " " + FestivalName.replace("+", " ") + " \n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://is.gd/FUEfj6";
            }
            //text = "\n\n" + getString(com.droidninja.imageeditengine.R.string.share) + " \n https://goo.gl/SU7Xbp";

            recyclerView.setVisibility(View.GONE);
            Bitmap bitmap = Bitmap.createBitmap(photoEditorView.getWidth(), photoEditorView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            photoEditorView.draw(canvas);

            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.watermark_img);
            bitmap1 = mark(bitmap1, "Hello");

//           imgshoot.setImageBitmap(bitmap1);

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

    public Bitmap mark(Bitmap src,String watermark) {
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
        if (currentMode != MODE_ADD_TEXT) {
            Log.i(ImageEditActivity.class.getSimpleName(), "onStartViewChangeListener" + "" + view.getId());
            toolbarLayout.setVisibility(View.GONE);
            stickerButton.setVisibility(View.GONE);
            addTextButton.setVisibility(View.GONE);
            paintButton.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            back_iv.setVisibility(View.GONE);
            back_color.setVisibility(View.GONE);
            AnimationHelper.animate(getContext(), deleteButton, R.anim.fade_in_medium, View.VISIBLE, null);
        }
    }

    @Override
    public void onStopViewChangeListener(View view) {
        if (currentMode != MODE_ADD_TEXT) {
            Log.i(ImageEditActivity.class.getSimpleName(), "onStopViewChangeListener" + "" + view.getId());
            deleteButton.setVisibility(View.GONE);
            stickerButton.setVisibility(View.VISIBLE);
            addTextButton.setVisibility(View.VISIBLE);
            paintButton.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            back_iv.setVisibility(View.VISIBLE);
            back_color.setVisibility(View.GONE);
            AnimationHelper.animate(getContext(), toolbarLayout, R.anim.fade_in_medium, View.VISIBLE, null);
        }
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {
    }

    private Bitmap getBitmapCache(Bitmap bitmap) {
        Matrix touchMatrix = mainImageView.getMatrix();

        Bitmap resultBit = Bitmap.createBitmap(bitmap).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(resultBit);

        float[] data = new float[9];
        touchMatrix.getValues(data);
        Matrix3 cal = new Matrix3(data);
        Matrix3 inverseMatrix = cal.inverseMatrix();
        Matrix m = new Matrix();
        m.setValues(inverseMatrix.getValues());

        float[] f = new float[9];
        m.getValues(f);
        int dx = (int) f[Matrix.MTRANS_X];
        int dy = (int) f[Matrix.MTRANS_Y];
        float scale_x = f[Matrix.MSCALE_X];
        float scale_y = f[Matrix.MSCALE_Y];
        canvas.save();
        canvas.translate(dx, dy);
        canvas.scale(scale_x, scale_y);

        photoEditorView.setDrawingCacheEnabled(true);
        if (photoEditorView.getDrawingCache() != null) {
            canvas.drawBitmap(photoEditorView.getDrawingCache(), 0, 0, null);
        }

        if (photoEditorView.getPaintBit() != null) {
            canvas.drawBitmap(photoEditorView.getPaintBit(), 0, 0, null);
        }

        canvas.restore();
        return resultBit;
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
}
