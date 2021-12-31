package com.ruzz.butilordering.Model;

public class NotificationModel {
    private String title;
    private String content;

    public NotificationModel(String title, String content) {
        this.content = content;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
