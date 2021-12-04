package com.example.bean;


import org.json.JSONObject;

public class Project {
    private int id;
    private String client_id;
    private String title;
    private boolean active;
    public Project(){}
    public Project(int id, String client_id, String title, boolean active) {
        this.id = id;
        this.client_id = client_id;
        this.title = title;
        this.active = active;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        String jsonString = new JSONObject()
                .put("id", id)
                .put("client_id", client_id)
                .put("title", title)
                .put("active", active)
                .toString();

        System.out.println(jsonString);
        return jsonString;
    }
}