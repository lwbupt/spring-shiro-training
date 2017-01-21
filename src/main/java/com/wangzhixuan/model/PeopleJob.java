package com.wangzhixuan.model;

import java.io.Serializable;

/**
 * Created by wangwk on 2017/1/15.
 */
public class PeopleJob implements Serializable {
    private static final long serialVersionUID = -5321613594382537423L;

    private Long id;

    private String jobCategory;

    private String jobLevel;

    private String salary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }


    @Override
    public String toString(){
        return "People{" +
                "id=" + id +
                "}";
    }

}
