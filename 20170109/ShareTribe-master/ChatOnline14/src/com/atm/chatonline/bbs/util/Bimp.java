package com.atm.chatonline.bbs.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class Bimp {
	public static int max = 0;
	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();	
	
	//
	public static List<String> drr = new ArrayList<String>();
	

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = 2;//(int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
	public static Bitmap resizePhoto(Bitmap photo){
		 //获取这个图片的宽和高
       int width = photo.getWidth();
       int height = photo.getHeight();

       //定义预转换成的图片的宽度和高度
       int newWidth = 80;
       int newHeight = 80;

       //计算缩放率，新尺寸除原始尺寸
       float scaleWidth = ((float) newWidth) / width;
       float scaleHeight = ((float) newHeight) / height;

       // 创建操作图片用的matrix对象
       Matrix matrix = new Matrix();

       // 缩放图片动作
       matrix.postScale(scaleWidth, scaleHeight);
       Bitmap resizedBitmap = Bitmap.createBitmap(photo, 0, 0,
               width, height, matrix, true);
		return resizedBitmap;
		
	}
}
