package com.example.bean;


import org.json.JSONObject;

import java.sql.Date;

public class Issue {
    private int id;
    private String client_id;
    private int project_id;
    private Boolean done;
    private String title;
    private Date due_date;
    private String priority;
    private String project_client_id;

    public Issue(){}
    public Issue(int id, String client_id, int project_id, Boolean done, String title, Date due_date, String priority, String project_client_id) {
        this.id = id;
        this.client_id = client_id;
        this.project_id = project_id;
        this.done = done;
        this.title = title;
        this.due_date = due_date;
        this.priority = priority;
        this.project_client_id = project_client_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProject_client_id() {
        return project_client_id;
    }

    public void setProject_client_id(String project_client_id) {
        this.project_client_id = project_client_id;
    }

    @Override
    public String toString() {
        String jsonString = new JSONObject()
                .put("id", id)
                .put("client_id", client_id)
                .put("project_id", project_id)
                .put("done", done)
                .put("due_date", due_date)
                .put("title", title)
                .put("priority", priority)
                .put("project_client_id", project_client_id)
                .toString();

        System.out.println(jsonString);
        return jsonString;
    }
}