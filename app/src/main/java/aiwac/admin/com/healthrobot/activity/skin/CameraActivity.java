package aiwac.admin.com.healthrobot.activity.skin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.camera.CameraSize;
import aiwac.admin.com.healthrobot.camera.FaceDetect;
import aiwac.admin.com.healthrobot.camera.FaceView;
import aiwac.admin.com.healthrobot.HealthRobotApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//import static android.aiwac.com.common.constant.CommonConstant.CAMERA_HAS_STARTED_PREVIEW;
//import static android.aiwac.com.common.constant.CommonConstant.UPDATE_FACE_RECT;

public class CameraActivity extends FragmentActivity implements CameraHintDialogFragment.Callback{
    private static final String TAG = "CameraActivity";
    //-------------------------------------------------------
    //触觉感知
    //人脸采集
    //public final static int TOUCH_SOCKET_PORT = 10222;
    public static final int UPDATE_FACE_RECT = 0;
    public static final int CAMERA_HAS_STARTED_PREVIEW = 1;
    //-------------------------------------------------------
    private SurfaceView mSurfaceView;
    private SwitchCompat mSwitch;
    private TextView mTextView;
    private LinearLayout mTest;
    private ImageButton mCameraBtn;
    private FrameLayout mFrame;

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    public int mBackCameraId;//后置摄像头
    public Camera.CameraInfo mBackCameraInfo;
    public int mFrontCameraId;//前置摄像头
    public Camera.CameraInfo mFrontCameraInfo;
    private int mRotation;//预览旋转角度
    private FaceDetect faceDetect;
    private FaceView mFaceView;
    private mHandler mHandler;
    //Camera.Parameters parameters;
    Camera.Size bestPreviewSizeValue;
    boolean hasFace=false;
    boolean isCapture=false;
    int mCameraSize;//相机尺寸

    private static final int TIME_TO_CAPTURE=2;
    private String LOG_TAG="CameraActivity";

    @SuppressLint("HandlerLeak")
    private class mHandler extends Handler{
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_FACE_RECT:
                    Camera.Face[] faces=(Camera.Face[])msg.obj;
                    mFaceView.setFaces(faces);
                    hasFace=true;//检测出人脸
                    isCapture=false;
                    float faceSize[]=new float[faces.length];
                    for(int i=0;i<faces.length;i++){
                        RectF rect = new RectF();
                        rect.set(faces[i].rect);
                        faceSize[i]=Math.abs((rect.right-rect.left)*(rect.top-rect.bottom));
                        Log.d(LOG_TAG, "faceSize:"+faceSize[i]);
                        if(faceSize[i]>=mCameraSize*0.55f) {
                            isCapture = true;//大小合适，可以拍照
                            break;
                        }
                    }
                    break;
                case CAMERA_HAS_STARTED_PREVIEW:
                    Log.d(LOG_TAG, "CAMERA_HAS_STARTED_PREVIEW ");
                    startGoogleFaceDetect();
                    break;
                case TIME_TO_CAPTURE:
                    if(!hasFace){
                        //Log.d(LOG_TAG, "handleMessage: "+HealthRobotApplication.getContext());
                        Toast.makeText(CameraActivity.this,"未检测出人脸",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(isCapture){
                        Toast.makeText(HealthRobotApplication.getContext(),"距离合适，开始拍照",Toast.LENGTH_SHORT).show();
                        mHandler.removeCallbacks(task);//停止循环
                        //stopGoogleFaceDetect();//停止人脸检测
                        capture();
                    }else{
                        //Toast.makeText(HealthRobotApplication.getContext(),"请离摄像头近一点",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    //循环尝试拍照
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(TIME_TO_CAPTURE);
            mHandler.postDelayed(this, 5 * 1000);//每五秒尝试拍一次
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSurfaceView=findViewById(R.id.camera_surface);
        mSwitch=findViewById(R.id.myswitch);
        mTextView=findViewById(R.id.switchtv);
        mTest=findViewById(R.id.test);
        mCameraBtn = findViewById(R.id.cameraButton);
        mFaceView=findViewById(R.id.camera_faceview);
        mFrame=findViewById(R.id.camera_frame);
        mCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capture();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {

        //帮助按钮
        mTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CameraHintDialogFragment cameraHintDialogFragment = new  CameraHintDialogFragment();
                //cameraHintDialogFragment.show(getSupportFragmentManager(),"CameraHintDialogFragment");
                Intent intent=new Intent(CameraActivity.this,TestHelpActivity.class);
                startActivity(intent);
            }
        });
        //人声引导开关
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mTextView.setText("人声引导：开");
                    //打开人声引导
                }else{
                    mTextView.setText("人声引导：关");
                    //关闭人声引导
                }
            }
        });
        initCamera();//初始化摄像头
        initSurfaceSize(this.getResources().getConfiguration().orientation);//初始化surfaceview尺寸
        //准备预览环境
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);
        // mSurfaceView添加回调
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void surfaceCreated(SurfaceHolder holder) { //SurfaceView创建
                //initCamera();// 初始化Camera
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d(LOG_TAG, "surfaceChanged ");
                if(mCamera!=null){
                    mCamera.stopPreview();//先关闭预览
                    if(mSurfaceHolder!=null){
                        takePreview();//开启预览
                        // 实现自动对焦
                        mCamera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                if (success) {
                                    camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦
                                    doAutoFocus();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { //SurfaceView销毁
                Log.d(LOG_TAG, "surfaceDestroyed");
                // 释放Camera资源
                releaseCamera();
            }
        });
        //点击对焦
        mSurfaceView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(mCamera!=null){
                    mCamera.autoFocus(null);
                }
            }
        });
        mHandler=new mHandler();
        faceDetect=new FaceDetect(HealthRobotApplication.getContext(),mHandler);//人脸检测
        mHandler.sendEmptyMessageDelayed(CAMERA_HAS_STARTED_PREVIEW, 1500);//发送预览开启信号
        mHandler.postDelayed(task,1500);

    }

    private void initSurfaceSize(int orientation){
        Camera.Parameters parameters=mCamera.getParameters();
        RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) mFrame.getLayoutParams();
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            int targetHeight=CameraActivity.this.getWindowManager().getDefaultDisplay().getHeight();
            Log.d(LOG_TAG, "相机设备高度 "+targetHeight);
            bestPreviewSizeValue = CameraSize.getInstance().getBestPreviewSizebyHeight(parameters.getSupportedPreviewSizes(),targetHeight);
            float rate=(float)bestPreviewSizeValue.width/(float)bestPreviewSizeValue.height;
            if(rate>0){
                //宽高比
                params.height=targetHeight;
                params.width=(int)(targetHeight*(float)(bestPreviewSizeValue.width/bestPreviewSizeValue.height));
            }else{
                //高宽比
                params.height=targetHeight;
                params.width=(int)(targetHeight*(float)(bestPreviewSizeValue.height/bestPreviewSizeValue.width));
            }

        }else{
            //竖屏
            int targetWidth=CameraActivity.this.getWindowManager().getDefaultDisplay().getWidth();
            Log.d(LOG_TAG, "相机设备宽度 "+targetWidth);
            bestPreviewSizeValue=CameraSize.getInstance().getBestPreviewSizebyWidth(parameters.getSupportedPreviewSizes(),targetWidth);
            float rate=(float)bestPreviewSizeValue.width/(float)bestPreviewSizeValue.height;
            if(rate>0){
                //宽高比
                params.width=targetWidth;
                params.height=(int)(targetWidth*((float)bestPreviewSizeValue.width/(float)bestPreviewSizeValue.height));
            }else{
                //高宽比
                params.width=targetWidth;
                params.height=(int)(targetWidth*((float)bestPreviewSizeValue.height/(float)bestPreviewSizeValue.width));
            }
        }
        Log.d(LOG_TAG, "bestPreviewSizeValue.width="+bestPreviewSizeValue.width+"bestPreviewSizeValue.height="+bestPreviewSizeValue.height);
        Log.d(LOG_TAG, "params.width="+params.width+"params.height"+params.height);
        mFrame.setLayoutParams(params);
        mCameraSize=params.height*params.width;
    }

    //初始化摄像头
    private void initCamera(){
        try {
            //判断是否有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //初始化摄像头信息
            initCameraInfo();
            //打开摄像头
                if(mBackCameraInfo!=null){
                    mCamera=Camera.open(mBackCameraId);//优先开启后置摄像头
                    Log.d(LOG_TAG, "开启后置摄像头 "+"cameraid:"+mBackCameraId);
                }else if(mFrontCameraInfo!=null){
                    mCamera= Camera.open(mFrontCameraId);//没有后置，就尝试开启前置摄像头
                    Log.d(LOG_TAG, "开启前置摄像头 "+"cameraid:"+mFrontCameraId);
                }else{
                    throw new RuntimeException("没有任何相机可以开启");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //初始化摄像头信息
    private void initCameraInfo() {
        int numberOfCameras = Camera.getNumberOfCameras();// 获取摄像头个数
        for (int cameraId = 0; cameraId < numberOfCameras; cameraId++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 后置摄像头信息
                mBackCameraId = cameraId;
                mBackCameraInfo = cameraInfo;
            } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                // 前置摄像头信息
                mFrontCameraId = cameraId;
                mFrontCameraInfo = cameraInfo;
            }
        }
    }


    /**
     * 设置预览
     */
    private void takePreview(){
        try{
            mCamera.setPreviewDisplay(mSurfaceHolder);//绑定Camera和SurfaceHolder
            //设置最适合的预览尺寸
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(bestPreviewSizeValue.width,bestPreviewSizeValue.height);
            mCamera.setParameters(parameters);
            //预览方向矫正
            if(mBackCameraInfo!=null) setCameraDisplayOrientation(mBackCameraId);
            else if(mFrontCameraInfo!=null) setCameraDisplayOrientation(mFrontCameraId);
            mCamera.startPreview();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //预览方向矫正
    private void setCameraDisplayOrientation(int cameraId){
        Camera.CameraInfo info=new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,info);
        int rotation=this.getWindowManager().getDefaultDisplay().getRotation();//获取屏幕旋转角度(横屏or竖屏)
        int degrees=0;
        switch (rotation){
            case Surface.ROTATION_0:degrees=0;break;
            case Surface.ROTATION_90:degrees=90;break;
            case Surface.ROTATION_180:degrees=180;break;
            case Surface.ROTATION_270:degrees=270;break;
        }
        Log.d(LOG_TAG, "屏幕旋转角度:"+degrees);
        Log.d(LOG_TAG, "摄像头预设角度:"+info.orientation);
        int result;
        if(info.facing==Camera.CameraInfo.CAMERA_FACING_FRONT){
            result=(info.orientation+degrees)%360;
            result=(360-result)%360;//前置摄像头需要镜像处理
        }else{
            result=Math.abs((info.orientation-degrees)%360);
        }
        mRotation=result;
        Log.d(LOG_TAG, "预览图像矫正旋转角度:"+result);
        mCamera.setDisplayOrientation(result);

    }

    /**
     * 拍照
     */
   private void capture(){
//       mCamera.autoFocus(null);//对焦
         mCamera.takePicture(null,null,mPictureCallBack);
   }
    //拍照回调
    private Camera.PictureCallback mPictureCallBack=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            mCamera.stopPreview();
            //存储照片
            // 获取 SD 卡根目录
            String saveDir = Environment.getExternalStorageDirectory() + "/pictures";
            // 新建目录
            File dir = new File(saveDir);
            if (! dir.exists())
                dir.mkdir();
            String mFilePath=saveDir+"/"+System.currentTimeMillis()+".jpg";
            File tempFile=new File(mFilePath);
            try{
                if(tempFile!=null){
                    FileOutputStream fos=new FileOutputStream(tempFile);
                    fos.write(bytes);
                    fos.close();
                    Intent intent = new Intent();
                    intent.putExtra("photoPath", tempFile.getAbsolutePath());
                    intent.putExtra("photoRoi",mRotation);
                    intent.setClass(CameraActivity.this, DetectProcessActivity.class);
                    startActivity(intent);
                }
            }catch (FileNotFoundException e){
                Log.e(LOG_TAG, "onPictureTaken: " + e.getMessage());
            }catch (IOException e) {
                Log.e(LOG_TAG, "onPictureTaken: " + e.getMessage() );
            }
        }
    };


   //设置自动对焦
    private void doAutoFocus() {
        Camera.Parameters parameters = mCamera.getParameters();
              parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
              mCamera.setParameters(parameters);
              mCamera.autoFocus(new Camera.AutoFocusCallback() {
                     @Override
                     public void onAutoFocus(boolean success, Camera camera) {
                          if (success) {
                                   camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
                                   if (!Build.MODEL.equals("KORIDY H30")) {
                                          Camera.Parameters parameters = camera.getParameters();
                                          parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
                                          camera.setParameters(parameters);
                                   }else{
                                          Camera.Parameters parameters = camera.getParameters();
                                          parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                                          camera.setParameters(parameters);
                                   }
                          }
                     }
              });
    }


   //开启人脸检测
    private void startGoogleFaceDetect(){
        Camera.Parameters params = mCamera.getParameters();
        if(params.getMaxNumDetectedFaces() > 0){
            if(mFaceView != null){
                mFaceView.clearFaces();
                mFaceView.setRotation(mRotation);
                mFaceView.setVisibility(View.VISIBLE);
            }
            mCamera.setFaceDetectionListener(faceDetect);
            mCamera.startFaceDetection();
        }
    }
    //关闭人脸检测
    private void stopGoogleFaceDetect(){
        Camera.Parameters params = mCamera.getParameters();
        if(params.getMaxNumDetectedFaces() > 0){
            mCamera.setFaceDetectionListener(null);
            mCamera.stopFaceDetection();
            mFaceView.clearFaces();
        }
    }

    //释放相机资源
    private void releaseCamera(){
        if(mCamera != null){
            stopGoogleFaceDetect();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(task);
        releaseCamera();
    }
    //dialog回调
    @Override
    public void onClick(int which) {
        switch (which){
            case 1:
                //重新拍照
                // retakePreview();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //finish();
    }

    /**
     * 初始化Camera2
     */
   /* private void initCamera2() {
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        Handler mainHandler = new Handler(getMainLooper());

        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 10);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { //可以在这里处理拍照得到的临时照片
            @Override
            public void onImageAvailable(ImageReader reader) {
                // 获取拍照照片数据
                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);//由缓冲区存入字节数组
                Bitmap srcBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //创建图像对象
                //FaceImage faceImage = new FaceImage();
              //  faceImage.setBitmap(bitmap);
                //ArrayList<FaceImage> faceImages = new ArrayList<FaceImage>();
                //faceImages.add(faceImage);
                //business.setFaceImages(faceImages);

                Intent intent = new Intent();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                intent.putExtra("miniPhoto", baos.toByteArray());
                intent.setClass(CameraActivity.this, DetectProcessActivity.class);
                startActivity(intent);

            }
        }, mainHandler);
        //获取摄像头管理
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //打开摄像头
            mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;//后置摄像头
            mCameraManager.openCamera(mCameraID, stateCallback, mainHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 摄像头创建监听,获得摄像头状态stateCallback
     */
    /*private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {//打开摄像头
            mCameraDevice = camera;
            //开启预览
            takePreview();
            Log.d(LOG_TAG, "onOpened: 打开摄像头");
        }

        @Override
        public void onDisconnected(CameraDevice camera) {//关闭摄像头
            if (null != mCameraDevice) {
                mCameraDevice.close();
                CameraActivity.this.mCameraDevice = null;
                Log.d(LOG_TAG, "onDisconnected: 关闭摄像头");
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {//发生错误
            Toast.makeText(CameraActivity.this, "摄像头开启失败", Toast.LENGTH_SHORT).show();
        }
    };*/

    /**
     * 开始预览
     */
    /*private void takePreview() {
        try {
            // 创建预览需要的CaptureRequest.Builder
            previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 将SurfaceView的surface作为CaptureRequest.Builder的目标
            previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    if (null == mCameraDevice)
                        return;
                    // 当摄像头已经准备好时，开始显示预览
                    mCameraCaptureSession = cameraCaptureSession;
                    try {
                        // 自动对焦
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 打开闪光灯
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        // 显示预览
                        CaptureRequest previewRequest = previewRequestBuilder.build();
                        mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                        Log.d(LOG_TAG, "onConfigured: 开启预览");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CameraActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
                }
            }, childHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 拍照
     */
    /*
    private void takePicture() {
        if (mCameraDevice == null) return;
        // 创建拍照需要的CaptureRequest.Builder
        final CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // 获取手机方向
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            // 根据设备方向计算设置照片的方向
            CameraCharacteristics characteristics =  mCameraManager.getCameraCharacteristics(mCameraID);
            int mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);//相机传感器方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, mSensorOrientation);
            //拍照
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            mCameraCaptureSession.capture(mCaptureRequest, null, childHandler);
            Log.d(LOG_TAG, CommonConstant.DATA_CAMERA_PHOTO_SUCCESS);
        } catch (Exception e) {
            Log.d(LOG_TAG, CommonConstant.DATA_CAMERA_PHOTO_FAILURE);
            e.printStackTrace();
        }
    }

    private void retakePreview(){
        try {
            CaptureRequest previewRequest = previewRequestBuilder.build();
            mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
            Log.d(LOG_TAG, "retakePreview: 重新开启预览");
        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "retakePreview: 重新开启预览失败");
        }
    }*/



}
