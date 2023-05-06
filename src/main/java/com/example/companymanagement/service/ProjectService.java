package com.example.companymanagement.service;

import com.example.companymanagement.domain.Project;
import com.example.companymanagement.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectService {
    private final CleanService cleanService;
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository, CleanService cleanService) {
        this.projectRepository = projectRepository;
        this.cleanService = cleanService;
    }

    public void deleteById(Integer id){
        cleanService.deleteConnections("project_programmer", id);
        cleanService.deleteConnections("project_manager", id);
        projectRepository.deleteById(id);
    }

    public void update(Project project){
        cleanService.deleteConnections("project_programmer", project.getId());
        cleanService.deleteConnections("project_manager", project.getId());
        projectRepository.save(project);
    }
}
