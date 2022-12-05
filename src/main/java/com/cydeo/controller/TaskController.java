package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    public final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getTasks(){
       List<TaskDTO> taskDTOList= taskService.listAllTasks();
       return ResponseEntity.ok(new ResponseWrapper("Tasks retrieved", taskDTOList, HttpStatus.OK));
    }

    @GetMapping("{taskId}")
    public ResponseEntity<ResponseWrapper> getTaskById(@PathVariable("taskId") Long id) {
       TaskDTO taskDTO= taskService.findById(id);
       return ResponseEntity.ok(new ResponseWrapper("Task is retrieved", taskDTO, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO){
        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Task is created", HttpStatus.OK));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task is updated", HttpStatus.OK));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("taskId") Long id){
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Task is deleted", HttpStatus.OK));
    }

    @GetMapping("/employee/pending-tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTasks(){
       List<TaskDTO> pendingTaskDTO= taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
       return ResponseEntity.ok(new ResponseWrapper("Pending tasks retrieved", pendingTaskDTO, HttpStatus.OK));
    }

    @PutMapping("employee/update")
    public ResponseEntity<ResponseWrapper> employeeUpdateTasks(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task updated", HttpStatus.OK));
    }

    @GetMapping("/employee/archive")
    public ResponseEntity<ResponseWrapper> employeeArchiveTasks(){
        List<TaskDTO> taskDTOList =taskService.listAllTasksByStatus(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Archived Tasks retrieved",taskDTOList, HttpStatus.OK));
    }

}
