package aiwac.admin.com.healthrobot.medicalexam.adapter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;

public class GetMedicalExamUtil {

    public static List<MedicalExam> getList() {
        return list;
    }

    public static List<MedicalExam> list = new ArrayList<MedicalExam>();

    public static List<MedicalExam> getUserList() {
        return getUserList(0);
    }
    /**
     * @param page 页码
     * @return
     */
    public static List<MedicalExam> getUserList(int page) {
        return getUserList(page, 10);
    }
    /**
     * @param page 页码
     * @param count 最大一页数量
     * @return
     */
    public static List<MedicalExam> getUserList(int page, int count) {
        list=textGetExam();
        return list;
    }

    /**
     * 这个是从网页端获取数据后，将其初始化在这个类里
     * @return
     */
    public boolean initList(List<MedicalExam> list){
        this.list = list;
        return true;
    }

    /**
     * 这个是本地生成的测试数据
     * @return
     */
    private static List<MedicalExam> textGetExam(){
        List<MedicalExam> list = new ArrayList<MedicalExam>();
        MedicalExam medicalExam;
        for(int i=0;i<20;i++){
            medicalExam=new MedicalExam();
            medicalExam.setExamID(i+1);
            if(i%2==0){
                medicalExam.setName("大学生入学体检");
                //medicalExam.setDescription("为新入学的大学生提供体检服务");
                try {
                    medicalExam.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-12-12 14:30:00"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                medicalExam.setDescription("入学时有新生体检，入学第一年下学期第二次体检为机能测试体检，第二年机能测试一次体检，第三年又进行第三次机能测试体检，" +
                        "主要目的是了解大学生在校期间的生长、发育状况和生理机能状况，第四学期是大学生毕业体检，有些大学生在毕业体检时又进行考研体检或参加工作体检。" +
                        "体检的内容大致一样，除机能测试内容要求简单一些外，其他体检内容都一样，比较细，内容较全面。\n 机能测试体检内容主要侧重生长、发育、体态、" +
                        "营养状况或机能状况以及在校期间是否患了有关传染病。内容有身高、体重、胸围、肺活量、视力、胸透、肝功能、乙肝及血常规尿常规的检查。");
            }else{
                medicalExam.setName("aiwac员工入职体检");
                //medicalExam.setDescription("为新aiwac来的员工提供体检服务");
                try {
                    medicalExam.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-12-12 14:30:00"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                medicalExam.setDescription("很多同学在体检时感到紧张害怕，尤其是在新生人学时的体检，表现得异常紧张。越是紧张，越是容易在体检中出现问题，" +
                        "比如由于紧张就可能出现心动过速、血压升髙等。另外由于入学前，一些同学过分高兴，或者过多游玩，缺乏休息，而使自己身体处于一种疲劳状态，" +
                        "休息不足、过度劳累都会引起肝功能异常。此外，感冒发烧会引起尿常规异常，或者出现意外伤，也会引起入学时体检的结果不正常或不合格。" +
                        "所以，在接到入学通知后，家长或学生都不要过度兴奋，以免影响新生人学时的体检结果。");
            }
            list.add(medicalExam);
        }
        return list;
    }
}
