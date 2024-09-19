package com.kuge.mall.admin.vo;

import lombok.Data;
import java.util.List;
import javax.validation.constraints.NotBlank;

/**
 * created by xbxie on 2024/4/25
 */
@Data
public class RoleAddVo {
    /**
     * 角色名
     */
    @NotBlank(message = "请输入角色名")
    private String name;


    /**
     * 角色菜单id集合
     */
    private List<Long> menuIds;
}
