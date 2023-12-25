package com.mibottle.manager.service;

import com.mibottle.manager.model.IdeConfigInfo;
import com.mibottle.manager.model.IdeDomainInfo;
import com.mibottle.manager.model.IdeWorkspace;
import com.mibottle.manager.repository.IdeConfigInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;


@Service
public class IdeConfigService {

    private final IdeConfigInfoRepository ideConfigInfoRepository;

    @Autowired
    public IdeConfigService(IdeConfigInfoRepository ideConfigInfoRepository) {
        this.ideConfigInfoRepository = ideConfigInfoRepository;
    }

    public Optional<IdeConfigInfo> findConfigInfoByName(String name) {
        return ideConfigInfoRepository.findById(name);
    }
    /**
     * 주어진 이름으로 IdeConfigInfo를 찾아 해당 IdeDomainInfo를 반환합니다.
     *
     * @param name 찾고자 하는 IdeConfigInfo의 이름
     * @return 찾은 IdeDomainInfo, 존재하지 않을 경우 Optional.empty()
     */
    public Optional<IdeDomainInfo> findDomainInfoByName(String name) {
        return ideConfigInfoRepository.findByName(name)
                .map(IdeConfigInfo::getDomainInfo);
    }

    /**
     * 주어진 이름으로 IdeConfigInfo를 찾아 해당 IdeWorkspace를 반환합니다.
     *
     * @param name 찾고자 하는 IdeConfigInfo의 이름
     * @return 찾은 IdeWorkspace, 존재하지 않을 경우 Optional.empty()
     */
    public Optional<IdeWorkspace> findWorkspaceInfoByName(String name) {
        return ideConfigInfoRepository.findByName(name)
                .map(IdeConfigInfo::getWorkspace);
    }

    /**
     * 새로운 IdeConfigInfo 객체를 생성하고 데이터베이스에 저장합니다.
     *
     * @param ideConfigInfo 데이터베이스에 저장할 IdeConfigInfo 객체
     * @return 저장된 IdeConfigInfo 객체
     */
    @Transactional
    public IdeConfigInfo createConfigInfo(IdeConfigInfo ideConfigInfo) {
        return ideConfigInfoRepository.save(ideConfigInfo);
    }

    /**
     * 주어진 이름에 해당하는 IdeConfigInfo 객체를 데이터베이스에서 삭제합니다.
     *
     * @param name 삭제할 IdeConfigInfo의 이름
     */
    @Transactional
    public void deleteConfigInfo(String name) {
        ideConfigInfoRepository.deleteById(name);
    }

    /**
     * 주어진 이름에 해당하는 IdeConfigInfo 객체를 업데이트합니다.
     * 값이 null이 아니고, 빈 문자열이 아닌 경우에만 해당 필드를 업데이트합니다.
     *
     * @param name 업데이트할 IdeConfigInfo의 이름
     * @param ideConfigInfo 업데이트할 정보가 담긴 IdeConfigInfo 객체
     * @return 업데이트된 IdeConfigInfo 객체, 이름에 해당하는 객체가 없는 경우 예외 발생
     */
    @Transactional
    public IdeConfigInfo updateConfigInfo(String name, IdeConfigInfo ideConfigInfo) {
        IdeConfigInfo existingConfig = ideConfigInfoRepository.findById(name)
                .orElseThrow(() -> new RuntimeException("ConfigInfo not found"));

        if (ideConfigInfo.getIdeConfigCrdName() != null && !ideConfigInfo.getIdeConfigCrdName().isEmpty()) {
            existingConfig.setIdeConfigCrdName(ideConfigInfo.getIdeConfigCrdName());
        }
        if (ideConfigInfo.getStatus() != null && !ideConfigInfo.getStatus().isEmpty()) {
            existingConfig.setStatus(ideConfigInfo.getStatus());
        }
        if (ideConfigInfo.getDomainInfo() != null) {
            existingConfig.setDomainInfo(ideConfigInfo.getDomainInfo());
        }
        if (ideConfigInfo.getWorkspace() != null) {
            existingConfig.setWorkspace(ideConfigInfo.getWorkspace());
        }

        return ideConfigInfoRepository.save(existingConfig);
    }
}


