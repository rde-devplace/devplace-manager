package com.mibottle.manager.customresource;

import lombok.Data;

@Data
public class Git {
    String id;
    String token;
    String repository;
    String branch;
}
