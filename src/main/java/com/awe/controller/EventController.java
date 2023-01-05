package com.awe.controller;

import com.awe.exception.ServiceException;
import com.awe.model.other.AjaxResult;
import com.awe.model.vo.EventInfoVO;
import com.awe.service.EventService;
import com.awe.utils.StringUtils;
import com.awe.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 活动业务
 *
 * @author BionGo
 */
@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * 上传图片
     *
     * @param fileUpload 上传图片
     */
    @PreAuthorize("@av.hasAuthority('event')")
    @PostMapping("/imgUpload")
    public String imgUpload(@RequestParam("file") MultipartFile fileUpload) {
        //获取文件名
        String fileName = fileUpload.getOriginalFilename();
        String imgType = "";
        if(StringUtils.isBlank(fileName)){
            throw new ServiceException("文件名不能为空");
        }else{
            String[] arr = fileName.split("\\.");
            imgType = arr[arr.length - 1];
        }

        String UUID = IdUtils.fastUUID();
        String tmpFilePath =  "c://test//image//"  ;
        //没有路径就创建路径
        boolean result = false;
        File tmp = new File(tmpFilePath);
        if (!tmp.exists()) {
            result = tmp.mkdirs();
        }
        String resourcesPath = tmpFilePath + "//" + UUID + "." +imgType;
        File upFile = new File(resourcesPath);
        try {
            fileUpload.transferTo(upFile);
        } catch (IOException e) {
            return "";
        }
        return UUID;
    }

    /**
     * 登录方法
     *
     * @param eventInfoVO 登录信息
     * @return 结果
     */
    @PreAuthorize("@av.hasAuthority('event')")
    @PostMapping("/eventInfoRegis")
    public AjaxResult eventInfoRegis(@RequestBody EventInfoVO eventInfoVO) {
        AjaxResult ajax = AjaxResult.success();
        eventService.eventInfoRegis(eventInfoVO);
        return ajax;
    }
}
