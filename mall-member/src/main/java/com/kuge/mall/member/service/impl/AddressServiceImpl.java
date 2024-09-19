package com.kuge.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kuge.mall.common.dto.TokenDto;
import com.kuge.mall.common.entity.CommonAddressEntity;
import com.kuge.mall.common.service.CommonAddressService;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.JwtUtils;
import com.kuge.mall.common.utils.R;
import com.kuge.mall.member.service.AddressService;
import com.kuge.mall.member.vo.AddressAddVo;
import com.kuge.mall.member.vo.AddressUpdateVo;
import com.kuge.mall.member.vo.AddressVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by xbxie on 2024/5/23
 */
@Service("addressService")
public class AddressServiceImpl implements AddressService {
    @Resource
    private CommonAddressService commonAddressService;

    @Override
    public R<AddressVo> getDefault(HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);

        CommonAddressEntity commonAddressEntity = commonAddressService.getOne(new QueryWrapper<CommonAddressEntity>().eq("user_id", tokenDto.getId()).eq("is_default", 1));
        if (commonAddressEntity == null) {
            throw new CustomException("无默认地址");
        }

        AddressVo addressVo = new AddressVo();
        BeanUtils.copyProperties(commonAddressEntity, addressVo);

        return R.success(addressVo);
    }

    @Override
    public R<Void> add(AddressAddVo addressAddVo, HttpServletRequest request) {
        CommonAddressEntity commonAddressEntity = new CommonAddressEntity();
        TokenDto tokenDto = JwtUtils.parseToken(request);

        BeanUtils.copyProperties(addressAddVo, commonAddressEntity);
        commonAddressEntity.setUserId(tokenDto.getId());
        commonAddressEntity.setIsDefault(addressAddVo.getIsDefault() ? 1 : 0);

        // 如果添加的地址是用户的第一个地址，将其设置为默认地址
        if (!commonAddressService.exists(new QueryWrapper<CommonAddressEntity>().eq("user_id", tokenDto.getId()))) {
            commonAddressEntity.setIsDefault(1);
        }

        if (!commonAddressService.save(commonAddressEntity)) {
            return R.fail("添加地址失败");
        }

        // 如果新增的地址为默认地址，则将其他存在的默认地址改为非默认
        if (commonAddressEntity.getIsDefault() == 1) {
            commonAddressService.update(new UpdateWrapper<CommonAddressEntity>().set("is_default", 0).eq("user_id", tokenDto.getId()).eq("is_default", 1).ne("id", commonAddressEntity.getId()));
        }

        return R.success("添加地址成功");
    }

    @Override
    public R<List<AddressVo>> getList(HttpServletRequest request) {
        TokenDto tokenDto = JwtUtils.parseToken(request);

        List<CommonAddressEntity> list = commonAddressService.list(new QueryWrapper<CommonAddressEntity>().eq("user_id", tokenDto.getId()));

        if(CollectionUtils.isEmpty(list)) {
            return R.success(new ArrayList<AddressVo>());
        }

        return R.success(
            list.stream().map(commonAddressEntity -> {
                AddressVo addressVo = new AddressVo();
                BeanUtils.copyProperties(commonAddressEntity, addressVo);
                addressVo.setIsDefault(commonAddressEntity.getIsDefault() == 1);
                return addressVo;
            }).collect(Collectors.toList())
        );
    }

    @Override
    public R<AddressVo> getAddress(Long id) {
        CommonAddressEntity commonAddressEntity = commonAddressService.getById(id);

        if (commonAddressEntity == null) {
            throw new CustomException("地址不存在");
        }

        AddressVo addressVo = new AddressVo();
        BeanUtils.copyProperties(commonAddressEntity, addressVo);
        addressVo.setIsDefault(commonAddressEntity.getIsDefault() == 1);

        return R.success(addressVo);
    }

    @Override
    public R<Void> del(Long id) {
        // 删除地址
        if (!commonAddressService.removeById(id)) {
            return R.fail("删除地址失败");
        }

        return R.success("删除地址成功");
    }

    @Override
    public R<Void> updateAddress(AddressUpdateVo addressUpdateVo) {
        // 更新的地址不存在
        CommonAddressEntity commonAddressEntity = commonAddressService.getById(addressUpdateVo.getId());
        if (commonAddressEntity == null) {
            return R.fail("地址不存在");
        }

        BeanUtils.copyProperties(addressUpdateVo, commonAddressEntity);
        commonAddressEntity.setIsDefault(addressUpdateVo.getIsDefault() ? 1 : 0);

        if (!commonAddressService.updateById(commonAddressEntity)) {
            return R.fail("更新地址失败");
        }

        // 如果更新后的地址为默认地址，则将其他存在的默认地址改为非默认
        System.out.println("commonAddressEntity = " + commonAddressEntity);
        if (commonAddressEntity.getIsDefault() == 1) {
            commonAddressService.update(new UpdateWrapper<CommonAddressEntity>().set("is_default", 0).eq("user_id", commonAddressEntity.getUserId()).eq("is_default", 1).ne("id", commonAddressEntity.getId()));
        }

        return R.success("更新地址成功");
    }

    @Override
    public R<Void> setDefault(Long id) {
        CommonAddressEntity commonAddressEntity = commonAddressService.getById(id);
        if (commonAddressEntity == null) {
            throw new CustomException("地址不存在");
        }

        // 将其他的默认地址设置为非默认地址
        List<CommonAddressEntity> commonAddressEntities = commonAddressService.list(new QueryWrapper<CommonAddressEntity>().ne("id", id).eq("is_default", 1));
        if (!CollectionUtils.isEmpty(commonAddressEntities)) {
            commonAddressEntities.forEach(item -> item.setIsDefault(0));
            System.out.println("commonAddressEntities = " + commonAddressEntities);
            if (!commonAddressService.updateBatchById(commonAddressEntities)) {
                throw new CustomException("设置默认地址失败");
            }
        }

        commonAddressEntity.setIsDefault(1);
        if (!commonAddressService.updateById(commonAddressEntity)) {
            throw new CustomException("设置默认地址失败");
        }

        return R.success("设置默认地址成功");
    }
}
