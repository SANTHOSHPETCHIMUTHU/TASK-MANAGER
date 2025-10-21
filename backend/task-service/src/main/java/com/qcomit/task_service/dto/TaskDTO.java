package com.qcomit.task_service.dto;

import com.qcomit.task_service.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;
    private Long employeeId;
}
