package aiwac.admin.com.healthrobot.activity.lecture;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.LectureCourse;
import aiwac.admin.com.healthrobot.bean.LectureCourseAbstractInfo;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;

public class fragment_lecture_video extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";


    private List<LectureCourse> data = new ArrayList<>();
    private LectureAdapter lectureAdapter;
    protected GridView lectureGridView;
    LectureCourseAbstractInfo lectureCourseAbstractInfo;

    public static fragment_lecture_audio newInstance(int columnCount) {
        fragment_lecture_audio fragment = new fragment_lecture_audio();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //获取已经到达 的讲座组消息数据，信息请求在 LectureActivtiy  被发送
        getLectureVideoAbstractAsync loadCourseGroupAsync = new getLectureVideoAbstractAsync();
        loadCourseGroupAsync.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecture_audi_video, container, false);

        lectureGridView = view.findViewById(R.id.lecture_video_music_Gridview);

//        // 测试开始
//        LectureCourse L1 = new LectureCourse();
//        LectureCourse L2 = new LectureCourse();
//
//        L1.setName("迦百农是圣经中的地名，系加利利海附近一域，据称耶稣开始传道时，即迁居");
//        L1.setUpdateTime("2019-03-02");
//        L1.setDescription("迦百农是圣经中的地名，系加利利海附近一域，据称耶稣开始传道时，即迁居此地，有不少神迹和重要的事情在这地方发生。拉巴基本片介于纪录片和小说之间，通过该片，她继续探索黎巴嫩社会，对这个不和谐社会系统的缺陷进行具有国际影响力的反思。 \n" +
//                "\n" +
//                "　　影片讲述了一个12岁男孩Zain的艰难历程 ，他状告父母让其来到这个世界，却没有能够好好的抚养他。故事就此展开，并对他存在的合法性产生质疑：除了被虐待之外，这个幼小的儿童生来就没有任何身份。通过Zain的奋争，迦百农希望成为所有没有获得基本权利保障、缺乏教育、健康和爱的人们的代言人。 \n" +
//                "\n" +
//                "　　为了让人们意识到这个残酷的现实和所讲故事的真实性，拉巴基选择一批真实生活于迦百农类似的人们出演本片。这位女影人在影片中展现他们曾经看到的情况或经历过的生活，她说：“演员必须曾经经历过这样的生活，没有那些将这部电影作为维护他们的奋斗目标的旗帜的人们，就没有这部电影”。 ");
//
//        L1.setDuration("2小时30分");
//        L1.setLectureID("6666");
//        data.add(L1);
//        data.add(L1);
//        for (int a =0; a<200; a++) {
//            data.add(L1);
//        }
        // 测试开始



        lectureAdapter = new LectureAdapter(getActivity(), data);
        lectureGridView.setAdapter(lectureAdapter);

        //设置子选项点击监听事件
        lectureGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final LectureCourse lectureCourseClicked = data.get(i);

                //向后台请求讲座音视频的详细内容
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                WebSocketApplication.getWebSocketApplication().send(JsonUtil.lectureAVDetail2Json( lectureCourseClicked.getLectureID()));
                            }catch (Exception e){
                                e.printStackTrace();
                                Log.d("tag", "LoadEducationInfoAsync onPostExecute setOnItemClickListener exception");
                            }
                        }
                    });

                    Intent intent = new Intent(getContext(), LectureVideoDetailActivity.class);
                    intent.putExtra("LectureCourse",lectureCourseClicked);
                    startActivity(intent);


                    try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    protected class LectureAdapter extends BaseAdapter {
        private List<LectureCourse> lectureCourses;
        private Context mContext;

        public LectureAdapter(Context mContext, List<LectureCourse> lectureCourses) {
            this.mContext = mContext;
            this.lectureCourses = lectureCourses;
        }

        @Override
        public int getCount() {
            return this.lectureCourses.size();
        }

        @Override
        public Object getItem(int i) {
            return this.lectureCourses.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if (view == null)

                view = View.inflate(this.mContext, R.layout.lecture_audi_video_gridview_item, null);

            TextView lecture_name = view.findViewById(R.id.lecture_gridview_name);
            ImageView cover_image = view.findViewById(R.id.lecture_gridview_cover_image);

            LectureCourse lectureCourse = this.lectureCourses.get(position);  //取出一节讲座的信息

            lecture_name.setText(lectureCourse.getName());
            //集成需要加入
            cover_image.setImageBitmap(lectureCourse.getCover());

            return view;
        }
    }







    class getLectureVideoAbstractAsync extends AsyncTask<Void, Void, Boolean> {

        private AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view = View.inflate(getContext(), R.layout.activity_progress, null);
            builder.setIcon(R.drawable.login_aiwac_log);
            builder.setTitle(Constant.WEBSOCKET_BUSINESS_DOWNLOAD_LECTURE);
            builder.setView(view);  //必须使用view加载，如果使用R.layout设置则无法改变bar的进度

            dialog = builder.create();
            dialog.setCancelable(false);

            dialog.show();
        }

        // 获取已经到达的讲座摘要数据
        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                for (int i = 0; i < 5; i++) {
                    Thread.sleep(500);
                    lectureCourseAbstractInfo = WebSocketApplication.getWebSocketApplication().getWebSocketHelperLectureVideoAllInfo();

                    if (lectureCourseAbstractInfo != null) {
                        for(  LectureCourse item : lectureCourseAbstractInfo.getLectureCourseAbstracts()){
                            data.add(item);
                        }
                        return true;
                    }
                }
            } catch (Exception e) {
                Log.d("tag", e.getMessage());
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.cancel();
            if (aBoolean) {   //加载讲座列表
                lectureAdapter.notifyDataSetChanged();

            } else { // 失败。显示空白

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.drawable.login_aiwac_log);
                builder.setTitle("抱歉");
                builder.setMessage("亲，暂无资源~");
                builder.setNegativeButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                             ;
                    }
                });

                builder.show();

            }
        }
    }

}


