package com.mibottle.manager.model;

public final class IdeCommon {

    private IdeCommon() {
    }

    public static final String IDECONFIG_POSTFIX = "-vscode-server";

    public static String getIdeConfigInfoName(String userName, String workspaceName, String domainName) {
        return userName + "." + domainName + "." + workspaceName;
    }

    // getNames 메소드 구현
    public static IdeConfigNames getNames(String ideConfigInfoName) {
        String[] parts = ideConfigInfoName.split("\\.");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid ideConfigInfoName format");
        }
        return new IdeConfigNames(parts[0], parts[1], parts[2]);
    }
}



