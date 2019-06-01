package com.lwen.gitsync.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WsResultVO<T> {

    public static final String SYNC_STATUS_CHANGE = "sync_status_change";

    private Integer code;
    private String msg;
    private String type;
    private T data;
}
