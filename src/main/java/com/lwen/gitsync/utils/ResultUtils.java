package com.lwen.gitsync.utils;

import com.lwen.gitsync.constants.ResultCodeEnum;
import com.lwen.gitsync.vo.ResultVo;

public class ResultUtils{
    public static ResultVo ret(ResultCodeEnum resultCodeEnum,Object data) {
        return new ResultVo(resultCodeEnum.getCode(), resultCodeEnum.getMsg(), data);
    }

    public static ResultVo success(Object data) {
        return new ResultVo(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), data);
    }
}
