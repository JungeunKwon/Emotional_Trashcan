package com.example.jungeunkwon.myapplication.Trash;

import org.chalup.microorm.annotations.Column;

public class TrashCommentEntity {
    @Column("Seq")
    private int Seq;
    @Column("Comment")
    private String Comment;

    public int getSeq() {
        return Seq;
    }

    public String getComment() {
        return Comment;
    }


    public void setComment(String comment) {
        Comment = comment;
    }

    public void setSeq(int seq) {
        Seq = seq;
    }


}
