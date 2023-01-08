package com.awe.service.impl;

import com.awe.component.RedisCache;
import com.awe.constant.CacheConstant;
import com.awe.exception.ServiceException;
import com.awe.mapper.EventMapper;
import com.awe.model.entity.EventInfoDO;
import com.awe.model.entity.SysUserDO;
import com.awe.model.other.LoginUser;
import com.awe.model.vo.EventInfoVO;
import com.awe.service.EventService;
import com.awe.utils.SecurityUtils;
import com.awe.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private RedisCache redisCache;

    @Transactional
    @Override
    public void eventInfoRegis(EventInfoVO eventInfoVO) {
        EventInfoDO eventInfoDO = new EventInfoDO();
        BeanUtils.copyProperties(eventInfoVO, eventInfoDO);
        eventInfoDO.setCreateBy(SecurityUtils.getUsername());
        eventInfoDO.setCreateTime(new Date());
        eventMapper.eventInfoRegis(eventInfoDO);
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setUserName(SecurityUtils.getUsername());
        sysUserDO.setEventNm(eventInfoVO.getEventNum());
        sysUserDO.setUpdateTime(new Date());
        eventMapper.connectToUserTable(sysUserDO);
        // 将性别存入缓存
        LoginUser loginUser =  SecurityUtils.getLoginUser();
        loginUser.getUser().setGender(eventInfoVO.getGender());
        redisCache.setCacheObject(CacheConstant.USER_INFO_KEY + loginUser.getUuid(), loginUser);
    }

    @Override
    public List<EventInfoDO> loadEventInfo() {
        List<EventInfoDO> eventInfoDOS = null;
        if ("1".equals(SecurityUtils.getLoginUser().getUser().getGender())) {
            eventInfoDOS = eventMapper.loadEventInfoByGender("0");
        }
        if ("0".equals(SecurityUtils.getLoginUser().getUser().getGender())) {
            eventInfoDOS = eventMapper.loadEventInfoByGender("1");
        }
        if (Objects.isNull(eventInfoDOS)) {
            throw new ServiceException("请先填写个人信息");
        }
        return eventInfoDOS;
    }
}
