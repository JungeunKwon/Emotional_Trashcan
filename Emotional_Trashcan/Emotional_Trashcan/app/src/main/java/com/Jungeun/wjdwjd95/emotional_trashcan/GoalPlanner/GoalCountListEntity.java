package com.Jungeun.wjdwjd95.emotional_trashcan.GoalPlanner;

import org.chalup.microorm.annotations.Column;

public class GoalCountListEntity {
    @Column("ID")
    private String ID;

    @Column("Count")
    private int Count;


    public int getCOUNT() {
        return Count;
    }

    public String getEmoji() {
        return ID;
    }

    public void setCOUNT(int Count) {
        this.Count = Count;
    }

    public void setEmoji(String value) {
        ID = value;
    }

}