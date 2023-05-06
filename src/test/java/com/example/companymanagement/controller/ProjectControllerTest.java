package com.example.companymanagement.controller;

import com.example.companymanagement.domain.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectController projectController;

    @Test
    void contextLoads() {
        assertThat(projectController);
    }
    @Test
    void findAll() throws Exception{
        this.mockMvc.perform(get("/projects")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception{
        this.mockMvc
                .perform(get("/projects/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("EASY PROJECT"));
    }

    @Test
    void deleteById() throws Exception{
        this.mockMvc
                .perform(delete("/projects/{id}", 1) )
                .andExpect(status().isNoContent());

    }

    @Test
    void update() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Project project = new Project();
        project.setId(1);
        project.setName("TEST PROJECT");

        this.mockMvc
                .perform(put("/projects/{id}", 1)
                        .content(mapper.writeValueAsString(project))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TEST PROJECT"));
    }

   @Test
   @Sql(value = {"/create-after.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
   public void create() throws Exception
   {
       ObjectMapper mapper = new ObjectMapper();

       Project project = new Project();
       project.setName("TEST PROJECT");

       this.mockMvc.perform( post("/projects")
                       .content(mapper.writeValueAsString(project))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").exists());
   }
}