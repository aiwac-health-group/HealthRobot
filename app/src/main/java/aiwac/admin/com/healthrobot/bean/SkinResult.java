package aiwac.admin.com.healthrobot.bean;

import android.graphics.Bitmap;

public class SkinResult extends BaseEntity{

    private Bitmap face;            //照片，Base64str

    private Integer score;          //测肤得分
    private float[] heitouResults;  //黑头结果，第一版时有2个数值
    private float[] douResults;     //痘结果，第一版时有2个数值
    private float[] banResults;     //斑结果，第一版时有2个数值
    private float[] fuseResults;    //肤色结果，第一版时有3个数值
    private String body;     //体质描述
    private String diet;     //饮食建议
    private String medicine;       //美容中药方剂
    private String drug;        //生药

    public SkinResult(){}
    @Override
    public String toString() {
        return "Skin["+ face + ", " + score + "]";
    }


//-----------------------------------------------


    public Bitmap getFace() {
        return face;
    }

    public void setFace(Bitmap face) {
        this.face = face;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    //-------------------------------------
    public float[] getHeitouResults() {
        return heitouResults;
    }

    public void setHeitouResults(float[] heitouResults) {
        this.heitouResults = heitouResults;
    }
    public void setHeitouResults(float a,float b) {
        this.heitouResults[0] = a;
        this.heitouResults[1] = b;
    }
    //-------------------------------------

    public float[] getDouResults() {
        return douResults;
    }

    public void setDouResults(float[] douResults) {
        this.douResults = douResults;
    }
    public void setDouResults(float a,float b) {
        this.douResults[0] = a;
        this.douResults[1] = b;
    }
    //-------------------------------------

    public float[] getBanResults() {
        return banResults;
    }

    public void setBanResults(float[] banResults) {
        this.banResults = banResults;
    }
    public void setBanResults(float a,float b) {
        this.banResults[0] = a;
        this.banResults[1] = b;
    }
    //-------------------------------------
    public float[] getFuseResults() {
        return fuseResults;
    }

    public void setFuseResults(float[] fuseResults) {
        this.fuseResults = fuseResults;
    }
    public void setFuseResults(float a,float b, float c) {
        this.fuseResults[0] = a;
        this.fuseResults[1] = b;
        this.fuseResults[2] = c;
    }
    //-------------------------------------
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }
}
