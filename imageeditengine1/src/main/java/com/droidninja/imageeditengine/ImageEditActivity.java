package com.droidninja.imageeditengine;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.droidninja.imageeditengine.utils.FragmentUtil;

import java.util.Calendar;

import static com.droidninja.imageeditengine.ImageEditor.EXTRA_IMAGE_PATH;
import static com.droidninja.imageeditengine.ImageEditor.EXTRA_NAME;

public class ImageEditActivity extends BaseImageEditActivity {
    private Rect cropRect;
    public PhotoEditorFragment photoEditorFragment;
    Bitmap bmp;
    byte[] byteArray;
    String imagePath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_edit);


        String name = getIntent().getStringExtra(EXTRA_NAME);
        if (name != null && name.compareTo("FESTIVAL") == 0) {
         byteArray = getIntent().getByteArrayExtra(ImageEditor.EXTRA_IMAGE_PATH);
         bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "IMG_" + Calendar.getInstance().getTime().getSeconds(), null);
            FragmentUtil.addFragment(this, R.id.fragment_container,
                    PhotoFestivalFragment.newInstance(path));
        }
        else{
             imagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);
        }


        if (imagePath != null) {
            FragmentUtil.addFragment(this, R.id.fragment_container,
                    PhotoEditorFragment.newInstance(imagePath));
        }
    }

    public void showdialog() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ImageEditActivity.this);
        alertDialog.setMessage("Do You Want to DISCARD this Image?");

        alertDialog.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        showdialog();
    }
}
