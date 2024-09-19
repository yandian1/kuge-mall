package com.kuge.mall.member.vo;

import lombok.Data;

/**
 * created by xbxie on 2024/5/23
 */
@Data
public class AddressVo {
    /**
     * 地址id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 省 name
     */
    private String province;

    /**
     * 市 name
     */
    private String city;

    /**
     * 区 name
     */
    private String county;

    /**
     * 省 code
     */
    private String provinceCode;

    /**
     * 市 code
     */
    private String cityCode;

    /**
     * 区 code
     */
    private String countyCode;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 是否为默认地址
     */
    private Boolean isDefault;
}
