package aiwac.admin.com.healthrobot.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**     将网络传输的二进制流转换成bitmap图片, 反转等
 * Created by luwang on 2017/11/2.
 */

public class ImageUtil {

    //将字节数组转换成Bitmap图片
    public static Bitmap getBitmap(byte[] bytes){
        try{
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(e.getMessage());
            return null;
        }

    }


    //将Base64编码的字符串转换成Bitmap图片
    public static Bitmap getBitmap(String base64Str){
        try{
            byte[] picByte = CodeUtil.decode(base64Str);
            return BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(e.getMessage());
            return null;
        }

    }

    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }


    //将Bitmap图片，转换成Base64编码
    public static String getBase64Str(Bitmap bitmap){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            /*Hint to the compressor, 0-100. 0 meaning compress for
            *                 small size, 100 meaning compress for max quality. Some
            *                 formats, like PNG which is lossless, will ignore the
            *                 quality setting
            */
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] bytes = baos.toByteArray();
            baos.close();
            return CodeUtil.encode(bytes);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(e.getMessage());
            return null;
        }

    }

    /**
     * 将图片按照某个角度进行旋转并返回旋转后的图片
     * @param bitmap 旋转前的图像
     * @param degree 旋转的角度
     * @return 如果出现异常，返回null
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**保存Bitmap到sdcard
     * @param b
     */
    public static void saveBitmap(Bitmap b){

        long dataTake = System.currentTimeMillis();
        String path = Environment.getExternalStorageDirectory()+ "/"+dataTake+".jpg";

        LogUtil.i("saveBitmap:jpegName = " + path);
        try {
            FileOutputStream fout = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            LogUtil.i("saveBitmap成功");
        } catch (IOException e) {
            LogUtil.i("saveBitmap:失败");
            e.printStackTrace();
        }

    }


    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
                                     int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }

    public static Bitmap cropBitmap(Bitmap bitmap, Rect rect) {
        int w = rect.right - rect.left;
        int h = rect.bottom - rect.top;
        Bitmap ret = Bitmap.createBitmap(w, h, bitmap.getConfig());
        Canvas canvas = new Canvas(ret);
        canvas.drawBitmap(bitmap, -rect.left, -rect.top, null);
        bitmap.recycle();
        return ret;
    }


}
