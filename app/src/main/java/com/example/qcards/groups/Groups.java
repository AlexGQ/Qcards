package com.example.qcards.groups;

public class Groups {
	 
    int id;
    String group_name;
    String date_time;
 
    // constructors
    public Groups() {
 
    }
 
    public Groups(String group_name) {
        this.group_name = group_name;
    }
 
    public Groups(int id, String group_name, String date_time) {
        this.id = id;
        this.group_name = group_name;
        this.date_time = date_time;
    }
 
    // setter
    public void setId(int id) {
        this.id = id;
    }
 
    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }
 
    // getter
    public int getId() {
        return this.id;
    }
 
    public String getGroupName() {
        return this.group_name;
    }
}
