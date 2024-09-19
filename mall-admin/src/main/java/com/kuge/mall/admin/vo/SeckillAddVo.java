package com.kuge.mall.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * created by xbxie on 2024/5/17
 */
@Data
public class SeckillAddVo {

    private String name;

    private String banner;

    private List<Time> timeList;

    @Data
    public static class Time {

        private LocalDateTime startTime;

        private LocalDateTime endTime;

        private List<Long> spuIds;
    }
}