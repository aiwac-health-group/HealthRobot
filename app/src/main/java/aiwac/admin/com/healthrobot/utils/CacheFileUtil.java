package aiwac.admin.com.healthrobot.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import aiwac.admin.com.healthrobot.common.Constant;

/**     文件存储读取操作
 * Created by luwang on 2017/11/3.
 */

public class CacheFileUtil {

    //   /storage/emulated/0/Android/data/android.wang.com.androidtest/cache
    private String cachePath;


    public CacheFileUtil(Context context){
        cachePath = context.getExternalCacheDir().getPath() + File.separator;
        LogUtil.d( "CacheFileUtil : " + cachePath);
    }

    public boolean createFolder(String foldername){
        String path = cachePath + foldername;
        File folder = new File(path);
        if(!isExist(folder)){
            if(folder.mkdirs()){
                LogUtil.d( Constant.FILE_CREATE_FOLD_SUCCESS + path);
                return true;
            }else {
                LogUtil.d(Constant.FILE_CREATE_FOLD_EXCEPTION + path);
                return false;
            }

        }else{
            LogUtil.d(Constant.FILE_FOLD_EXIST + path);
            return true;
        }
    }

    public boolean isExist(File file){
        return file.exists();
    }

    public boolean isExist(String path){
        File file=new File(path);
        return file.exists();
    }


    /**
     *
     * @param foldername  文件所在文件夹
     * @param filename 文件名
     * @return 文件创建成功或者存在 返回文件，否则返回null
     */
    public File createFile(String foldername, String filename){
        try{
            if(createFolder(foldername)) {
                filename = cachePath + foldername + File.separator + filename;
                File file = new File(filename);
                if (!file.exists()) {
                    if(file.createNewFile()) {
                        LogUtil.d(Constant.FILE_CREATE_FILE_SUCCESS + filename);
                        return file;
                    }else {
                        LogUtil.d(Constant.FILE_CREATE_FILE_EXCEPTION);
                        return null;
                    }

                }else{
                    LogUtil.d(Constant.FILE_FILE_EXIST + filename);
                    return file;
                }

            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.FILE_CREATE_FILE_EXCEPTION);
            return null;
        }
    }

    public boolean delete(String folderName,String fileName){
        File file=new File(cachePath+folderName+File.separator+fileName);
        if(file.delete()){
            LogUtil.d(Constant.FILE_DELETE_FILE_SUCCESS);
            return true;
        }else{
            LogUtil.d(Constant.FILE_DELETE_FILE_EXCEPTION);
            return  false;
        }

    }

    public String joinFile(String foldername, String filename){
        return cachePath + foldername + File.separator + filename;
    }

    public static String getFileSeparator(){
        return File.separator;
    }

    public String getCacheFold(){
        return cachePath;
    }

    public static void close(FileInputStream fis){
        if(fis != null){
            try{
                fis.close();
                LogUtil.d(Constant.FILE_CLOSE_FILEINPUTSTREAM_SUCCESS);
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.d(Constant.FILE_CLOSE_FILEINPUTSTREAM_EXCEPTION);
            }

        }
    }

    public static void close(ByteArrayOutputStream baos){
        if(baos != null){
            try{
                baos.close();
                LogUtil.d(Constant.FILE_CLOSE_FILEOUTPUTSTREAM_SUCCESS);
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.d(Constant.FILE_CLOSE_FILEOUTPUTSTREAM_EXCEPTION);
            }

        }
    }

    public static void close(FileOutputStream fos){
        if(fos != null){
            try{
                fos.close();
                LogUtil.d(Constant.FILE_CLOSE_FILEOUTPUTSTREAM_SUCCESS);
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.d(Constant.FILE_CLOSE_FILEOUTPUTSTREAM_EXCEPTION);
            }

        }
    }






}
