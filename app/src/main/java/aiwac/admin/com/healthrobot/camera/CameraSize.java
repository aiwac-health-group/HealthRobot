package aiwac.admin.com.healthrobot.camera;

import android.hardware.Camera;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraSize {
    private static CameraSize cameraSize=null;

    private static final String LOG_TAG="CameraSize";

    private Comparator<Camera.Size> comparatorbywidth=new Comparator<Camera.Size>() {
       @Override
       public int compare(Camera.Size s1, Camera.Size s2) {
            return s1.width-s2.width;//按camera支持的尺寸宽度升序排序
            //return s2.width-s1.width;//降序排列
        }
    };
    private Comparator<Camera.Size> comparatorbyheight=new Comparator<Camera.Size>() {
        @Override
        public int compare(Camera.Size s1, Camera.Size s2) {
            return s1.height-s2.height;//按camera支持的尺寸高度升序排序
            //return s2.height-s1.height;//降序排列
        }
    };

    //单例模式
    public static CameraSize getInstance(){
        if(cameraSize==null){
            cameraSize=new CameraSize();
            return cameraSize;
        }else return cameraSize;
    }
    public Camera.Size getBestPreviewSizebyWidth(List<Camera.Size> list,int th){
        Collections.sort(list,comparatorbywidth);//升序排序
        for(Camera.Size size:list){
            Log.d(LOG_TAG, "摄像头支持的尺寸："+size.width+"x"+size.height);
        }
        int i=0;
        for(Camera.Size nowsize:list){
            if(nowsize.width>th&&equalRate(nowsize,1.33f)){//保证宽高比接近4:3
                Log.d(LOG_TAG, "getBestPreviewSize:"+nowsize.width+"x"+nowsize.height+" (w x h)");
                break;
            }
            i++;
        }
        if(i==list.size()) i--;//如果没有合适的，则选择最接近尺寸
        return list.get(i);
    }
    public Camera.Size getBestPreviewSizebyHeight(List<Camera.Size> list,int th){
        Collections.sort(list,comparatorbyheight);//升序排序
        int i=0;
        for(Camera.Size nowsize:list){
            if(nowsize.height>th&&equalRate(nowsize,1.33f)){//保证宽高比接近4:3
                Log.d(LOG_TAG, "getBestPreviewSize:"+nowsize.width+"x"+nowsize.height+" (w x h)");
                break;
            }
            i++;
        }
        if(i==list.size()) i--;
        return list.get(i);
    }
    public Camera.Size getBestPictureSize(List<Camera.Size> list,int th){
        Collections.sort(list,comparatorbywidth);//升序排序
        int i=0;
        for(Camera.Size nowsize:list){
            if((nowsize.width)>th&&equalRate(nowsize,1.33f)){
                Log.d(LOG_TAG, "getBestPictureSize:"+nowsize.width+nowsize.height+" (w x h)");
                break;
            }
            i++;
        }
        return list.get(i);
    }
    //宽高比比较
    public boolean equalRate(Camera.Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.2)
        {
            return true;
        }
        else{
            return false;
        }
    }

}
