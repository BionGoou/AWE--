package com.awe.service;

import com.awe.model.entity.EventInfoDO;
import com.awe.model.vo.EventInfoVO;

import java.util.List;

public interface EventService {

    void eventInfoRegis(EventInfoVO eventInfoVO);

    List<EventInfoDO> loadEventInfo();
}
