package com.example.trabalhopoo.services;

import com.example.trabalhopoo.models.TaskModel;
import com.example.trabalhopoo.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public void save(TaskModel taskModel) {

        taskModel.setStatus("A FAZER");
        taskModel.setCreated_at(new Date());

        taskRepository.save(taskModel);
    }


    public List<TaskModel> findAll() {
        return taskRepository.findAll();
    }

    public void edit(TaskModel task) {
        taskRepository.save(task);
    }

    public Optional<TaskModel> findById(String id) {
        return taskRepository.findById(id);
    }

    public void delete (TaskModel taskModel) {
        taskRepository.delete(taskModel);
    }
}
