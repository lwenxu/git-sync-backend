package com.lwen.gitsync.utils;

import com.lwen.gitsync.constants.ResultCodeEnum;
import com.lwen.gitsync.vo.ResultVo;
import com.lwen.gitsync.vo.WsResultVO;

public class WsResultUtils {
    public static WsResultVO ret(ResultCodeEnum resultCodeEnum,String type, Object data) {
        return new WsResultVO(resultCodeEnum.getCode(), resultCodeEnum.getMsg(), type, data);
    }

    public static WsResultVO success(Object data,String type) {
        return new WsResultVO(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), type, data);
    }
}
