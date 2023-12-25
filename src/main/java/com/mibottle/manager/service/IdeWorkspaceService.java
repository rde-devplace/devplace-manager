package com.mibottle.manager.service;

import com.mibottle.manager.model.IdeWorkspace;
import com.mibottle.manager.repository.IdeWorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IdeWorkspaceService {

    private final IdeWorkspaceRepository repository;

    @Autowired
    public IdeWorkspaceService(IdeWorkspaceRepository repository) {
        this.repository = repository;
    }

    // IdeWorkspace 생성
    public IdeWorkspace createIdeWorkspace(IdeWorkspace ideWorkspace) {
        return repository.save(ideWorkspace);
    }

    // 모든 IdeWorkspace 검색
    public List<IdeWorkspace> getAllIdeWorkspaces() {
        return repository.findAll();
    }

    // name 기반으로 IdeWorkspace 검색
    public Optional<IdeWorkspace> getIdeWorkspaceByName(String name) {
        Optional<IdeWorkspace> ideWorkspace = repository.findById(name);
        return ideWorkspace;
    }


    // IdeWorkspace 수정
    public IdeWorkspace updateIdeWorkspace(String name, IdeWorkspace updatedIdeWorkspace) {
        return repository.findById(name)
                .map(existingWorkspace -> {

                    if (updatedIdeWorkspace.getDescription() != null && !updatedIdeWorkspace.getDescription().isEmpty()) {
                        existingWorkspace.setDescription(updatedIdeWorkspace.getDescription());
                    }

                    return repository.save(existingWorkspace);
                }).orElse(null);
    }

    // projectName을 기준으로 IdeWorkspace 검색
    public List<IdeWorkspace> findByProjectName(String projectName) {
        return repository.findByProjectName(projectName);
    }

    // IdeWorkspace 삭제
    public void deleteIdeWorkspace(String name) {
        repository.deleteById(name);
    }
}

