package aiwac.admin.com.healthrobot.bean;

import android.graphics.Bitmap;

/**
 * 保存三条体检推荐信息，只包括id和图片，用于轮播
 */
public class ExamInfoForCarousel extends BaseEntity {

    private String examID;
    private Bitmap cover;

    public String getExamID() {
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }
}


