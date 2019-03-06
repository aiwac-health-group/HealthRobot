package aiwac.admin.com.healthrobot.medicalexam.model;

import java.util.Date;

import zuo.biao.library.base.BaseModel;

public class MedicalExam extends BaseModel {


    private int examID;//体检id
    private String title;//体检的标题
    private String description;//体检的简要描述
    private Date date;//体检的日期
    private String examContext;//体检的具体内容

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
    protected boolean isCorrect() {
        return false;
    }


    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getExamContext() {
        return examContext;
    }

    public void setExamContext(String examContext) {
        this.examContext = examContext;
    }
}
