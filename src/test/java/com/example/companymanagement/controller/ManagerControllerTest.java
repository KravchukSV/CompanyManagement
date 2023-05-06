package com.example.companymanagement.controller;

import com.example.companymanagement.domain.Kind;
import com.example.companymanagement.domain.Level;
import com.example.companymanagement.domain.Manager;
import com.example.companymanagement.domain.Programmer;
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
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ManagerController managerController;

    @Test
    void contextLoads() {
        assertThat(managerController);
    }
    @Test
    void findAll() throws Exception{
        this.mockMvc.perform(get("/managers")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception{
        this.mockMvc
                .perform(get("/managers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("BOSS"))
                .andExpect(jsonPath("$.level").value(Level.SENIOR.name()))
                .andExpect(jsonPath("$.kind").value(Kind.DEVOPS.name()));

    }

    @Test
    void deleteById() throws Exception{
        this.mockMvc
                .perform(delete("/managers/{id}", 1) )
                .andExpect(status().isNoContent());

    }

    @Test
    void update() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Manager manager = new Manager();
        manager.setId(1);
        manager.setName("Bob");
        manager.setLevel(Level.SENIOR);
        manager.setKind(Kind.DEVOPS);

        this.mockMvc
                .perform(put("/managers/{id}", 1)
                        .content(mapper.writeValueAsString(manager))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.level").value(Level.SENIOR.name()))
                .andExpect(jsonPath("$.kind").value(Kind.DEVOPS.name()));
    }

   @Test
   @Sql(value = {"/create-after.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
   public void create() throws Exception
   {
       ObjectMapper mapper = new ObjectMapper();

       Programmer programmer = new Programmer();
       programmer.setName("Bob");
       programmer.setLevel(Level.SENIOR);
       programmer.setKind(Kind.DEVOPS);


       this.mockMvc.perform( post("/managers")
                       .content(mapper.writeValueAsString(programmer))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.level").value(Level.SENIOR.name()))
               .andExpect(jsonPath("$.kind").value(Kind.DEVOPS.name()));
   }
}