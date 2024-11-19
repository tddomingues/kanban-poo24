package com.example.trabalhopoo.controllers;


import com.example.trabalhopoo.models.TaskModel;
import com.example.trabalhopoo.repositories.TaskRepository;
import com.example.trabalhopoo.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class TaskController {

    private int getPriorityOrder(String priority) {
        switch (priority.toUpperCase()) {
            case "ALTA":
                return 1;
            case "MEDIA":
                return 2;
            case "BAIXA":
                return 3;
            default:
                return 4;
        }
    }


    @Autowired
    private TaskService taskService;

    // obtendo todos as tarefas
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskModel>> getAllTasks() {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findAll());
    }

    // obtendo tarefa por id
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Object> getTask(@PathVariable(value = "id") String id) {
        Optional<TaskModel> taskO = taskService.findById(id);

        if(taskO.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(taskService.findById(taskO.get().getId()));
    }

    // obtendo tarefas ordenadas (3.1)
    @GetMapping("/tasks/order-priority")
    public ResponseEntity<Object> getTaskByOrderPriority() {
        List<TaskModel> tasks = taskService.findAll();
        List<TaskModel> taskOrder;

        taskOrder = tasks.stream()
                .sorted((task1, task2) -> {
                    int priority1 = getPriorityOrder(task1.getPriority());
                    int priority2 = getPriorityOrder(task2.getPriority());
                    return Integer.compare(priority1, priority2);
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(taskOrder);
    }

    // filtrar tarefas por prioridade e data limite. (3.2)
    @GetMapping("/tasks/order-priority-date")
    public ResponseEntity<Object> getTaskByOrderPriorityDate() {
        List<TaskModel> tasks = taskService.findAll();
        List<TaskModel> taskOrder1;
        List<TaskModel> taskOrder2;


        taskOrder1 = tasks.stream()
                .sorted((task1, task2) -> {
                    int priority1 = getPriorityOrder(task1.getPriority());
                    int priority2 = getPriorityOrder(task2.getPriority());
                    return Integer.compare(priority1, priority2);
                })
                .collect(Collectors.toList());

        taskOrder2 = tasks.stream()
                .filter(task -> task.getDeadline().before(task.getCreated_at()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(taskOrder2);
    }

    // obtendo as tarefas atrasadas (3.3)
    @GetMapping("/tasks/late/{id}")
    public ResponseEntity<Object> getLateTask(@PathVariable(value = "id") String id) {
        List<TaskModel> tasks = taskService.findAll();
        List<TaskModel> tasksLate;

        tasksLate = tasks.stream()
                .filter(task -> task.getDeadline().before(task.getCreated_at()) && !task.getStatus().equals("CONCLUIDO"))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(tasksLate);
    }

    // obtendo tarefa por status (2.2)
    @GetMapping("/tasks/type")
    public ResponseEntity<Object> getTaskByStatus(@RequestParam(value = "status") String status) {

        if(status.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sem status.");
        }

        List<TaskModel> tasks = taskService.findAll();
        List<TaskModel> tasksByStatus;

        if(status.equals("afazer")) {
            tasksByStatus = tasks.stream()
                    .filter(task -> "A FAZER".equals(task.getStatus()))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(tasksByStatus);
        }

        if(status.equals("emprogresso")) {
            tasksByStatus = tasks.stream()
                    .filter(task -> "EM PROGRESSO".equals(task.getStatus()))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(tasksByStatus);
        }

        if(status.equals("concluido")) {
            tasksByStatus = tasks.stream()
                    .filter(task -> "CONCLUIDO".equals(task.getStatus()))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(tasksByStatus);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Não encontrado.");
    }

    // criando tarefa (2.1)
    @PostMapping("/tasks/create-task")
    public ResponseEntity<Object> saveTask(@RequestBody TaskModel task ) {

        taskService.save(task);

        return ResponseEntity.status(HttpStatus.CREATED).body("Tarefa criada com sucesso.");
    }

    // deletando tarefa (2.5)
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable(value = "id") String id) {
        Optional<TaskModel> taskO = taskService.findById(id);

        if(taskO.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        taskService.delete(taskO.get());

        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso");

    }

    // editar tarefa (2.4)
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable(value = "id") String id, @RequestBody TaskModel task) {
        Optional<TaskModel> taskO = taskService.findById(id);

        if(taskO.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        TaskModel taskModel = taskO.get();

        System.out.println(taskModel);

        if (task.getTitle() != null && !task.getTitle().isEmpty()) {
            taskModel.setTitle(task.getTitle());
        }
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            taskModel.setDescription(task.getDescription());
        }
        if (task.getPriority() != null) {
            taskModel.setPriority(task.getPriority());
        }
        if (task.getStatus() != null) {
            taskModel.setStatus(task.getStatus());
        }
        if (task.getDeadline() != null) {
            taskModel.setDeadline(task.getDeadline());
        }

        taskService.edit(taskModel);

        return ResponseEntity.status(HttpStatus.OK).body("Tarefa editada com sucesso.");
    }

    // mover uma tarefa entre as colunas (2.3)
    @PutMapping("/tasks/update-status/{id}")
    public ResponseEntity<Object> updateStatusTask(@PathVariable(value = "id") String id, @RequestBody String status) {
        Optional<TaskModel> taskO = taskService.findById(id);

        // o status seria 'A FAZER', 'EM ANDAMENTO' e 'CONCLUIDO', mas não tem validação

        if(taskO.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        TaskModel taskModel = taskO.get();

        taskModel.setStatus(status);
        taskService.save(taskModel);

        return ResponseEntity.status(HttpStatus.OK).body("Status movido com sucesso.");
    }
}
