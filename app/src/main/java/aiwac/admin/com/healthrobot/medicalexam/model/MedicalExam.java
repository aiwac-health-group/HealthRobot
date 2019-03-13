package aiwac.admin.com.healthrobot.medicalexam.model;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import zuo.biao.library.base.BaseModel;

public class MedicalExam extends BaseModel {


    private int examID;//体检id


    private String name;//体检的标题
    private String description;//体检的描述
    private Date date;//体检的日期
    private Bitmap cover;//体检的图片

    private  String examContext;//体检的具体内容 ，用来在体检的具体内容页面显示，因为只有一个页面，只需要一个，所以作为static

    public String getDataToShowAsText(){
        if(date!=null){
            String showDate="";
            showDate+=(date.getYear()+1900)+"年"+date.getMonth()+"月"+date.getDay()+"日  "
                    +(date.getHours()==0?"00":date.getHours())+ ":"
                    +(date.getMinutes()==0?"00":date.getMinutes())+":"
                    +(date.getSeconds()==0?"00":date.getSeconds());
            return showDate;
        }
        return "";
    }

    @Override
    public String toString() {
        return "examID:"+examID+" name:"+name+" description:"+description+" date:"+date+" cover:"+cover;
    }

    @Override
    protected boolean isCorrect() {
        return false;
    }


    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public void setDate(String dateString){
        try {
            this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }
    public String getExamContext() {
        return examContext;
    }

    public void setExamContext(String examContext) {
        this.examContext = examContext;
    }

}
