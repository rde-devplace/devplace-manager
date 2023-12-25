package com.mibottle.manager.repository;

import com.mibottle.manager.model.IdeConfigInfo;
import com.mibottle.manager.model.IdeDomainInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IdeDomainInfoRepository extends JpaRepository<IdeDomainInfo, String> {
}
