package aiwac.admin.com.healthrobot.bean;

import android.graphics.Bitmap;

import java.io.Serializable;


//   存讲座视频  or 音频 名字图片等摘要信息
public class LectureCourse implements Serializable {

    protected String lectureID;
    protected String name;
    protected String updateTime;
    protected String description;
    protected Bitmap cover;
    protected String duration;


    public String getLectureID() {
        return lectureID;
    }
    public void setLectureID(String lectureID) {
        this.lectureID = lectureID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getCover() {
        return cover;
    }
    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
}
