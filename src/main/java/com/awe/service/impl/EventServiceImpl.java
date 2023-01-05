package com.awe.service.impl;

import com.awe.mapper.EventMapper;
import com.awe.model.entity.EventInfoDO;
import com.awe.model.vo.EventInfoVO;
import com.awe.service.EventService;
import com.awe.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventMapper eventMapper;

//    @Transactional
    @Override
    public void eventInfoRegis(EventInfoVO eventInfoVO) {
        EventInfoDO eventInfoDO = new EventInfoDO();
        BeanUtils.copyProperties(eventInfoVO, eventInfoDO);
        eventInfoDO.setCreateBy("baba");
        eventInfoDO.setCreateTime(new Date());
        eventMapper.eventInfoRegis(eventInfoDO);
    }
}
