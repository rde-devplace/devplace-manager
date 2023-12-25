package com.mibottle.manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ide_config_info")
public class IdeConfigInfo {

    @Id
    private String name;
    private String ideConfigCrdName;
    private String status;
    private String namespace;
    private String userName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_info_id")
    private IdeDomainInfo domainInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private IdeWorkspace workspace;
}
