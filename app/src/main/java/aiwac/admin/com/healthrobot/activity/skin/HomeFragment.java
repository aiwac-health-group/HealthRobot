package aiwac.admin.com.healthrobot.activity.skin;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher.ViewFactory;

import org.greenrobot.eventbus.EventBus;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.MessageEvent;
import zuo.biao.library.base.BaseFragment;

public class HomeFragment extends BaseFragment implements ViewFactory{

    private static final int UPDATE_LISTVIEW=0;
    private int pageNo=0;
    private ImageSwitcher mImageSwitcher;
    private int[] imgIds;
    private int curPos;
    private LinearLayout linearLayout;
    private ImageView[] tips;
    private ImageButton btn_back;

    private LinearLayout testBtn;

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    public interface Callback{
        void onClick(int which);
    }

    private HomeFragment.Callback callback;

    private String LOG_TAG="HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);
        setEvent(view);


        //setHeight(listview, contentAdapter);

        return view;
    }
    //callBack，在SkinMainActivity 中实现，用以跳转到照相界面CameraActivity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.Callback){
            callback = (HomeFragment.Callback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement Callback");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
    private void initView(View view){

        imgIds = new int[]{R.drawable.fig1, R.drawable.fig2, R.drawable.fig3};
        mImageSwitcher = view.findViewById(R.id.imageSwitcher1);
        mImageSwitcher.setFactory(this);
        linearLayout = view.findViewById(R.id.viewGroup);

        //设置banner
        setImageSwitcher();

    }


    /**
     * 按下按钮，开始测肤
     * @param view
     */
    private void setEvent(View view){


        testBtn = view.findViewById(R.id.testbtn_home);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ViewDialogFragment viewDialogFragment = new ViewDialogFragment();
//                viewDialogFragment.show(getFragmentManager());
                if (callback != null){
                    callback.onClick(1);
                }



            }
        });
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {

        }

        //返回键按钮
        btn_back=view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageEvent messageEvent = new MessageEvent("SkinMainActivity", "back");
                EventBus.getDefault().post(messageEvent);
            }
        });
    }
    private void setImageSwitcher(){
        // 图片 index 布局的生成
        tips = new ImageView[imgIds.length];
        for (int i=0; i<imgIds.length; i++){
            ImageView mImageView = new ImageView(this.getContext());
            tips[i] = mImageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = 3;
            layoutParams.leftMargin = 3;
            layoutParams.height = 30;
            layoutParams.width = 30;
            mImageView.setBackgroundResource(R.drawable.dot_unfocus);
            linearLayout.addView(mImageView, layoutParams);
        }

        // 设置动画
        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this.getContext(), android.R.anim.slide_in_left));
        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this.getContext(), android.R.anim.slide_out_right));
        curPos=0;
        mImageSwitcher.setImageResource(imgIds[curPos]);
        setImageBackground(curPos);
        // 按时切换图片
        mImageSwitcher.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(curPos==imgIds.length-1){
                    curPos=0;
                }else{
                    curPos++;
                }
                mImageSwitcher.setImageResource(imgIds[curPos]);
                setImageBackground(curPos);
                mImageSwitcher.postDelayed(this, 1500);
            }
        }, 1500);

    }
    private void setImageBackground(int selectItems) {
        for (int i=0; i<tips.length; i++){
            if(i==selectItems){
                tips[i].setBackgroundResource(R.drawable.dot_focus);
            }else{
                tips[i].setBackgroundResource(R.drawable.dot_unfocus);
            }
        }
    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(this.getContext());
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        return i;
    }

}
