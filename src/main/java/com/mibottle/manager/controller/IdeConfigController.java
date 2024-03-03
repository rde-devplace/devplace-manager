package com.mibottle.manager.controller;

import com.mibottle.manager.customresource.IdeConfig;
import com.mibottle.manager.customresource.IdeConfigSpec;
import com.mibottle.manager.model.*;
import com.mibottle.manager.service.IdeConfigService;
import com.mibottle.manager.service.IdeDomainService;
import com.mibottle.manager.service.IdeWorkspaceService;
import com.mibottle.manager.util.SendWithRequestHeader;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ide-manager/api/ide-configs")
@Tag(name = "IdeConfigController", description = "IDE VSCODE & CLI 컨테이너를 생성하고 관리하는 역할 수행")
public class IdeConfigController {

    private final IdeConfigService ideConfigService;

    private final IdeDomainService ideDomainService;
    private final IdeWorkspaceService ideWorkspaceService;

    private final SendWithRequestHeader sendWithRequestHeader;

    private final String DELETE_PATH = "ide-manager";

    @Autowired
    public IdeConfigController(IdeConfigService ideConfigService, IdeDomainService ideDomainService, IdeWorkspaceService ideWorkspaceService, SendWithRequestHeader sendWithRequestHeader) {
        this.ideConfigService = ideConfigService;
        this.ideDomainService = ideDomainService;
        this.ideWorkspaceService = ideWorkspaceService;
        this.sendWithRequestHeader = sendWithRequestHeader;
    }

    @PostMapping("/custom-resource")
    @Operation(summary = "ConfigMap 생성", description = "IdeConfig 생성")
    public ResponseEntity<IdeConfigInfo> createConfig(HttpServletRequest request,
                                                      @RequestParam String workspaceName,
                                                      @RequestParam String domainName,
                                                      @RequestParam String namespace,
                                                      @RequestParam String name,
                                                      @RequestBody IdeConfigSpec ideConfigSpec) {

        Optional<IdeDomainInfo> domainOpt = ideDomainService.getDomainByName(domainName);
        if(domainOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String requestParams = String.format("namespace=%s&name=%s", namespace, name);
        ResponseEntity<String> response = sendWithRequestHeader.sendForStringResponse(
                domainOpt.get(), requestParams, HttpMethod.POST, request, DELETE_PATH, ideConfigSpec, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Optional<IdeWorkspace> workspaceOpt = ideWorkspaceService.getIdeWorkspaceByName(workspaceName);
            // ideConfigInfo Name 정의
            String ideConfigInfoName = IdeCommon.getIdeConfigInfoName(ideConfigSpec.getUserName(), domainName, workspaceName);
            if (domainOpt.isEmpty() || workspaceOpt.isEmpty()) {
                String errorMessage = "";
                if (domainOpt.isEmpty()) {
                    errorMessage += "Domain with name '" + domainName + "' not found. ";
                }
                if (workspaceOpt.isEmpty()) {
                    errorMessage += "Workspace with name '" + workspaceName + "' not found.";
                }
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(IdeConfigInfo.builder()
                                .name(ideConfigInfoName)
                                .status(errorMessage)
                                .build());
            }

            IdeConfigInfo ideConfigInfo = IdeConfigInfo.builder()
                    .name(ideConfigInfoName)
                    .domainInfo(domainOpt.get())
                    .workspace(workspaceOpt.get())
                    .namespace(namespace)
                    .userName(name)
                    .ideConfigCrdName(name + IdeCommon.IDECONFIG_POSTFIX)
                    .status("Created")
                    .build();

            IdeConfigInfo createdConfig = ideConfigService.createConfigInfo(ideConfigInfo);

            return ResponseEntity.ok(createdConfig);
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    @GetMapping("/custom-resource")
    @Operation(summary = "IdeConfig 목록 조회", description = "IdeConfig 목록을 조회합니다.")
    public ResponseEntity<List<IdeConfig>> getConfigs(HttpServletRequest request,
                                                      @RequestParam String domainName,
                                                      @RequestParam String namespace,
                                                      @RequestParam(required = false) String name) {
        String requestParams;
        if (name == null || name.isEmpty()) {
            requestParams = String.format("namespace=%s", namespace);
        } else {
            requestParams = String.format("namespace=%s&name=%s", namespace, name);
        }
        Optional<IdeDomainInfo> domainOpt = ideDomainService.getDomainByName(domainName);
        if(domainOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ResponseEntity<List<IdeConfig>> response = sendWithRequestHeader.sendForGenericResponse(
                domainOpt.get(),
                requestParams,
                HttpMethod.GET,
                request,
                DELETE_PATH,
                null, // GET 요청의 경우, 바디는 null
                new ParameterizedTypeReference<List<IdeConfig>>() {});

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    @GetMapping("/custom-resource/spec")
    @Operation(summary = "IdeConfig 목록 조회", description = "IdeConfig 목록을 조회합니다.")
    public ResponseEntity<List<IdeConfigSpec>> getConfigSpec(HttpServletRequest request,
                                                      @RequestParam String domainName,
                                                      @RequestParam String namespace,
                                                      @RequestParam(required = false) String name) {
        String requestParams;
        if (name == null || name.isEmpty()) {
            requestParams = String.format("namespace=%s", namespace);
        } else {
            requestParams = String.format("namespace=%s&name=%s", namespace, name);
        }
        Optional<IdeDomainInfo> domainOpt = ideDomainService.getDomainByName(domainName);
        if(domainOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ResponseEntity<List<IdeConfigSpec>> response = sendWithRequestHeader.sendForGenericResponse(
                domainOpt.get(),
                requestParams,
                HttpMethod.GET,
                request,
                DELETE_PATH,
                null, // GET 요청의 경우, 바디는 null
                new ParameterizedTypeReference<List<IdeConfigSpec>>() {});

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    @PutMapping("/custom-resource")
    @Operation(summary = "IdeConfig CR 업데이트", description = "새로운 IDE Config CR을 업데이트 합니다.")
    public ResponseEntity<IdeConfigInfo> updateConfig(HttpServletRequest request,
                                                      @RequestParam String workspaceName,
                                                      @RequestParam String domainName,
                                                      @RequestParam String userName,
                                                      @RequestBody IdeConfigSpec ideConfigSpec) {

        String ideConfigInfoName = IdeCommon.getIdeConfigInfoName(userName, domainName, workspaceName);
        IdeConfigInfo ideConfigInfo = ideConfigService.findConfigInfoByName(ideConfigInfoName).orElse(null);
        String requestParams = String.format("namespace=%s&name=%s", ideConfigInfo.getNamespace(), ideConfigInfo.getUserName());

        // 1. ideConfigInfoName으로 IdeConfigInfo를 조회한다.
        Optional<IdeConfigInfo> opt = ideConfigService.findConfigInfoByName(ideConfigInfoName);
        if(opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        IdeDomainInfo domainInfo = opt.get().getDomainInfo();

        // 2. Kubernetes에 생성되어 있는 IdeConfig CR 정보를 각 Cluster의 Operator로 부터 조회해서 가져온다.
        ResponseEntity<List<IdeConfig>> responseIdeConfig = sendWithRequestHeader.sendForGenericResponse(
                domainInfo,
                requestParams,
                HttpMethod.GET,
                request,
                DELETE_PATH,
                null, // GET 요청의 경우, 바디는 null
                new ParameterizedTypeReference<List<IdeConfig>>() {});

        IdeConfig existingConfig = responseIdeConfig.getBody().get(0);

        if (existingConfig == null) {
            return ResponseEntity.notFound().build();
        }


        //3. IdeConfigSpec에서 비어 있지 않은 값만을 사용하여 기존 IdeConfigInfo를 업데이트한다.
        IdeConfigSpec existingConfigSpec = updateIdeConfigInfoFromSpec(existingConfig.getSpec(), ideConfigSpec);

        // Remote Cluster에 업데이트 요청
        ResponseEntity<String> response = sendWithRequestHeader.sendForStringResponse(
                domainInfo,
                requestParams,
                HttpMethod.PUT,
                request,
                DELETE_PATH,
                existingConfigSpec,
                IdeConfigSpec.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            IdeConfigInfo info = IdeConfigInfo.builder()
                    .name(ideConfigInfoName)
                    .status("update").build();
            IdeConfigInfo updatedConfig = ideConfigService.updateConfigInfo(ideConfigInfoName, info);
            return ResponseEntity.ok(updatedConfig);
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    @DeleteMapping("/custom-resource")
    @Operation(summary = "IdeConfig CR 삭제", description = "지정된 IDE Config CR을 삭제합니다.")
    public ResponseEntity<String> deleteIdeConfig(HttpServletRequest request,
                                                  @RequestParam String workspaceName,
                                                  @RequestParam String domainName,
                                                  @RequestParam String userName) {

        String ideConfigInfoName = IdeCommon.getIdeConfigInfoName(userName, domainName, workspaceName);
        IdeConfigInfo ideConfigInfo = ideConfigService.findConfigInfoByName(ideConfigInfoName).orElse(null);
        String requestParams = String.format("namespace=%s&name=%s", ideConfigInfo.getNamespace(), ideConfigInfo.getUserName());

        // 1. ideConfigInfoName으로 IdeConfigInfo를 조회한다.
        Optional<IdeConfigInfo> opt = ideConfigService.findConfigInfoByName(ideConfigInfoName);
        if(opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        IdeDomainInfo domainInfo = opt.get().getDomainInfo();

        // 2. Remote Cluster에 삭제 요청을 보낸다.
        ResponseEntity<String> response = sendWithRequestHeader.sendForStringResponse(
                domainInfo,
                requestParams,
                HttpMethod.DELETE,
                request,
                DELETE_PATH,
                null, // DELETE 요청의 경우, 바디는 null
                String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // 삭제 성공 로직 (예: 로컬 DB에서 IdeConfigInfo 삭제)
            ideConfigService.deleteConfigInfo(ideConfigInfoName);
            return ResponseEntity.ok("IdeConfig deleted successfully.");
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    private IdeConfigSpec updateIdeConfigInfoFromSpec(IdeConfigSpec existingConfigSpec, IdeConfigSpec ideConfigSpec) {
        if (ideConfigSpec.getUserName() != null && !ideConfigSpec.getUserName().isEmpty()) {
            existingConfigSpec.setUserName(ideConfigSpec.getUserName());
        }
        if (ideConfigSpec.getServiceTypes() != null && !ideConfigSpec.getServiceTypes().isEmpty()) {
            existingConfigSpec.setServiceTypes(ideConfigSpec.getServiceTypes());
        }
        if (ideConfigSpec.getWebssh() != null) {
            existingConfigSpec.setWebssh(ideConfigSpec.getWebssh());
        }
        if (ideConfigSpec.getVscode() != null) {
            existingConfigSpec.setVscode(ideConfigSpec.getVscode());
        }
        if (ideConfigSpec.getPortList() != null && !ideConfigSpec.getPortList().isEmpty()) {
            existingConfigSpec.setPortList(ideConfigSpec.getPortList());
        }
        if (ideConfigSpec.getInfrastructureSize() != null) {
            existingConfigSpec.setInfrastructureSize(ideConfigSpec.getInfrastructureSize());
        }
        if (ideConfigSpec.getReplicas() > 0) {
            existingConfigSpec.setReplicas(ideConfigSpec.getReplicas());
        }

        return existingConfigSpec;
    }


    /*

    @PostMapping("/info")
    @Operation(summary = "새로운 설정 정보 생성", description = "새로운 IDE Config CR을 생성합니다.")
    public ResponseEntity<IdeConfigInfo> createConfigInfo(@RequestBody IdeConfigInfo ideConfigInfo) {
        IdeConfigInfo createdConfig = ideConfigService.createConfigInfo(ideConfigInfo);
        return ResponseEntity.ok(createdConfig);
    }

    @DeleteMapping("/info/{name}")
    @Operation(summary = "설정 정보 삭제", description = "주어진 이름의 설정 정보를 삭제합니다.")
    public ResponseEntity<Void> deleteConfigInfo(@PathVariable String name) {
        ideConfigService.deleteConfigInfo(name);
        return ResponseEntity.noContent().build();
    }

     */

    @GetMapping("/config-infos")
    @Operation(summary = "도메인 정보 조회", description = "주어진 이름으로 도메인 설정 정보를 조회합니다.")
    public ResponseEntity<IdeConfigInfoDTO> getConfigInfo(
            @RequestParam String workspaceName,
            @RequestParam String domainName,
            @RequestParam String userName
    ) {
        String ideConfigInfoName = IdeCommon.getIdeConfigInfoName(userName, domainName, workspaceName);
        Optional<IdeConfigInfo> configInfo = ideConfigService.findConfigInfoByName(ideConfigInfoName);

        return configInfo
                .map(IdeConfigInfoDTO::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/domains/{name}")
    @Operation(summary = "도메인 정보 조회", description = "주어진 이름으로 도메인 설정 정보를 조회합니다.")
    public ResponseEntity<IdeDomainInfo> getDomainInfoByName(@PathVariable String name) {
        Optional<IdeDomainInfo> domainInfo = ideConfigService.findDomainInfoByName(name);
        return domainInfo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/workspaces")
    @Operation(summary = "워크스페이스 정보 조회", description = "주어진 이름으로 워크스페이스 설정 정보를 조회합니다.")
    public ResponseEntity<IdeWorkspace> getWorkspaceInfoByName(@RequestParam String name) {
        Optional<IdeWorkspace> workspaceInfo = ideConfigService.findWorkspaceInfoByName(name);
        return workspaceInfo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/config-infos")
    @Operation(summary = "설정 정보 업데이트", description = "주어진 이름의 설정 정보를 업데이트합니다.")
    public ResponseEntity<IdeConfigInfo> updateConfigInfo(@RequestParam String name, @RequestBody IdeConfigInfo ideConfigInfo) {
        try {
            IdeConfigInfo updatedConfig = ideConfigService.updateConfigInfo(name, ideConfigInfo);
            return ResponseEntity.ok(updatedConfig);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

