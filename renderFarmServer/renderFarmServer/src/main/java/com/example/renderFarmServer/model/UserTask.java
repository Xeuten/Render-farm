package com.example.renderFarmServer.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Random;

@Entity
@Table(name="user_tasks")
public class UserTask {

    public UserTask(){}

    public UserTask( String userId) {
        this.username = userId;
        this.start_time = OffsetDateTime.now();
        this.status = UserTaskStatus.RENDERING;
        this.render_time = (Math.abs(new Random().nextInt()) % 240) + 60;
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long task_id;

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

    public Long getTask_id() { return task_id; }

    public void setTask_id(Long task_id) { this.task_id = task_id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getTask_name() { return task_name; }

    public void setTask_name(String task_name) { this.task_name = task_name; }

    public OffsetDateTime getStart_time() { return start_time;}

    public OffsetDateTime getEnd_time() { return end_time; }

    public UserTaskStatus getStatus() { return status; }

    public Integer getRender_time() { return render_time; }
}
