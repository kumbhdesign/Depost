package com.droidninja.imageeditengine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageEditor {
    public static final int EDITOR_STICKER = 1;
    public static final int EDITOR_TEXT = 2;
    public static final int EDITOR_PAINT = 3;
    public static final int EDITOR_CROP = 4;
    public static final int EDITOR_FILTERS = 5;

    public static final String EXTRA_STICKER_FOLDER_NAME = "EXTRA_STICKER_FOLDER_NAME";
    public static final String EXTRA_IS_TEXT_MODE = "EXTRA_IS_TEXT_MODE";
    public static final String EXTRA_IS_PAINT_MODE = "EXTRA_IS_PAINT_MODE";
    public static final String EXTRA_IS_STICKER_MODE = "EXTRA_IS_STICKER_MODE";
    public static final String EXTRA_IS_CROP_MODE = "EXTRA_IS_CROP_MODE";
    public static final String EXTRA_HAS_FILTERS = "EXTRA_HAS_FILTERS";
    public static final String EXTRA_HAS_POSTID = "EXTRA_HAS_POSTID";
    public static final String EXTRA_HAS_TEMPLATEID = "EXTRA_HAS_TEMPLATEID";
    public static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";
    public static final String EXTRA_NAME = "FESTIVAL";

    public static final int RC_IMAGE_EDITOR = 0x34;

    public static class Builder {

        private final String imagePath;
        private Activity context;
        private String stickerFolderName;
        private boolean enabledEditorText = true;
        private boolean enabledEditorPaint = true;
        private boolean enabledEditorSticker = false;
        private boolean enableEditorCrop = false;
        private boolean enableFilters = true;
        public static String FestivalName, UserName;

        public Builder(Activity context, String imagePath, String Festival, String UserName) {
            this.context = context;
            this.imagePath = imagePath;
            this.FestivalName = Festival;
            this.UserName = UserName;
        }

        public Builder setStickerAssets(String folderName) {
            this.stickerFolderName = folderName;
            enabledEditorSticker = true;
            return this;
        }

        public Builder disable(int editorType) {
            if (editorType == EDITOR_TEXT) {
                enabledEditorText = false;
            } else if (editorType == EDITOR_PAINT) {
                enabledEditorPaint = false;
            } else if (editorType == EDITOR_STICKER) {
                enabledEditorSticker = false;
            }

            return this;
        }

        public void open() {
            if (imagePath != null && (new File(imagePath).exists())) {
                Intent intent = new Intent(context, ImageEditActivity.class);
                intent.putExtra(ImageEditor.EXTRA_STICKER_FOLDER_NAME, stickerFolderName);
                intent.putExtra(ImageEditor.EXTRA_IS_PAINT_MODE, enabledEditorPaint);
                intent.putExtra(ImageEditor.EXTRA_IS_STICKER_MODE, enabledEditorSticker);
                intent.putExtra(ImageEditor.EXTRA_IS_TEXT_MODE, enabledEditorText);
                intent.putExtra(ImageEditor.EXTRA_IS_CROP_MODE, enableEditorCrop);
                intent.putExtra(ImageEditor.EXTRA_HAS_FILTERS, enableFilters);
                intent.putExtra(ImageEditor.EXTRA_IMAGE_PATH, imagePath);
                context.startActivityForResult(intent, RC_IMAGE_EDITOR);
            } else {
                Intent intent = new Intent(context, ImageEditActivity.class);
                intent.putExtra(ImageEditor.EXTRA_STICKER_FOLDER_NAME, stickerFolderName);
                intent.putExtra(ImageEditor.EXTRA_IS_PAINT_MODE, enabledEditorPaint);
                intent.putExtra(ImageEditor.EXTRA_IS_STICKER_MODE, enabledEditorSticker);
                intent.putExtra(ImageEditor.EXTRA_IS_TEXT_MODE, enabledEditorText);
                intent.putExtra(ImageEditor.EXTRA_IS_CROP_MODE, enableEditorCrop);
                intent.putExtra(ImageEditor.EXTRA_HAS_FILTERS, enableFilters);
                intent.putExtra(ImageEditor.EXTRA_IMAGE_PATH, imagePath);
                context.startActivityForResult(intent, RC_IMAGE_EDITOR);
            }
        }

        public void festivalCall(String b, String postId, String templateId) {
            if (imagePath != null && (new File(imagePath).exists())) {
                Intent intent = new Intent(context, ImageEditActivity.class);
                intent.putExtra(ImageEditor.EXTRA_IS_PAINT_MODE, enabledEditorPaint);
                intent.putExtra(ImageEditor.EXTRA_IS_STICKER_MODE, enabledEditorSticker);
                intent.putExtra(ImageEditor.EXTRA_IS_TEXT_MODE, enabledEditorText);
                intent.putExtra(ImageEditor.EXTRA_IS_CROP_MODE, enableEditorCrop);
                intent.putExtra(ImageEditor.EXTRA_HAS_FILTERS, enableFilters);
                intent.putExtra(ImageEditor.EXTRA_IMAGE_PATH, imagePath);
                intent.putExtra(ImageEditor.EXTRA_HAS_POSTID, postId);
                intent.putExtra(ImageEditor.EXTRA_HAS_TEMPLATEID, templateId);
//                Bundle bundle = new Bundle();
//                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//                b.compress(Bitmap.CompressFormat.PNG, 100, bStream);
//                byte[] byteArray = bStream.toByteArray();
//
//                bundle.putByteArray(ImageEditor.EXTRA_IMAGE_PATH, byteArray);
                intent.putExtra(ImageEditor.EXTRA_IMAGE_PATH,b);
//                bundle.putString(ImageEditor.EXTRA_NAME, "FESTIVAL");
                context.startActivityForResult(intent, RC_IMAGE_EDITOR);
            } else {
                Intent intent = new Intent(context, ImageEditActivity.class);
//                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//                b.compress(Bitmap.CompressFormat.PNG, 100, bStream);
//                byte[] byteArray = bStream.toByteArray();
                intent.putExtra(ImageEditor.EXTRA_IS_PAINT_MODE, enabledEditorPaint);
//                intent.putExtra(ImageEditor.EXTRA_IMAGE_PATH, byteArray);
                intent.putExtra(ImageEditor.EXTRA_IMAGE_PATH,b);
                intent.putExtra(ImageEditor.EXTRA_NAME, "FESTIVAL");
                intent.putExtra(ImageEditor.EXTRA_IS_TEXT_MODE, enabledEditorText);
                intent.putExtra(ImageEditor.EXTRA_IS_CROP_MODE, enableEditorCrop);
                intent.putExtra(ImageEditor.EXTRA_HAS_FILTERS, enableFilters);
                intent.putExtra(ImageEditor.EXTRA_HAS_POSTID, postId);
                intent.putExtra(ImageEditor.EXTRA_HAS_TEMPLATEID, templateId);
                context.startActivityForResult(intent, RC_IMAGE_EDITOR);
            }
        }
    }
}
