package com.kuge.mall.admin.vo;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ActivityUpdateVo extends ActivityAddVo {
    @NotNull(message = "请输入活动id")
    private Long id;
}
