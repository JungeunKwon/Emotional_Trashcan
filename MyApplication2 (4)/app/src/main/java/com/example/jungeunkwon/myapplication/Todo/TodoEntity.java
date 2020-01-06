package com.example.jungeunkwon.myapplication.Todo;
import org.chalup.microorm.annotations.Column;
public class TodoEntity {
    @Column("Id")
    private String Id;
    @Column("IsChecked")
    private Boolean IsChecked;
    @Column("Content")
    private String Content;
    @Column("EndDate")
    private String EndDate;

    public void setId(String value){ Id= value;}
    public void setChecked(Boolean value){ IsChecked = value;}
    public void setContent(String value) { Content = value;}

    public String getContent() {
        return Content;
    }

    public Boolean getChecked() {
        return IsChecked;
    }

    public String getId() {
        return Id;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String value) {
        EndDate = value;
    }
}
