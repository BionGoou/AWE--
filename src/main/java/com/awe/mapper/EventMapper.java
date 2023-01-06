package com.awe.mapper;

import com.awe.model.entity.EventInfoDO;
import com.awe.model.entity.SysUserDO;

public interface EventMapper {

    void eventInfoRegis(EventInfoDO eventInfoVO);

    void connectToUserTable(SysUserDO sysUserDO);
}
