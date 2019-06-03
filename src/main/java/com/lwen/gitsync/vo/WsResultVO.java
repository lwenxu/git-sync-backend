package com.lwen.gitsync.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WsResultVO<T> {

    public static final String SYNC_STATUS_CHANGE = "sync_status_change";
    public static final String SYNCING_FILES = "syncing_files";
    public static final String SYNC_SUCCESS = "sync_success";
    public static final String SYNC_FAILED = "sync_failed";

    private Integer code;
    private String msg;
    private String type;
    private T data;
}
