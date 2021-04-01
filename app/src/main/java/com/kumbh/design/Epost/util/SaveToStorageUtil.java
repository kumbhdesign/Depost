package com.kumbh.design.Epost.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Utility class for saving images to external storage
 */
public class SaveToStorageUtil {

	public static String save(Bitmap bitmap, String imgName, Context context, String name){
		//insertImage(context.getContentResolver(),bitmap,"TestImage","TestDescription");
		
		String path = getFolderPath()+"/"+imgName;
		Log.i("SaveToStorage", "Path: "+path);
		try {
		       FileOutputStream out = new FileOutputStream(path);
		       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
		       e.printStackTrace();
		}
		return path;
	}

	private static String getFolderPath() {
		// check if exist on sdcard or create directory "/Pictures/REC"
		String lvl1 = "Depost";

		File folderLvl1 = checkPath(Environment.getExternalStorageDirectory(), lvl1);
		//File folderLvl2 = checkPath(folderLvl1, lvl2);
		
		return folderLvl1.getPath();
	}

	private static File checkPath(File pathLvl1, String lvl2) {
		File pathLvl2 = new File(pathLvl1, lvl2);
		if (!pathLvl2.exists()) {
			pathLvl2.mkdir();
		}
		return pathLvl2;
	}

	public static String save(Bitmap bitmap, Context context , String name){
		return save(bitmap, generateImageName(name), context,name);
	}
	
	public static String generateImageName(String name) {
//		return "IMG_"+ DateFormat.format("yyyyMMdd_kkmmss", new Date())+".jpg";
		return name;
	}
}
