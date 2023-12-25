package com.mibottle.manager.model;

import lombok.Data;

@Data
public class IdeConfigInfoDTO {
    private String name;
    private String ideConfigCrdName;
    private String status;
    private String namespace;
    private String userName;
    private String workspaceName; // workspace의 name
    private String domainName; // domainInfo의 domainName

    // 생성자, getter, setter 생략

    // IdeConfigInfo 엔티티로부터 DTO를 생성하는 정적 메소드
    public static IdeConfigInfoDTO from(IdeConfigInfo ideConfigInfo) {
        IdeConfigInfoDTO dto = new IdeConfigInfoDTO();
        dto.setName(ideConfigInfo.getName());
        dto.setIdeConfigCrdName(ideConfigInfo.getIdeConfigCrdName());
        dto.setStatus(ideConfigInfo.getStatus());
        dto.setNamespace(ideConfigInfo.getNamespace());
        dto.setUserName(ideConfigInfo.getUserName());
        dto.setWorkspaceName(ideConfigInfo.getWorkspace() != null ? ideConfigInfo.getWorkspace().getName() : null);
        dto.setDomainName(ideConfigInfo.getDomainInfo() != null ? ideConfigInfo.getDomainInfo().getName() : null);
        return dto;
    }
}

