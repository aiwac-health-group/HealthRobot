package aiwac.admin.com.healthrobot.SkinDetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import aiwac.admin.com.healthrobot.db.AiwacApplication;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

public class CommonAlexnetExtract {

    private static final int IMAGE_WIDTH = 227;
    private static final int IMAGE_HEIGHT = 227;

    static {
        //加载库文件
        System.loadLibrary("tensorflow_inference");
    }

    public static float[] extractFeature(String modelName, String filePath) throws Exception {
        byte[] graphDef = readAllBytesOrExit(modelName);
        Tensor img_tensor_byte = imageToTensor(filePath);
        float[] lp_byte = executeInceptionGraph(graphDef, img_tensor_byte);
        for (int i=0; i< lp_byte.length; i++) {
            Log.e("SKIN DETECTION", modelName + String.valueOf(lp_byte[i]));
        }

        return lp_byte;
    }

    public static float[] extractFeatureFromBitmap(String modelName, Bitmap bitmap) throws Exception {
        byte[] graphDef = readAllBytesOrExit(modelName);
        Tensor img_tensor_byte = bitmapToTensor(bitmap);
        float[] lp_byte = executeInceptionGraph(graphDef, img_tensor_byte);
        for (int i=0; i< lp_byte.length; i++) {
            Log.e("SKIN DETECTION", modelName + String.valueOf(lp_byte[i]));
        }

        return lp_byte;
    }

    private static float[] executeInceptionGraph(byte[] graphDef, Tensor img_tensor_byte) {
        float kpf = 1.0f;
        Tensor kp = Tensor.create(kpf);
        try (Graph g = new Graph()) {
            g.importGraphDef(graphDef);
            try (Session s = new Session(g);
                 Tensor result = s.runner().feed("Placeholder", img_tensor_byte).feed("Placeholder_2", kp).fetch("test/prob").run().get(0)){
                final long[] rshape = result.shape();
                int nlabels = (int) rshape[1];
                float[][] probs = new float[1][nlabels];
                FloatBuffer output = FloatBuffer.allocate(nlabels);
                result.writeTo(output);
                return output.array();
//                return result.copyTo(probs)[0];
            }catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Tensor bitmapToTensor(Bitmap bitmap) throws Exception {
        double[][][] result = getImagePixel(bitmap);
        float[] mean = {123.68f,116.779f,103.939f};
        long[] shape = {1, IMAGE_WIDTH, IMAGE_HEIGHT, 3};
        FloatBuffer face = FloatBuffer.allocate(IMAGE_WIDTH * IMAGE_HEIGHT *3);
        int count = 0;
        for (int i = 0; i < IMAGE_WIDTH; i++) {
            for (int j = 0; j < IMAGE_HEIGHT; j++) {
                for (int k = 0; k < 3; k++) {
                    face.put(count, (float) result[i][j][k] - mean[k]);
                    count++;
                }
            }
        }
        Tensor img_tensor_byte = Tensor.create(shape, face);
        return img_tensor_byte;
    }

    public static Tensor imageToTensor(String original_jpg) throws Exception {
        double[][][] result = getImagePixel(original_jpg);
        float[] mean = {123.68f,116.779f,103.939f};
        long[] shape = {1, IMAGE_WIDTH, IMAGE_HEIGHT, 3};
        FloatBuffer face = FloatBuffer.allocate(IMAGE_WIDTH * IMAGE_HEIGHT *3);
        int count = 0;
        for (int i = 0; i < IMAGE_WIDTH; i++) {
            for (int j = 0; j < IMAGE_HEIGHT; j++) {
                for (int k = 0; k < 3; k++) {
                    face.put(count, (float) result[i][j][k] - mean[k]);
                    count++;
                }
            }
        }
        Tensor img_tensor_byte = Tensor.create(shape, face);
        return img_tensor_byte;
    }


    public static double[][][] getImagePixel(String image) throws IOException {
        File picture = new File(Environment.getExternalStorageDirectory(), image);
        Uri filepath = Uri.fromFile(picture);

        Bitmap bitmap = BitmapFactory.decodeFile(filepath.getPath());
        bitmap = getScaleBitmap(bitmap, IMAGE_HEIGHT);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        double[][][] imageValue = new double[IMAGE_WIDTH][IMAGE_HEIGHT][3];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = bitmap.getPixel(i, j); // 下面三行代码将一个数字转换为RGB数字
                imageValue[j][i][0] = (pixel & 0xff0000) >> 16;
                imageValue[j][i][1] = (pixel & 0xff00) >> 8;
                imageValue[j][i][2] = (pixel & 0xff);
            }
        }
        return imageValue;
    }

    public static double[][][] getImagePixel(Bitmap bitmap) throws IOException {

        bitmap = getScaleBitmap(bitmap, IMAGE_HEIGHT);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        double[][][] imageValue = new double[IMAGE_WIDTH][IMAGE_HEIGHT][3];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = bitmap.getPixel(i, j); // 下面三行代码将一个数字转换为RGB数字
                imageValue[j][i][0] = (pixel & 0xff0000) >> 16;
                imageValue[j][i][1] = (pixel & 0xff00) >> 8;
                imageValue[j][i][2] = (pixel & 0xff);
            }
        }
        return imageValue;
    }

    // 对图片进行缩放
    private static Bitmap getScaleBitmap(Bitmap bitmap, int size) throws IOException {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) size) / width;
        float scaleHeight = ((float) size) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    static byte[] readAllBytesOrExit(String path) {
        try {
            return InputStream2ByteArray(path);
        } catch (IOException e) {
            Log.e("Failed to read [" + path + "]: " , e.getMessage());
        }
        return null;
    }


    public static byte[] InputStream2ByteArray(String fileName) throws IOException {

        InputStream in = AiwacApplication.getContext().getAssets().open(fileName);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    public static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
}

