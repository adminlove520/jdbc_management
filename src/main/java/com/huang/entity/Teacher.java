package com.huang.entity;

public class Teacher {
    private String teacherNum;
    private String teacherName;
    private String sex;
    private String title;

    public Teacher() {}

    public Teacher(String teacherNum, String teacherName, String sex, String title) {
        this.teacherNum = teacherNum;
        this.teacherName = teacherName;
        this.sex = sex;
        this.title = title;
    }

    public String getTeacherNum() {
        return teacherNum;
    }
    public String getTeacherName() {
        return teacherName;
    }
    public String getSex() {
        return sex;
    }
    public String getTitle() {
        return title;
    }
    public void setTeacherNum(String teacherNum) {
        this.teacherNum = teacherNum;
    }
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}