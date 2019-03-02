package aiwac.admin.com.healthrobot.utils;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import aiwac.admin.com.healthrobot.HealthRobotApplication;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.exception.FileException;

/** 用于文件操作
 *
 */

public class FileUtil {

    private static Context context = HealthRobotApplication.getContext();

    public static File getDir(String folderName) {
        return Environment.getExternalStoragePublicDirectory(folderName);
        //return new File(sdDir, "ServiceCamera");
    }

    public static File getAndCreateDir(String rootFolder, String folderName){
        File file = new File(getDir(rootFolder), folderName);
        //创建目录，保存图片
        if (!file.exists() && !file.mkdirs()) {
            LogUtil.d( "Can't create directory");
            return null;
        }
        return file;
    }

    public static File createFile(String rootFolder, String folderName, String filename){
        File file = getAndCreateDir(rootFolder, folderName);
        if(file == null) {
            return null;
        }

        return new File(file.getPath() + File.separator + filename);
    }


    //保存字节数组到某个文件
    public static void saveFile(File file, byte[] data){

        if(file == null || data == null || data.length <= 0){
            LogUtil.d( "input data is invalidate.");
            return;
        }

        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + PHOTO_NAME;

        File pictureFile = new File(filename);
        */
        LogUtil.d("Filename is "+ file.getAbsolutePath());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            LogUtil.d( "data saved : " + file.getName());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("FileUtil saveFile exception.");
        }finally {
            close(fos);
        }

    }


    public static void close(Closeable stream){
        try{
            if(stream != null){
                if(stream instanceof FileInputStream){
                    FileInputStream fis = (FileInputStream) stream;
                    fis.close();
                }else if(stream instanceof FileOutputStream){
                    FileOutputStream fos = (FileOutputStream) stream;
                    fos.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static byte[] getFileBytes(String rootFolder, String foldername, String filename){
            filename = Environment.getExternalStorageDirectory().getPath() +File.separator+ rootFolder +File.separator +foldername+File.separator +filename;
            FileInputStream fis = null;
            ByteArrayOutputStream baos = null;

            try {
                fis = new FileInputStream(filename);
                baos = new ByteArrayOutputStream();

                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = fis.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }

                byte[] fileBytes = baos.toByteArray();

                return fileBytes;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d( Constant.FILE_OPERATOR_EXCEPTION);
                throw new FileException(Constant.FILE_OPERATOR_EXCEPTION, e);
            } finally {
                CacheFileUtil.close(fis);
                CacheFileUtil.close(baos);
            }

    }


    // 创建一个临时目录，用于复制临时文件，如assets目录下的离线资源文件
    public static String createTmpDir() {
        String sampleDir = "baiduTTS";
        String tmpDir = Environment.getExternalStorageDirectory().toString() + "/" + sampleDir;
        if (!FileUtil.makeDir(tmpDir)) {
            tmpDir = context.getExternalFilesDir(sampleDir).getAbsolutePath();
            if (!FileUtil.makeDir(sampleDir)) {
                throw new RuntimeException("create model resources dir failed :" + tmpDir);
            }
        }
        return tmpDir;
    }

    public static boolean fileCanRead(String filename) {
        File f = new File(filename);
        return f.canRead();
    }

    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    public static void copyFromAssets(AssetManager assets, String source, String dest, boolean isCover)
            throws IOException {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = assets.open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                    }
                }
            }
        }
    }



}
