package com.example.jungeunkwon.myapplication.Diary;

public interface DiarySchema {
    public String SQL_TABLE_CREATE  = "CREATE TABLE [Diary]([ID] NVARCHAR(20) PRIMARY KEY NOT NULL , [Date] NVARCHAR(20), [Context] NVARCHAR(200) )";

}
