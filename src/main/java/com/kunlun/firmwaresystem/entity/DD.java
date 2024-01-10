package com.kunlun.firmwaresystem.entity;

import java.util.ArrayList;
import java.util.List;

public class DD {
    int id;
    String title;
   ArrayList<DD> children;

    public void setId(int id) {
        this.id = id;
    }

    public void setChildren(ArrayList<DD> children) {
        this.children = children;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public ArrayList<DD> getChildren() {
        if(children==null){
            children=new ArrayList<>();
        }
        return children;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "DD{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", children=" + children +
                '}';
    }
}
