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
import android.widget.ListView;
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

public class fragment_lecture_article extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";



    private List<LectureCourse> data = new ArrayList<>();
    private ListView listView;
    private LectureAdapter lectureAdapter;
    LectureCourseAbstractInfo lectureCourseAbstractInfo;



    public static fragment_lecture_article newInstance(int columnCount) {
        fragment_lecture_article fragment = new fragment_lecture_article();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取已经到达 的讲座组消息数据，信息请求在 LectureActivtiy  被发送
        getLectureArticleAbstractAsync loadCourseGroupAsync = new  getLectureArticleAbstractAsync();
        loadCourseGroupAsync.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecture_article, container, false);

        listView = view.findViewById(R.id.lecture_article_listview);

//
//        // 测试开始
//        LectureCourse L1 = new LectureCourse();
//        LectureCourse L2 = new LectureCourse();
//
//        L1.setName("每天“保温杯里泡枸杞”，会给我们带来哪些好处？");
//        L1.setUpdateTime("18-11-2111:21");
//        L2.setName("正确泡枸杞的方法，很多人第一步都做错了，怪不得不好喝？");
//        L2.setUpdateTime("18-08-0816:13");
//        L1.setLectureID("6666");
//        L2.setLectureID("6666");
//        data.add(L1);
//        data.add(L2);
//        // 测试完毕

        lectureAdapter = new LectureAdapter(getActivity(), data);
        listView.setAdapter(lectureAdapter);


        //设置子选项点击监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final LectureCourse lectureCourseClicked = data.get(i);

                //向后台请求讲座音视频的详细内容
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WebSocketApplication.getWebSocketApplication().send(JsonUtil. lectureArticleDetail2Json(lectureCourseClicked.getLectureID()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("tag", "LoadEducationInfoAsync onPostExecute setOnItemClickListener exception");
                        }
                    }
                });

                Intent intent = new Intent(getContext(),LectureArticleDetailActivity.class);

//                //测试 开始
//                lectureCourseClicked.setName("每天“保温杯里泡枸杞”，会给我们带来哪些好处？");
//                lectureCourseClicked.setUpdateTime("18-11-2111:21");
//                lectureCourseClicked.setDescription("  将食材清洗干净后榨汁儿，每次服一小杯，每日3~4次，可以凉血、清热、败火。药补不如食补，食物一般都比较平和，长期吃身体也不会。凉拌糖醋藕清爽可口，排骨搭配莲藕做成汤，也是相当美味哦~中医私房菜——排骨莲藕汤排骨对身体有补益作用，但吃多了容上火，另外口感也比较油腻，莲藕是凉血去腻的，做排骨的时候搭配莲藕煮汤，两者结合能够补虚而不上火。润肺止咳吃什么？百合百合能够清热止咳、清心安神。中医讲白色入肺，因而白色的百合非常适合阴虚咳嗽、劳嗽咳嗽、久咳不愈、热病后咳嗽的人。《金匮要》记载，百合还能治疗“百合病”。百合病属于精神方面疾病，常见于更年期、经前期、产后的病人，中医称之为“脏燥”。实际上就是一种情绪上的不稳定，比如说心烦、失眠、抑郁、烦躁，对任何事情不感兴趣等。中医常用百合生地知母汤来调理“脏燥”，它除了可以润肺止咳，还可以降燥、平稳情绪，可以调养神经、滋养神经、安神定志。北方人吃羊肉的时候喜欢加上一些百合，一方面百合能润肺能生津养阴，另外一方面它还可以制约羊肉的辛燥，让口感变得更加爽口。百合的用方法有很多，可以煲汤，可以做成蒸百合来吃常见的菜品中甜口居多，其实百合也能做成咸口菜——百合木耳炒肉片！中私房菜——百炒肉片中医专家餐桌上都吃啥？您家餐桌上是不是也将要出现了呢？将食材清洗干净后榨汁儿，每次服一小杯，每日3~4次，可以凉血、清热、败火。药补不如食补，食物一般都比较平和，长期吃身体也不会。凉拌糖醋藕清爽可口，排骨搭配莲藕做成汤，也是相当美味哦~中医私房菜——排骨莲藕汤排骨对身体有补益作用，但吃多了容上火，另外口感也比较油腻，莲藕是凉血去腻的，做排骨的时候搭配莲藕煮汤，两者结合能够补虚而不上火。润肺止咳吃什么？百合百合能够清热止咳、清心安神。中医讲白色入肺，因而白色的百合非常适合阴虚咳嗽、劳嗽咳嗽、久咳不愈、热病后咳嗽的人。《金匮要》记载，百合还能治疗“百合病”。百合病属于精神方面疾病，常见于更年期、经前期、产后的病人，中医称之为“脏燥”。实际上就是一种情绪上的不稳定，比如说心烦、失眠、抑郁、烦躁，对任何事情不感兴趣等。中医常用百合生地知母汤来调理“脏燥”，它除了可以润肺止咳，还可以降燥、平稳情绪，可以调养神经、滋养神经、安神定志。北方人吃羊肉的时候喜欢加上一些百合，一方面百合能润肺能生津养阴，另外一方面它还可以制约羊肉的辛燥，让口感变得更加爽口。百合的用方法有很多，可以煲汤，可以做成蒸百合来吃常见的菜品中甜口居多，其实百合也能做成咸口菜——百合木耳炒肉片！中私房菜——百炒肉片中医专家餐桌上都吃啥？您家餐桌上是不是也将要出现了呢？  ");
//                //测试 完毕


                intent.putExtra("LectureCourse", lectureCourseClicked);
                startActivity(intent);


                try {
                    Thread.sleep(500);
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

            view = View.inflate(this.mContext, R.layout.lecture_article_listview_item, null);

            TextView articleTime = view.findViewById(R.id.lecture_article_upTtime);
            TextView articleName = view.findViewById(R.id.lecture_article_name);

            LectureCourse lectureCourse = this.lectureCourses.get(position);  //取出一节讲座的信息

            articleName.setText(lectureCourse.getName());
            articleTime.setText(lectureCourse.getUpdateTime());


            return view;
        }
    }




    // 获取已经到达的文章摘要信息
    class getLectureArticleAbstractAsync extends AsyncTask<Void, Void, Boolean> {

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
                    lectureCourseAbstractInfo = WebSocketApplication.getWebSocketApplication().getWebSocketHelperLectureArticleAllInfo();

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
