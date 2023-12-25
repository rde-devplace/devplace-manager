package com.mibottle.manager.customresource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdeRole {
    private String kind;
    private String name;
    private String roleBinding;
}
