package com.android.sandwichmaker.model;

public class Action {

    private int id;
    private String desc;


    public Action(String enteredTask) {
        this.desc = enteredTask;

    }

    public Action(int id, String enteredTask) {
        this.id = id;
        this.desc = enteredTask;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
