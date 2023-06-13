package com.example.serverex3;

import java.util.Date;

public class ToDO {
    protected int id =1;
  protected String title;
    protected String content;
    protected long dueDate;
    protected Status status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        if(status.equals("PENDING"))
            this.status = Status.PENDING;
        if(status.equals("LATE"))
            this.status = Status.LATE;
        if(status.equals("DONE"))
            this.status = Status.DONE;
    }
}
