package com.lwen.gitsync.constants;

import lombok.Data;
import lombok.Getter;

@Getter
public enum  ResultCodeEnum {
    SUCCESS(200, "成功"),
    SETTINGS_GET_SUCCESS(200, "配置获取成功"),
    SYNC_DIR_DELETE_SUCCESS(200, "删除配置成功"),
    SERVER_FAILED(500, "服务端响应失败"),
    ;
    private Integer code;
    private String msg;
    ResultCodeEnum(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }
}
