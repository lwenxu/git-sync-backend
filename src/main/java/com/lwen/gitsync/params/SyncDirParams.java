package com.lwen.gitsync.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncDirParams {
    private String syncPath;
    private String repository;
    private String syncRule;
    private int status;
}
