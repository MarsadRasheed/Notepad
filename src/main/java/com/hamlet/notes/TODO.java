package com.hamlet.notes;

public class TODO {
    private int itemId;
    private String toDo;
    private String description;
    private int priority;
    private String dateTime;

    public TODO(int itemId, String toDo, String description, int priority, String dateTime) {
        this.itemId = itemId;
        this.toDo = toDo;
        this.description = description;
        this.priority = priority;
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getToDo() {
        return toDo;
    }

    public void setToDo(String toDo) {
        this.toDo = toDo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
