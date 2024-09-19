package com.kuge.mall.common.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页数据工具类
 * created by xbxie on 2024/4/24
 */
@Data
public class PageData<E> {
    /**
     * 当前页码
     */
    private Long pageNum;

    /**
     * 每页包含的数据条数
     */
    private Long pageSize;

    /**
     * 当前页的数据
     */
    private List<E> list;

    /**
     * 数据总条数
     */
    private Long total;

    public static <P, C> PageData<C> getPageData(Page<P> page, Class<C> cla) {
        PageData<C> pageData = new PageData<>();

        pageData.setPageSize(page.getSize());
        pageData.setPageNum(page.getCurrent());
        pageData.setList(
            page.getRecords().stream().map(record -> JSON.parseObject(JSON.toJSONString(record), cla)).collect(Collectors.toList())
        );
        pageData.setTotal(page.getTotal());


        return pageData;
    }

    public static <T> PageData<T> getPageData(Page<T> page) {
        PageData<T> pageData = new PageData<>();

        pageData.setPageSize(page.getSize());
        pageData.setPageNum(page.getCurrent());
        pageData.setList(page.getRecords());
        pageData.setTotal(page.getTotal());

        return pageData;
    }

    public static <T> PageData<T> getPageData(Long pageNum, Long pageSize, Long total, List<T> list) {
        PageData<T> pageData = new PageData<>();

        pageData.setPageNum(pageNum);
        pageData.setPageSize(pageSize);
        pageData.setList(list);
        pageData.setTotal(total);

        return pageData;
    }

    public static <T> PageData<T> getPageData(Long pageNum, Long pageSize, List<T> list) {
        PageData<T> pageData = new PageData<>();

        long total = list.size();
        long startIdx = (pageNum - 1) * pageSize;
        long endIdx = startIdx + pageSize;


        if (startIdx < 0) {
            startIdx = 0;
        }
        if (startIdx > total) {
            startIdx =  total;
        }

        if (endIdx < 0) {
            endIdx = 0;
        }
        if (endIdx > total) {
            endIdx = total;
        }

        pageData.setPageNum(pageNum);
        pageData.setPageSize(pageSize);
        pageData.setList(list.subList((int) startIdx, (int) endIdx));
        pageData.setTotal(total);

        return pageData;
    }

    public static <T, C> PageData<C> getPageData(Long pageNum, Long pageSize, List<T> list, Class<C> cla) {
        PageData<T> pageData = getPageData(pageNum, pageSize, list);

        List<C> cList = pageData.getList().stream().map(record -> JSON.parseObject(JSON.toJSONString(record), cla)).collect(Collectors.toList());

        return getPageData(pageNum, pageSize, pageData.getTotal(), cList);
    }


    public static <C> PageData<C> emptyPageData(Class<C> cla) {
        PageData<C> pageData = new PageData<>();

        pageData.setPageSize(0L);
        pageData.setPageNum(0L);
        pageData.setList(new ArrayList<C>());
        pageData.setTotal(0L);


        return pageData;
    }
}
