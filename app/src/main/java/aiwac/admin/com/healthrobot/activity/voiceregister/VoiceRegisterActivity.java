package aiwac.admin.com.healthrobot.activity.voiceregister;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import aiwac.admin.com.healthrobot.BaiduSpeechSynthesizer.SpeechSyntherizer;
import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.bean.ProvinceBean;
import aiwac.admin.com.healthrobot.bean.RegisterInfo;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import zuo.biao.library.base.BaseActivity;

public class VoiceRegisterActivity extends BaseActivity {

    private Spinner spinner_province;
    private Spinner spinner_city;
    private Spinner spinner_hospital;
    private Spinner spinner_department;
    private Button btn_send;
    private Button btn_registerHistory;

    private ArrayList<String> provinceList = new ArrayList<>(); //省
    private ArrayList<ArrayList<String>> cityList = new ArrayList<>();//市
    private ArrayList<ArrayList<ArrayList<String>>>  hospitalList = new ArrayList<>();//区

    private ArrayList<String> options1Items; //省
    private ArrayList<String> options2Items;//市
    private ArrayList<String> options3Items;//区
    private String[] departments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_register);
        initJsonData();
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                //语音合成
                SpeechSyntherizer.speak("请选择您的所在地区挂号医院和科室");
            }
        });
        btn_send = (Button) findViewById(R.id.btn_send);
        spinner_province = (Spinner)findViewById(R.id.province);
        spinner_city = (Spinner)findViewById(R.id.city);
        spinner_hospital = (Spinner)findViewById(R.id.hospital);
        spinner_department = (Spinner)findViewById(R.id.department);
        btn_registerHistory = findView(R.id.btn_register_history);

        btn_registerHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            BaseEntity baseEntity = new BaseEntity();
                            baseEntity.setBusinessType(Constant.WEBSOCKET_REGISTERHISTORY_BUSSINESSTYPE_CODE);
                            String json = JsonUtil.baseEntity2Json(baseEntity);
                            WebSocketApplication.getWebSocketApplication().send(json);
                        }catch (Exception e){
                            LogUtil.d( e.getMessage());
                            //其他异常处理
                        }
                    }
                });
                /*toActivity(RegisterHistoryActivity.createIntent(context));*/

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(provinceName.equals("")){
                    Toast.makeText(VoiceRegisterActivity.this, "请选择省份", Toast.LENGTH_SHORT).show();
                }else if(cityName.equals("")){
                    Toast.makeText(VoiceRegisterActivity.this, "请选择城市", Toast.LENGTH_SHORT).show();
                }else if(hosipitalName.equals("")){
                    Toast.makeText(VoiceRegisterActivity.this, "请选择医院", Toast.LENGTH_SHORT).show();
                }else if(departmentName.equals("")){
                    Toast.makeText(VoiceRegisterActivity.this, "请选择科室", Toast.LENGTH_SHORT).show();
                }else{
                    showShortToast("挂号申请提交成功");

                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                RegisterInfo registerInfo = new RegisterInfo(provinceName,cityName,hosipitalName,departmentName);
                                registerInfo.setBusinessType(Constant.WEBSOCKET_VOICEREGISTER_BUSSINESSTYPE_CODE);
                                String json = JsonUtil.registerInfoToJson(registerInfo);
                                WebSocketApplication.getWebSocketApplication().send(json);
                            }catch (Exception e){
                                showShortToast("网络信号不好");
                                LogUtil.d( e.getMessage());
                                //其他异常处理
                            }
                        }
                    });
                }

            }
        });

        showData();


    }


    private ArrayAdapter<String> provinceAdapter;//省份数据适配器
    private ArrayAdapter<String> cityAdapter;//城市数据适配器
    private ArrayAdapter<String> hosipitalAdapter;//医院数据适配器
    int provinceId = 0;
    private String provinceName;
    private String cityName = "";
    private String hosipitalName = "";
    private String departmentName = "";

    private void showData(){



        spinner_province.setAdapter(provinceAdapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options1Items));
        spinner_city.setAdapter(cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options2Items));
        spinner_hospital.setAdapter(hosipitalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options3Items));

        spinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                provinceName = options1Items.get(position);
                provinceId  = position;
                options2Items.clear();
                for(String city:cityList.get(position)){
                    options2Items.add(city);
                }
                spinner_city.setSelection(0);
                cityAdapter.notifyDataSetChanged();

                options3Items.clear();
                for(String hospital:hospitalList.get(position).get(0)){
                    options3Items.add(hospital);
                }
                spinner_hospital.setSelection(0);
                hosipitalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityName = options2Items.get(position);
                options3Items.clear();
                for(String hospital:hospitalList.get(provinceId).get(position)){
                    options3Items.add(hospital);
                }
                spinner_hospital.setSelection(0);
                hosipitalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinner_hospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hosipitalName = options3Items.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinner_department.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departments));
        spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentName = departments[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public String getJson( String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    private void initJsonData() {//解析数据 （省市区三级联动）
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = getJson("hospital.json");//获取assets目录下的json文件数据

        ArrayList<ProvinceBean> provinceBeans = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        //options1Items = provinceBeans;

        for (int i = 0; i < provinceBeans.size(); i++) {//遍历省份
            provinceList.add(provinceBeans.get(i).getName());
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三级）

            for (int c = 0; c < provinceBeans.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = provinceBeans.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (provinceBeans.get(i).getCityList().get(c).getArea() == null
                        || provinceBeans.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(provinceBeans.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            cityList.add(CityList);

            /**
             * 添加地区数据
             */
            hospitalList.add(Province_AreaList);
        }
        options1Items = provinceList;
        options2Items = cityList.get(0);
        options3Items = hospitalList.get(0).get(0);
        departments = getResources().getStringArray(R.array.hospital_department);
    }


    public ArrayList<ProvinceBean> parseData(String result) {//Gson 解析
        ArrayList<ProvinceBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                ProvinceBean entity = gson.fromJson(data.optJSONObject(i).toString(), ProvinceBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
