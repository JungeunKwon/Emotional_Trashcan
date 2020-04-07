package com.Jungeun.wjdwjd95.emotional_trashcan.Diary;

import org.chalup.microorm.annotations.Column;

public class DiaryEntity {
    @Column("ID")
    private String ID;

    @Column("Date")
    private String Date;

    @Column("Context")
    private String Content;

    public String getID() {return ID;}
    public String getDate()
    {
        return Date;
    }
    public String getContent()
    {
        return Content;
    }
    public void setID(String value){ID = value; }
    public void setDate(String value)
    {
        Date = value;
    }
    public void setContent(String value)
    {
        Content= value;
    }
}
