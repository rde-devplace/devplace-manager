package com.mibottle.manager.repository;

import com.mibottle.manager.model.IdeConfigInfo;
import com.mibottle.manager.model.IdeWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IdeConfigInfoRepository extends JpaRepository<IdeConfigInfo, String> {
    Optional<IdeConfigInfo> findByName(String name);
}
