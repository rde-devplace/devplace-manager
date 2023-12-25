package com.mibottle.manager.customresource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Port {
    private String name;
    private String protocol;
    private int port;
    private int targetPort;
}
