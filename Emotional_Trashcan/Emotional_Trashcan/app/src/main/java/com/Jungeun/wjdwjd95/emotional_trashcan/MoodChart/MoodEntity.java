package com.Jungeun.wjdwjd95.emotional_trashcan.MoodChart;

import org.chalup.microorm.annotations.Column;

public class MoodEntity {
    @Column("Date")
    private String Date;

    @Column("Emoji")
    private String Emoji;

    public String getDate() {
        return Date;
    }

    public String getEmoji() {
        return Emoji;
    }

    public void setDate(String date) {
        Date = date;
    }
    public void setEmoji(String emoji) {
        Emoji = emoji;
    }

}
