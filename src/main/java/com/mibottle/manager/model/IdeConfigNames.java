package com.mibottle.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdeConfigNames {
    private String userName;
    private String domainName;
    private String workspaceName;
}
