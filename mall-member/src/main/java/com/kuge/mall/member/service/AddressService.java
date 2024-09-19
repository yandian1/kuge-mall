package com.kuge.mall.member.service;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.member.vo.AddressAddVo;
import com.kuge.mall.member.vo.AddressUpdateVo;
import com.kuge.mall.member.vo.AddressVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by xbxie on 2024/5/23
 */
public interface AddressService {
    R<AddressVo> getDefault(HttpServletRequest request);

    R<Void> add(AddressAddVo addressAddVo, HttpServletRequest request);

    R<List<AddressVo>> getList(HttpServletRequest request);

    R<AddressVo> getAddress(Long id);

    R<Void> del(Long id);

    R<Void> updateAddress(AddressUpdateVo addressUpdateVo);

    R<Void> setDefault(Long id);
}
