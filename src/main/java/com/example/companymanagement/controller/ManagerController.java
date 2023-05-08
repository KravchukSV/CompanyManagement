package com.example.companymanagement.controller;

import com.example.companymanagement.domain.Manager;
import com.example.companymanagement.exception.BadRequestException;
import com.example.companymanagement.repository.ManagerRepository;
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

@Tag(name = "Manager", description = "CRUD operations with Manager")
@RequestMapping("/managers")
@RestController
public class ManagerController{

    private final ManagerRepository managerRepository;

    public ManagerController(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Operation(summary = "Get by all", description = "Returns all managers from the database")
    @ApiResponse(responseCode = "200", description = "All ok")
    @GetMapping
    Collection<Manager> findAll(){
        Collection<Manager> managers = (Collection<Manager>) managerRepository.findAll();
        return managers;
    }

    @Operation(summary = "Get by ID", description = "Returns manager data by id")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @GetMapping("/{id}")
    ResponseEntity<Manager> findById(@PathVariable Integer id){
        Optional<Manager> manager =  managerRepository.findById(id);
        if(manager.isEmpty()){
            throw new NotFoundException("Manager not found with id:" + id);
        }
        return ResponseEntity.ok(manager.get());
    }

    @Operation(summary = "Delete by ID", description = "Deletes the manager by the given id")
    @ApiResponse(responseCode = "204", description = "no content")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Integer id){
        if(!managerRepository.existsById(id)){
            throw new NotFoundException("Manager not found with id:" + id);
        }
        managerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update by ID", description = "Making changes to the manager according to the specified id")
    @ApiResponse(responseCode = "200", description = "ok")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Manager manager){
        if(id == null || !id.equals(manager.getId()))
            throw new BadRequestException("Bad request: invalid data");
        if(!managerRepository.existsById(id))
            throw new NotFoundException("Manager not found with id:" + id);
        managerRepository.save(manager);
        return ResponseEntity.ok().body(manager);
    }

    @Operation(summary = "Create", description = "Create new manager")
    @ApiResponse(responseCode = "200", description = "ok")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @PostMapping
    public ResponseEntity<Manager> create(@RequestBody Manager manager){
        if(manager == null || manager.getId() != null){
            throw new BadRequestException("Bad request: invalid data");
        }
        Manager newManager = managerRepository.save(manager);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newManager.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newManager);
    }
}
