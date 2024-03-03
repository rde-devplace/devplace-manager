package com.mibottle.manager.controller;

import com.mibottle.manager.model.IdeWorkspace;
import com.mibottle.manager.service.IdeConfigService;
import com.mibottle.manager.service.IdeWorkspaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ide-manager/api/workspaces")
@Tag(name = "IdeWorkspaceController", description = "IDE Container를 생성하고 관리하기 위한 Developer를 위한 Workspace를 생성 및 관리")
public class IdeWorkspaceController {

    private final IdeWorkspaceService ideWorkspaceService;
    private final IdeConfigService ideConfigService;

    @Autowired
    public IdeWorkspaceController(IdeWorkspaceService ideWorkspaceService, IdeConfigService ideConfigService) {
        this.ideWorkspaceService = ideWorkspaceService;
        this.ideConfigService = ideConfigService;
    }


    @GetMapping("/{name}")
    @Operation(summary = "특정 Workspace 검색", description = "주어진 이름으로 특정 Workspace 정보를 조회합니다.")
    public ResponseEntity<IdeWorkspace> getIdeWorkspaceByName(
            @Parameter(description = "검색할 Workspace의 이름") @PathVariable String name) {
        Optional<IdeWorkspace> workspace = ideWorkspaceService.getIdeWorkspaceByName(name);
        if (workspace.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(workspace.get(), HttpStatus.OK);
    }


    // POST: IdeWorkspace 생성
    @PostMapping
    @Operation(summary = "Workspace 생성", description = "새로운 Workspace를 생성합니다.")
    public ResponseEntity<IdeWorkspace> createIdeWorkspace(
            @Parameter(description = "생성할 Workspace 정보") @RequestBody IdeWorkspace ideWorkspace) {
        IdeWorkspace createdWorkspace = ideWorkspaceService.createIdeWorkspace(ideWorkspace);
        return new ResponseEntity<>(createdWorkspace, HttpStatus.CREATED);
    }

    // DELETE: IdeWorkspace 삭제
    @DeleteMapping("/{name}")
    @Operation(summary = "Workspace 삭제", description = "주어진 이름의 Workspace를 삭제합니다.")
    public ResponseEntity<String> deleteIdeWorkspace(
            @Parameter(description = "삭제할 Workspace의 이름") @PathVariable String name) {
        Optional<IdeWorkspace> workspace = ideWorkspaceService.getIdeWorkspaceByName(name);
        if (workspace.isEmpty()) {
            return new ResponseEntity<>("Workspace does not exist.", HttpStatus.NOT_FOUND);
        }
        ideWorkspaceService.deleteIdeWorkspace(name);
        return new ResponseEntity<>("Workspace deleted successfully.", HttpStatus.OK);
    }

    // PUT: IdeWorkspace 업데이트
    @PutMapping("/{name}")
    @Operation(summary = "Workspace 업데이트", description = "주어진 이름의 Workspace 정보를 업데이트합니다.")
    public ResponseEntity<IdeWorkspace> updateIdeWorkspace(
            @Parameter(description = "업데이트할 Workspace의 이름") @PathVariable String name,
            @Parameter(description = "업데이트할 Workspace 정보") @RequestBody IdeWorkspace updatedIdeWorkspace) {
        Optional<IdeWorkspace> workspace = ideWorkspaceService.getIdeWorkspaceByName(name);
        if (workspace.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        IdeWorkspace updatedWorkspace = ideWorkspaceService.updateIdeWorkspace(name, updatedIdeWorkspace);
        return new ResponseEntity<>(updatedWorkspace, HttpStatus.OK);
    }

    // projectName과 userName으로 IdeWorkspace 검색
    @GetMapping("/user-name/search")
    @Operation(summary = "Workspace 검색", description = "userName으로 Workspace를 검색합니다.")
    public ResponseEntity<IdeWorkspace> getIdeWorkspaces(
            @Parameter(description = "검색할 userName") @RequestParam String userName) {

        Optional<IdeWorkspace> ideWorkspaces = null;

        if (userName != null && !userName.isEmpty()) {
            ideWorkspaces = ideConfigService.findWorkspaceInfoByName(userName);
        }

        return new ResponseEntity<>(ideWorkspaces.get(), HttpStatus.OK);
    }

    // projectName과 userName으로 IdeWorkspace 검색
    @GetMapping("/project-name/search")
    @Operation(summary = "Workspace 검색", description = "project name으로 Workspace를 검색합니다.")
    public ResponseEntity<List<IdeWorkspace>> getIdeWorkspaceWithProjectName(
            @Parameter(description = "검색할 userName") @RequestParam String projectName) {

        List<IdeWorkspace> ideWorkspaces = null;

        if (projectName != null && !projectName.isEmpty()) {
            ideWorkspaces = ideWorkspaceService.findByProjectName(projectName);
        }

        return new ResponseEntity<>(ideWorkspaces, HttpStatus.OK);
    }
}

