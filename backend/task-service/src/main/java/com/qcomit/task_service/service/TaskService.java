package com.qcomit.task_service.service;

import com.qcomit.task_service.dto.TaskDTO;
import com.qcomit.task_service.entity.Task;

import java.util.List;

public interface TaskService {
    List<TaskDTO> getAllTasks();
    TaskDTO getTaskById(Long id);
    TaskDTO createTask(TaskDTO taskDTO);
    TaskDTO updateTask(Long id, TaskDTO taskDTO);
    void deleteTask(Long id);
}
