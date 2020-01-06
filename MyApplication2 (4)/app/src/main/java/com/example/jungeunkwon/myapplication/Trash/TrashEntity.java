package com.example.jungeunkwon.myapplication.Trash;

import org.chalup.microorm.annotations.Column;

public class TrashEntity {
    @Column("Date")
    private String Date;

    @Column("Title")
    private String Title;

    @Column("Context")
    private String Content;

    @Column("CommentSeq")
    private int CommentSeq;

    public String getTitle() {return Title;}
    public String getDate()
    {
        return Date;
    }
    public String getContent()
    {
        return Content;
    }
    public void setID(String value){Title = value; }
    public void setDate(String value)
    {
        Date = value;
    }
    public void setContent(String value)
    {
        Content= value;
    }
    public void setCommentSeq(int commentSeq) {
        CommentSeq = commentSeq;
    }

    public int getCommentSeq() {
        return CommentSeq;
    }

}
