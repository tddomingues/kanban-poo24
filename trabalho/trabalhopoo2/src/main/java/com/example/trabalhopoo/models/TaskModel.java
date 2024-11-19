package com.example.trabalhopoo.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;


@Entity
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String description;

    private String status;
    private String priority;
    private Date created_at;
    private Date deadline;

    public String getId() {
        return id;
    }

    public Date getDeadline() {
        return deadline;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", title=" + title +
                ", description=" + description +
                ", status=" + status + ", priority="
                + priority + ", created_at=" + created_at +
                ", deadline=" + deadline + "]";
    }
}