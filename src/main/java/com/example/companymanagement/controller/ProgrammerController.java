package com.example.companymanagement.controller;

import com.example.companymanagement.domain.Programmer;
import com.example.companymanagement.exception.BadRequestException;
import com.example.companymanagement.repository.ProgrammerRepository;
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

@Tag(name = "Programmer", description = "CRUD operations with Programmer")
@RequestMapping("/programmers")
@RestController
public class ProgrammerController{

    private final ProgrammerRepository programmerRepository;

    public ProgrammerController(ProgrammerRepository programmerRepository) {
        this.programmerRepository = programmerRepository;
    }

    @Operation(summary = "Get by all", description = "Returns all programmers from the database")
    @ApiResponse(responseCode = "200", description = "All ok")
    @GetMapping
    Collection<Programmer> findAll(){
        Collection<Programmer> programmers = (Collection<Programmer>) programmerRepository.findAll();
        return programmers;
    }

    @Operation(summary = "Get by ID", description = "Returns programmer data by id")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @GetMapping("/{id}")
    ResponseEntity<Programmer> findById(@PathVariable Integer id){
        Optional<Programmer> programmer =  programmerRepository.findById(id);
        if(programmer.isEmpty()){
            throw new NotFoundException("Programmer not found with id:" + id);
        }
        return ResponseEntity.ok(programmer.get());
    }

    @Operation(summary = "Delete by ID", description = "Deletes the programmer by the given id")
    @ApiResponse(responseCode = "204", description = "no content")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Integer id){
        if(!programmerRepository.existsById(id)){
            throw new NotFoundException("Programmer not found with id:" + id);
        }
        programmerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update by ID", description = "Making changes to the programmer according to the specified id")
    @ApiResponse(responseCode = "200", description = "ok")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Programmer programmer){
        if(id == null || !id.equals(programmer.getId()))
            throw new BadRequestException("Bad request: invalid data");
        if(!programmerRepository.existsById(id))
            throw new NotFoundException("Project not found with id:" + id);
        programmerRepository.save(programmer);
        return ResponseEntity.ok().body(programmer);
    }

    @Operation(summary = "Create", description = "Create new programmer")
    @ApiResponse(responseCode = "200", description = "ok")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @PostMapping
    public ResponseEntity<Programmer> create(@RequestBody Programmer programmer){
        if(programmer == null || programmer.getId() != null){
            throw new BadRequestException("Bad request: invalid data");
        }
        Programmer newProgrammer = programmerRepository.save(programmer);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newProgrammer.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newProgrammer);
    }
}