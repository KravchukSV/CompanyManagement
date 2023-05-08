package com.example.companymanagement.controller;

import com.example.companymanagement.domain.Project;
import com.example.companymanagement.exception.BadRequestException;
import com.example.companymanagement.repository.ProjectRepository;
import com.example.companymanagement.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.webjars.NotFoundException;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@Tag(name = "Project", description = "CRUD operations with Project")
@RequestMapping("/projects")
@RestController
public class ProjectController{
    private final ProjectService projectServer;

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository, ProjectService projectServer) {
        this.projectRepository = projectRepository;
        this.projectServer = projectServer;
    }

    @Operation(summary = "Get by all", description = "Returns all projects from the database")
    @ApiResponse(responseCode = "200", description = "All ok")
    @GetMapping
    public Collection<Project> findAll(){
        Collection<Project> projects = (Collection<Project>) projectRepository.findAll();
        return projects;
    }

    @Operation(summary = "Get by ID", description = "Returns project data by id")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable Integer id){
        Optional<Project> project =  projectRepository.findById(id);
        if(project.isEmpty()){
            throw new NotFoundException("Project not found with id:" + id);
        }
        return ResponseEntity.ok(project.get());
    }

    @Operation(summary = "Delete by ID", description = "Deletes the project by the given id")
    @ApiResponse(responseCode = "204", description = "no content")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id){
        if(!projectRepository.existsById(id)){
            throw new NotFoundException("Project not found with id:" + id);
        }
        projectServer.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update by ID", description = "Making changes to the project according to the specified id")
    @ApiResponse(responseCode = "200", description = "ok")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Project project){
        if(id == null || !id.equals(project.getId()))
            throw new BadRequestException("Bad request: invalid data");
        if(!projectRepository.existsById(id))
            throw new NotFoundException("Project not found with id:" + id);
        projectServer.update(project);
        return ResponseEntity.ok().body(project);
    }

    @Operation(summary = "Create", description = "Create new project")
    @ApiResponse(responseCode = "201", description = "created")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project){
        if(project == null || project.getId() != null){
            throw new BadRequestException("Bad request: invalid data");
        }
        Project newProject = projectRepository.save(project);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newProject.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newProject);
    }
}
