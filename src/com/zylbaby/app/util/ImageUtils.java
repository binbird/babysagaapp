package com.zylbaby.app.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

@SuppressLint("SdCardPath")
public class ImageUtils
{

    public static Bitmap getCircleBitmap(Bitmap bitmap)
    {
        return getCircleBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap getCircleBitmap(Bitmap bitmap, int width, int height)
    {
        Bitmap croppedBitmap = scaleCenterCrop(bitmap, width, height);
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        int radius = 0;
        if(width > height)
        {
            radius = height / 2;
        }
        else
        {
            radius = width / 2;
        }

        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(croppedBitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth)
    {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    @SuppressWarnings("deprecation")
	public static Drawable resizeImage2(String path,  
            int width,int height)   
    {  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;//不加载bitmap到内存中  
        BitmapFactory.decodeFile(path,options);   
        int outWidth = options.outWidth;  
        int outHeight = options.outHeight;  
        options.inDither = false;  
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;  
        options.inSampleSize = 1;  
          
        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0)   
        {  
            int sampleSize=(outWidth/width+outHeight/height)/2;  
            //Log.d(tag, "sampleSize = " + sampleSize);  
            options.inSampleSize = sampleSize;  
        }  
      
        options.inJustDecodeBounds = false;  
        return new BitmapDrawable(BitmapFactory.decodeFile(path, options));       
    }  
    
  //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
                 final int heightRatio = Math.round((float) height/ (float) reqHeight);
                 final int widthRatio = Math.round((float) width / (float) reqWidth);
                 inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
            return inSampleSize;
    }
    
 // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 240, 400);

            // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
        }
    
  //把bitmap转换成String
    public static String bitmapToString(String filePath) {

            Bitmap bm = getSmallBitmap(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
     }
    //保存图片
    public static void saveMyBitmap(String id, Bitmap mBitmap) {// 生成图片
        File f = new File("/sdcard/babysaga/newpic/" + id + ".jpg");
        File destDir = new File("/sdcard/babysaga/newpic/");
        if (!destDir.exists()) {
        	destDir.mkdirs();
        }
        
        try {
            f.createNewFile();

        } catch (IOException e) {
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}