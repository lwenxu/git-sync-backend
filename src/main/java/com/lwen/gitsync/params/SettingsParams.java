package com.lwen.gitsync.params;

import lombok.Data;

@Data
public class SettingsParams {
    private String gitPath;
    private String sshKey;
    private String repository;
    private String times;
    private String syncDir;
}
