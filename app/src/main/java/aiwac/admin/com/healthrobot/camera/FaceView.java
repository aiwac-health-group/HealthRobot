package aiwac.admin.com.healthrobot.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;

import aiwac.admin.com.healthrobot.R;
import com.baidu.aip.util.Util;

/**
 * 将检测到的人脸 Face[] 的 rect 取出
 */
public class FaceView extends AppCompatImageView {
    private static final String TAG = "FaceView";
    private Context mContext;
    private Paint mLinePaint;
    private Camera.Face[] mFaces;
    private Matrix mMatrix = new Matrix();
    private RectF mRect = new RectF();
    private Drawable mFaceIndicator ;
    private int mRotation;

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initPaint();
        mContext = context;
        mFaceIndicator = getResources().getDrawable(R.drawable.ic_face_find_2);
    }


    public void setFaces(Camera.Face[] faces) {
        this.mFaces = faces;
        invalidate();
    }

    public void clearFaces() {
        mFaces = null;
        invalidate();
    }
    public void setRotation(int rotation){
        mRotation=rotation;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        if (mFaces == null || mFaces.length < 1) {
            return;
        }
        Log.d(TAG, "Face count:"+mFaces.length);
        boolean isMirror = false;
        int Id = 0;
        if (Id == Camera.CameraInfo.CAMERA_FACING_BACK) {
            isMirror = false; //后置Camera无需mirror
        } else if (Id == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            isMirror = true;  //前置Camera需要mirror
        }
        prepareMatrix(mMatrix, isMirror, mRotation, getWidth(), getHeight());
        canvas.save();
        mMatrix.postRotate(0); //Matrix.postRotate默认是顺时针
        canvas.rotate(-0);   //Canvas.rotate()默认是逆时针
        for (int i = 0; i < mFaces.length; i++) {
            mRect.set(mFaces[i].rect);
            mMatrix.mapRect(mRect);
            mFaceIndicator.setBounds(Math.round(mRect.left), Math.round(mRect.top),
                    Math.round(mRect.right), Math.round(mRect.bottom));
            Log.d(TAG, "mRect.left"+mRect.left+"mRect.top"+mRect.top+"mRect.right"+mRect.right+"mRect.bottom"+mRect.bottom);
            mFaceIndicator.draw(canvas);
//			canvas.drawRect(mRect, mLinePaint);
        }
        canvas.restore();
        super.onDraw(canvas);
    }

    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		int color = Color.rgb(0, 150, 255);
        int color = Color.rgb(98, 212, 68);
//		mLinePaint.setColor(Color.RED);
        mLinePaint.setColor(color);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2f);
        mLinePaint.setAlpha(180);
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
}
