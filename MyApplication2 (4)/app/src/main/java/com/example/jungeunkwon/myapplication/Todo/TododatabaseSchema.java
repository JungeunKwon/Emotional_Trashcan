package com.example.jungeunkwon.myapplication.Todo;

public interface TododatabaseSchema {
    String SQL_TABLE_CREATE = "CREATE TABLE [Todo]( [Id] NVARCHAR(50) PRIMARY KEY NOT NULL, [IsChecked] BIT, [Content] NVARCHAR(200), [EndDate] NVARCHAR(10))";
}
