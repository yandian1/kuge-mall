package com.kuge.mall.product.service;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.product.vo.CategoryListReqVo;
import com.kuge.mall.product.vo.CategoryVo;

import java.util.List;

/**
 * created by xbxie on 2024-05-16 23:53:49
 */
public interface CategoryService {

    R<List<CategoryVo>> list(CategoryListReqVo categoryListReqVo);
}

