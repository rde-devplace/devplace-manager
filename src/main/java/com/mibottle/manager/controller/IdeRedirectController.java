package com.mibottle.manager.controller;

import com.mibottle.manager.model.IdeCommon;
import com.mibottle.manager.model.IdeDomainInfo;
import com.mibottle.manager.service.IdeConfigService;
import com.mibottle.manager.service.IdeWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/ide-manager/api/domain")
public class IdeRedirectController {

    private final IdeWorkspaceService ideWorkspaceService;
    private final IdeConfigService ideConfigInfoService;

    @Autowired
    public IdeRedirectController(IdeWorkspaceService ideWorkspaceService, IdeConfigService ideConfigInfoService) {
        this.ideWorkspaceService = ideWorkspaceService;
        this.ideConfigInfoService = ideConfigInfoService;
    }

    @GetMapping("/url")
    public String redirectVscode(@RequestParam String userName ) throws IOException {

        Optional<IdeDomainInfo> opt = ideConfigInfoService.findDomainInfoByName(userName + IdeCommon.IDECONFIG_POSTFIX);
        if (opt.isEmpty()) {
            return "https://kube-proxy-rs.skamdp.org";
        }

        return opt.get().getDomainURL();
    }
}
