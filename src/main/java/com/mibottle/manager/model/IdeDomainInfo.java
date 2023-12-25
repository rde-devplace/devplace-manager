package com.mibottle.manager.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ide_domain_info")
public class IdeDomainInfo {
    @Id
    private String name;
    private String domainURL;
    private String clusterName;

    @Column(columnDefinition = "TEXT")
    private String description;
}
