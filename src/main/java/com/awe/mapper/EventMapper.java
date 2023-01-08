package com.awe.mapper;

import com.awe.model.entity.EventInfoDO;
import com.awe.model.entity.SysUserDO;

import java.util.List;

public interface EventMapper {

    List<EventInfoDO> loadEventInfoByGender(String gender);
    void eventInfoRegis(EventInfoDO eventInfoVO);

    void connectToUserTable(SysUserDO sysUserDO);
}
