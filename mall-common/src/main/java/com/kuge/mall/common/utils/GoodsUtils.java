package com.kuge.mall.common.utils;

import com.alibaba.fastjson.JSON;
import com.kuge.mall.common.constant.SpuStatusEnum;

import java.util.Map;

/**
 * created by xbxie on 2024/6/4
 */
public class GoodsUtils {
    public static String getAttrValues(String attrs) {
        Map<String, String> attrsMap = (Map<String, String>) JSON.parse(attrs);
        if(attrsMap == null) {
            return "";
        } else {
            return String.join(",", attrsMap.values());
        }
    }

    public static String getDesc(Integer status) {

        if(status == SpuStatusEnum.SHELF_DOWN.getCode()) return "未上架";

        if(status == SpuStatusEnum.SHELF_UP.getCode()) return "已上架";

        return "";
    }
}
