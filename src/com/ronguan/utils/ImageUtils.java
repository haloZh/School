package com.ronguan.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;

public class ImageUtils {
    
    public static String     IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    
    public static String[]   maybePath  = { "/Pictures/Screenshots", "/Pictures/", "/Photo/Screenshots", "/Camera/", "/Photo/" };
    
    public static final long TIME       = 5 * 60 * 1000;
    
    /**
     * 获取用户手机中最新的3张照片
     * 
     * @return 最新的3第图片
     */
    public static List<String> getLatestImageFromPhone() {
    
        List<String> paths = new ArrayList<String>();
        for (int i = 0; i < maybePath.length; i++) {
            String path = IMAGE_PATH + maybePath[i];
            File file = new File(path);
            File[] files = file.listFiles();
            if (files == null)
                continue;
            for (File f : files) {
                if (f.isFile()) {
                    String fullName = f.getAbsoluteFile().getName().toLowerCase();
                    if (fullName.endsWith("jpg") || fullName.endsWith("png") || fullName.endsWith("jpeg") || fullName.endsWith("bmp")) {
                        long lastModifiedTime = f.lastModified();
                        long nowTime = System.currentTimeMillis();
                        // 如果此截图是10分钟之内的，则有可能是反馈图，记录其路径
                        if (nowTime - lastModifiedTime < TIME) {
                            paths.add(f.getAbsolutePath());
                            if (paths.size() >= 5) { // 最多找出3张
                                break;
                            }
                        }
                    }
                }
            }
            // 如果在某一文件夹下找到了图片，则中断查找
            if (paths.size() > 0) {
                break;
            }
        }
        
        return paths;
    }
    
    public static Bitmap getBitmapFromFile(String dst, int width, int height) {
    
        if (null != dst && !"".equals(dst)) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst, opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst, opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
    
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        
        return roundedSize;
    }
    
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
    
        double w = options.outWidth;
        double h = options.outHeight;
        
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
    
    /**
     * 重新剪裁图片 根据比例尺设定，圆角
     * 
     * @param bitmap
     * @param newSize
     *            新的尺寸
     * @return
     */
    public static Bitmap resizeImage(Context context,Bitmap bitmap, int newSize) {
    
        // load the origial Bitmap
        
        Bitmap BitmapOrg = bitmap;
        
        int width = BitmapOrg.getWidth();
        
        int height = BitmapOrg.getHeight();
        
        float scale = 0;
        
        int newWidth = 0;
        
        int newHeight = 0;
        
        if (width >= height) {
            newWidth = DensityUtil.dip2px(context,newSize);
            scale = ((float) newWidth) / width;
            newHeight = (int) (height * scale);
        } else {
            newHeight = DensityUtil.dip2px(context,newSize);
            scale = ((float) newHeight) / height;
            newWidth = (int) (width * scale);
        }
        
        // calculate the scale
        
        float scaleWidth = ((float) newWidth) / width;
        
        float scaleHeight = ((float) newHeight) / height;
        
        // create a matrix for the manipulation
        
        Matrix matrix = new Matrix();
        
        // resize the Bitmap
        
        matrix.postScale(scaleWidth, scaleHeight);
        
        // if you want to rotate the Bitmap
        
        // matrix.postRotate(45);
        
        // recreate the new Bitmap
        
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
        
        height, matrix, true);
        
        // make a Drawable from Bitmap to allow to set the Bitmap
        
        // to the ImageView, ImageButton or what ever
        
        resizedBitmap = toRoundCorner(resizedBitmap, 10);
        
        return resizedBitmap;
        
    }
    
    /**
     * 设置圆角
     * 
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
    
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        
        return output;
    }
}
