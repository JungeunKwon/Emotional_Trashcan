package com.Jungeun.wjdwjd95.emotional_trashcan.GoalPlanner;

import org.chalup.microorm.annotations.Column;

public class GoalDetailEntity {
    @Column("ID")
    private String ID;

    @Column("Date")
    private String Date;


    public String getID() {return ID;}
    public String getDate()
    {
        return Date;
    }
    public void setID(String value){ID = value; }
    public void setDate(String value)
    {
        Date = value;
    }

}
