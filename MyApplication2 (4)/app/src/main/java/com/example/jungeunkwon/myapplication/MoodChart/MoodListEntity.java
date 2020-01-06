package com.example.jungeunkwon.myapplication.MoodChart;

import org.chalup.microorm.annotations.Column;

public class MoodListEntity {
    @Column("Emoji")
    private String Emoji;

    @Column("Count")
    private int Count;


    public int getCOUNT() {
        return Count;
    }

    public String getEmoji() {
        return Emoji;
    }

    public void setCOUNT(int Count) {
        this.Count = Count;
    }

    public void setEmoji(String emoji) {
        Emoji = emoji;
    }

}
