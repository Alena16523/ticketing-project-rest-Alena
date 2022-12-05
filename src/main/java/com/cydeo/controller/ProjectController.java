package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    public final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getProjects(){
        List<ProjectDTO> projectDTOList=projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("Projects are successfully retrieved", projectDTOList, HttpStatus.OK));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable("code") String code){
        ProjectDTO projectDTO=projectService.getByProjectCode(code);
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved", projectDTO, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){
        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Project is created", HttpStatus.CREATED));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO){
        projectService.update(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is updated", HttpStatus.OK));
    }

    @DeleteMapping("/{projectcode}")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectcode") String projectCode){
      projectService.delete(projectCode);
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Project is deleted", HttpStatus.OK));
    }

    @GetMapping("/manager/project-status")
    public ResponseEntity<ResponseWrapper> getProjectByManager( ){
        List<ProjectDTO> projectDTOList=projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("Projects are successfully retrieved", projectDTOList, HttpStatus.OK));

    }

    @PutMapping ("/manager/complete/{projectcode}")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectcode") String projectCode){
        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project is completed", HttpStatus.OK));
    }


}
