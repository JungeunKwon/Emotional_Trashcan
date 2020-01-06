package com.example.jungeunkwon.myapplication.GoalPlanner;

import org.chalup.microorm.annotations.Column;

public class GoalEntity {
    @Column("ID")
    private String ID;

    @Column("StartDate")
    private String StartDate;

    @Column("Context")
    private String Content;

    @Column("EndDate")
    private String EndDate;

    @Column("Status")
    private String Status;

    @Column("Icon")
    private String Icon;

    public String getID() {return ID;}
    public String getStartDate()
    {
        return StartDate;
    }
    public String getEndDate(){return EndDate; }
    public String getContent()
    {
        return Content;
    }
    public String getStatus() {return Status; }
    public String getIcon() {return Icon;}
    public void setID(String value){ID = value; }
    public void setStartDate(String value)
    {
        StartDate = value;
    }
    public void setContent(String value)
    {
        Content= value;
    }
    public void setEndDate(String value) {EndDate = value;}
    public void setStatus(String value) {Status = value;}
    public void setIcon(String value) {Icon = value;}
}
