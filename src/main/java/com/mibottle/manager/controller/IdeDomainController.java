package com.mibottle.manager.controller;

import com.mibottle.manager.model.IdeDomainInfo;
import com.mibottle.manager.service.IdeDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;

@RestController
@RequestMapping("/ide-manager/api/domains")
@Tag(name = "IdeDomainController", description = "IDE VSCODE & CLI 접속을 위한 Remote Development Env. 접속 도메일을 관리")
public class IdeDomainController {

    private final IdeDomainService ideDomainService;

    @Autowired
    public IdeDomainController(IdeDomainService ideDomainService) {
        this.ideDomainService = ideDomainService;
    }

    @PostMapping
    @Operation(summary = "새 도메인 생성", description = "새로운 IDE 도메인 정보를 생성합니다.")
    public ResponseEntity<IdeDomainInfo> createDomain(@RequestBody IdeDomainInfo ideDomainInfo) {
        IdeDomainInfo createdDomain = ideDomainService.createDomain(ideDomainInfo);
        return ResponseEntity.ok(createdDomain);
    }

    @GetMapping("/{name}")
    @Operation(summary = "도메인 정보 조회", description = "주어진 이름으로 도메인 정보를 조회합니다.")
    public ResponseEntity<IdeDomainInfo> getDomainByName(@PathVariable String name) {
        Optional<IdeDomainInfo> domainInfo = ideDomainService.getDomainByName(name);
        return domainInfo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{name}")
    @Operation(summary = "도메인 정보 업데이트", description = "주어진 이름의 도메인 정보를 업데이트합니다.")
    public ResponseEntity<IdeDomainInfo> updateDomain(@PathVariable String name, @RequestBody IdeDomainInfo ideDomainInfo) {
        try {
            IdeDomainInfo updatedDomain = ideDomainService.updateDomain(name, ideDomainInfo);
            return ResponseEntity.ok(updatedDomain);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "도메인 삭제", description = "주어진 이름의 도메인 정보를 삭제합니다.")
    public ResponseEntity<Void> deleteDomain(@PathVariable String name) {
        ideDomainService.deleteDomain(name);
        return ResponseEntity.noContent().build();
    }
}

