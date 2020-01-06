package com.example.jungeunkwon.myapplication.GoalPlanner;

public interface GoalSchema {
    public String SQL_TABLE_CREATE  = "CREATE TABLE [Goal]([ID] NVARCHAR(20) PRIMARY KEY NOT NULL ,[Context] NVARCHAR(200), [StartDate] NVARCHAR(20),[EndDate] NVARCHAR(20), [Status]  NVARCHAR(20), [Icon] NVARCHAR(20))";

}
