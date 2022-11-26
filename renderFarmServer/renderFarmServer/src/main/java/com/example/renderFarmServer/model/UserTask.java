package com.example.renderFarmServer.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name="user_tasks")
public class UserTask {

    public UserTask(){}

    public UserTask(UserTask userTask) {
        this.username = userTask.getUsername();
        this.task_name = userTask.getTask_name();
        this.start_time = OffsetDateTime.now();
        this.status = UserTaskStatus.RENDERING;
        this.render_time = (Math.abs(new Random().nextInt()) % 240) + 60;
    }

    @Id
    @Column
    @GeneratedValue
    private UUID task_id;

    @Column
    private String username;

    @Column
    private String task_name;

    @Column
    private OffsetDateTime start_time;

    @Column
    private OffsetDateTime end_time;

    @Column
    @Enumerated(EnumType.STRING)
    private UserTaskStatus status;

    @Transient
    private Integer render_time;

    public UUID getTask_id() { return task_id; }

    public void setTask_id(UUID task_id) { this.task_id = task_id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getTask_name() { return task_name; }

    public void setTask_name(String task_name) { this.task_name = task_name; }

    public OffsetDateTime getStart_time() { return start_time;}

    public OffsetDateTime getEnd_time() { return end_time; }

    public void setEnd_time(OffsetDateTime end_time) { this.end_time = end_time; }

    public UserTaskStatus getStatus() { return status; }

    public void setStatus(UserTaskStatus status) { this.status = status; }

    public Integer getRender_time() { return render_time; }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> outputMap = new HashMap<>();
        outputMap.put("taskID", this.task_id.toString());
        outputMap.put("taskName", this.task_name);
        outputMap.put("status", this.status.toString());
        return outputMap;
    }
}