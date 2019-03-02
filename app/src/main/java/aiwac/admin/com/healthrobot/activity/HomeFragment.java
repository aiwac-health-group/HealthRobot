package aiwac.admin.com.healthrobot.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher.ViewFactory;

import aiwac.admin.com.healthrobot.R;
//import aiwac.admin.com.healthrobot.adapter.ContentAdapter;
//import aiwac.admin.com.healthrobot.bean.ContentEntity;
//import aiwac.admin.com.healthrobot.HealthRobotApplication;
//import aiwac.admin.com.healthrobot.ui.LoadMoreScrollView;
//import aiwac.admin.com.healthrobot.utils.JsonUtil;
//import aiwac.admin.com.healthrobot.utils.ThreadPoolManager;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.zip.Inflater;
//
//import static aiwac.admin.com.healthrobot.activity.HTTPClientHelper.httpClientHelper;

public class HomeFragment extends Fragment implements ViewFactory{

    private static final int UPDATE_LISTVIEW=0;
    private int pageNo=0;
    private ImageSwitcher mImageSwitcher;
    private int[] imgIds;
    private int curPos;
    private LinearLayout linearLayout;
    private ImageView[] tips;
//    private ContentAdapter contentAdapter;
    private ListView listview;
//    private LoadMoreScrollView scrollView;
//    private List<ContentEntity> contentEntities = new ArrayList<>();
    private LinearLayout testBtn;
    private LinearLayout skinBtn;

//    @SuppressLint("HandlerLeak")
//    private Handler handler=new Handler(){
//        public void handleMessage(Message msg){
//            switch (msg.what){
//                case UPDATE_LISTVIEW:
//                    //contentEntities.clear();
//                    contentEntities.addAll((List<ContentEntity>)msg.obj);
//                    contentAdapter.notifyDataSetChanged();
//                    break;
//            }
//        }
//    };

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
    private void initView(View view){

        imgIds = new int[]{R.drawable.fig1, R.drawable.fig2, R.drawable.fig3};
        mImageSwitcher = view.findViewById(R.id.imageSwitcher1);
        mImageSwitcher.setFactory(this);
        linearLayout = view.findViewById(R.id.viewGroup);

        //设置banner
        setImageSwitcher();


//
//        //动态listview
//        listview = view.findViewById(R.id.home_listview);
//        getContent();
//        contentAdapter = new ContentAdapter(HealthRobotApplication.getContext(), contentEntities);
//        listview.setAdapter(contentAdapter);
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent();
//                intent.putExtra("art_id",contentEntities.get(i).getContentId());
//                intent.setClass(HomeFragment.this.getContext(), ActiveMsgContentActivity.class);
//                startActivity(intent);
//            }
//        });
//        LoadMore(view);//下拉加载更多

    }

//    private void getContent(){
//        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
//            @Override
//            public void run() {
//                Map<String,String> map= new HashMap<>();
//                map.put("pageNo",""+pageNo);//参数列表
//                Message msg=Message.obtain();
//                msg.obj=httpClientHelper.getContentEntitise(map);
//                msg.what=UPDATE_LISTVIEW;
//                handler.sendMessage(msg);
//            }
//        });
//    }

//    private void LoadMore(View view){
//        //sv_child=view.findViewById(R.id.linearLayout1);
//        //scrollview上拉加载
//        scrollView=view.findViewById(R.id.scrollview);
//        //scrollView.setFootView(sv_child);//设置scrollview尾布局
//        scrollView.setListsner(new LoadMoreScrollView.RefreshListener() {
//           /* @Override
//            public void startRefresh() {
//                //下拉刷新
//            }*/
//
//            @Override
//            public void loadMore() {
//                //上拉加载
//                pageNo++;
//                getContent();
//            }
//
//            @Override
//            public void hintChange(String hint) {
//
//            }
//
//            /*@Override
//            public void setWidthX(int x) {
//
//            }*/
//        });
//    }

    private void setEvent(View view){
//        msgView = view.findViewById(R.id.home_msg);
//        msgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeFragment.this.getContext(), SysMsgsActivity.class);
//                startActivity(intent);
//            }
//        });

        testBtn = view.findViewById(R.id.testbtn_home);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogFragment viewDialogFragment = new ViewDialogFragment();
                viewDialogFragment.show(getFragmentManager());
            }
        });
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
//            //横屏
//            skinBtn=view.findViewById(R.id.skin);
//            skinBtn.setOnClickListener(new View.OnClickListener(){
//                public void onClick(View view){
//                    Intent intent=new Intent(getActivity(),BeautyResultActivity.class);
//                    startActivity(intent);
//                }
//            });
        }
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
