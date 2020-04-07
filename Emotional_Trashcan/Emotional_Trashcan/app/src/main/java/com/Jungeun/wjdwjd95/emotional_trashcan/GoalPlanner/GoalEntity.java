package com.Jungeun.wjdwjd95.emotional_trashcan.GoalPlanner;

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

    @Column("ConfirmDate")
    private String Comfirmdate;

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
    public String getComfirmdate(){return Comfirmdate;}
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

    public void setComfirmdate(String value) {
        Comfirmdate = value;
    }
}
