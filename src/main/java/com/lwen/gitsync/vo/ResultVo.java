package com.lwen.gitsync.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultVo<T> {
    private Integer code;
    private String msg;
    private T data;
}
