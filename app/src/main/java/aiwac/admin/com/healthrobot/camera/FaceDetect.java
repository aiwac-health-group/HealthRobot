package aiwac.admin.com.healthrobot.camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 实现FaceDetectionListener接口
 */
public class FaceDetect implements Camera.FaceDetectionListener {
    private Context mContext;
    private Handler mHander;

    private static int UPDATE_FACE_RECT=0;

    private static String LOG_TAG = "FaceDetect";
    public FaceDetect(Context c, Handler handler){
        mContext = c;
        mHander = handler;
    }
    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        // TODO Auto-generated method stub
        Log.i(LOG_TAG, "onFaceDetection...");
        if(faces != null){
            Message m = mHander.obtainMessage();
            m.what = UPDATE_FACE_RECT;
            m.obj = faces;
            m.sendToTarget();
        }
    }

}
