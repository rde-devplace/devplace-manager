package com.mibottle.manager.service;

import com.mibottle.manager.model.IdeDomainInfo;
import com.mibottle.manager.model.IdeWorkspace;
import com.mibottle.manager.repository.IdeDomainInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 서비스 클래스는 'IdeDomainInfo' 엔티티에 대한 CRUD 작업을 처리합니다.
 * 이 클래스는 'IdeDomainRepository'를 사용하여 데이터베이스 작업을 수행합니다.
 */
@Service
public class IdeDomainService {

    private final IdeDomainInfoRepository ideDomainRepository;

    /**
     * 생성자는 의존성 주입을 위해 'IdeDomainRepository' 인터페이스를 주입받습니다.
     *
     * @param ideDomainRepository 'IdeDomainInfo' 엔티티에 대한 데이터베이스 작업을 수행하는 레포지토리
     */
    @Autowired
    public IdeDomainService(IdeDomainInfoRepository ideDomainRepository) {
        this.ideDomainRepository = ideDomainRepository;
    }

    /**
     * 새로운 'IdeDomainInfo' 객체를 생성하고 데이터베이스에 저장합니다.
     *
     * @param ideDomainInfo 데이터베이스에 저장할 'IdeDomainInfo' 객체
     * @return 저장된 'IdeDomainInfo' 객체
     */
    @Transactional
    public IdeDomainInfo createDomain(IdeDomainInfo ideDomainInfo) {
        return ideDomainRepository.save(ideDomainInfo);
    }

    /**
     * 주어진 이름을 사용하여 'IdeDomainInfo' 객체를 조회합니다.
     *
     * @param name 조회할 도메인의 이름
     * @return 주어진 이름과 일치하는 'IdeDomainInfo' 객체, 존재하지 않을 경우 Optional.empty()
     */
    public Optional<IdeDomainInfo> getDomainByName(String name) {
        return ideDomainRepository.findById(name);
    }

    /**
     * 주어진 이름에 해당하는 'IdeDomainInfo' 객체를 업데이트합니다.
     * 이름에 해당하는 객체가 없는 경우 예외를 발생시킵니다.
     *
     * @param name 업데이트할 도메인의 이름
     * @param ideDomainInfo 업데이트할 정보가 담긴 'IdeDomainInfo' 객체
     * @return 업데이트된 'IdeDomainInfo' 객체
     */
    @Transactional
    public IdeDomainInfo updateDomain(String name, IdeDomainInfo ideDomainInfo) {
        IdeDomainInfo existingDomain = ideDomainRepository.findById(name)
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        // domainURL이 null이 아니고 빈 문자열이 아닐 경우에만 업데이트
        if (ideDomainInfo.getDomainURL() != null && !ideDomainInfo.getDomainURL().isEmpty()) {
            existingDomain.setDomainURL(ideDomainInfo.getDomainURL());
        }

        // clusterName이 null이 아니고 빈 문자열이 아닐 경우에만 업데이트
        if (ideDomainInfo.getClusterName() != null && !ideDomainInfo.getClusterName().isEmpty()) {
            existingDomain.setClusterName(ideDomainInfo.getClusterName());
        }

        // description이 null이 아니고 빈 문자열이 아닐 경우에만 업데이트
        if (ideDomainInfo.getDescription() != null && !ideDomainInfo.getDescription().isEmpty()) {
            existingDomain.setDescription(ideDomainInfo.getDescription());
        }

        return ideDomainRepository.save(existingDomain);
    }



    /**
     * 주어진 이름에 해당하는 'IdeDomainInfo' 객체를 데이터베이스에서 삭제합니다.
     *
     * @param name 삭제할 도메인의 이름
     */
    @Transactional
    public void deleteDomain(String name) {
        ideDomainRepository.deleteById(name);
    }
}
