package com.mibottle.manager.repository;

import com.mibottle.manager.model.IdeWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeWorkspaceRepository extends JpaRepository<IdeWorkspace, String> {
    List<IdeWorkspace> findByProjectName(String projectName);
}
