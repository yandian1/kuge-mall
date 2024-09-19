package com.kuge.mall.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ActivityAddVo {

    @NotBlank(message = "请输入活动名")
    private String name;

    @NotBlank(message = "请上传活动banner")
    private String banner;

    private List<ActivityAddVo.Section> sections;

    @Data
    public static class Section {

        private String title;

        private List<Long> spuIds;
    }
}
