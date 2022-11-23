package com.example.renderFarmServer.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Random;

@Entity
@Table(name="user_tasks")
public class UserTask {

    public UserTask(){}

    public UserTask(Long taskId, Long userId) {
        this.task_id = taskId;
        this.user_id = userId;
        this.start_time = OffsetDateTime.now();
        this.status = UserTaskStatus.RENDERING;
        this.render_time = (Math.abs(new Random().nextInt()) % 240) + 60;
    }

    @Id
    @Column
    private Long task_id;

    @Column
    private Long user_id;

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

    public Long getUser_id() { return user_id; }

    public void setUser_id(Long user_id) { this.user_id = user_id; }

    public OffsetDateTime getStart_time() { return start_time;}

    public OffsetDateTime getEnd_time() { return end_time; }

    public UserTaskStatus getStatus() { return status; }

    public Integer getRender_time() { return render_time; }
}
