package com.kuge.mall.member.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * created by xbxie on 2024/5/23
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressUpdateVo extends AddressAddVo {
    /**
     * id
     */
    @NotNull(message = "请输入id")
    private Long id;
}
