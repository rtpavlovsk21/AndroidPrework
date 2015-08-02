package com.example.alyksia.myapplication;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by alyksia on 8/1/15.
 */

@Table(name="TodoItems")
public class TodoItem extends Model {
    @Column(name="idd")
    private int id;
    @Column(name="body")
    private String body;
    @Column(name="priority")
    private int priority;

    public String toString(){
        return this.body;
    }

    public TodoItem(){
        super();
    }

    public TodoItem(String body, int priority) {
        super();
        this.body = body;
        this.priority = priority;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getIdd() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
