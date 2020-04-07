package com.Jungeun.wjdwjd95.emotional_trashcan.Trash;

public interface TrashSchema {
    public String SQL_TABLE_CREATE  = "CREATE TABLE [Trash]([Date] NVARCHAR(20), [Title] NVARCHAR(20), [Context] NVARCHAR(200), [CommentSeq] INTEGER)";
    //title as a key(id)
}
