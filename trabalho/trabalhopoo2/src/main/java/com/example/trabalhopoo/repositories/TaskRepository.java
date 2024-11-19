package com.example.trabalhopoo.repositories;

import com.example.trabalhopoo.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, String> {
    List<TaskModel> findByStatus(String status);
}
