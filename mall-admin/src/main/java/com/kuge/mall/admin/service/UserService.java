package com.kuge.mall.admin.service;

import com.kuge.mall.admin.vo.*;
import com.kuge.mall.common.entity.CommonUserEntity;
import com.kuge.mall.common.utils.PageData;
import com.kuge.mall.common.utils.R;

public interface UserService {
    R<Void> add(UserAddVo userAddVo);
    R<Void> del(Long id);
    R<Void> updateUser(UserUpdateVo userUpdateVo);
    R<PageData<UserPageItemVo>> pageList(UserPageVo userPageVo);

    R<Void> changeStatus(UserStatusVo userStatusVo);

    R<UserDetailVo> getUser(Long id);

    R<Void> updatePwd(UserUpdatePwdVo userUpdatePwdVo);
}
