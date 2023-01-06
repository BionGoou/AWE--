package com.awe.controller;

import com.awe.exception.ServiceException;
import com.awe.model.other.AjaxResult;
import com.awe.model.vo.EventInfoVO;
import com.awe.service.EventService;
import com.awe.utils.SecurityUtils;
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
    @PreAuthorize("hasAuthority('event')")
    @PostMapping("/imgUpload")
    public AjaxResult imgUpload(@RequestParam("file") MultipartFile fileUpload) {
        String fileName = fileUpload.getOriginalFilename();
        String imgType = "";
        if(StringUtils.isBlank(fileName)){
            throw new ServiceException("文件名不能为空");
        }else{
            String[] arr = fileName.split("\\.");
            imgType = arr[arr.length - 1];
        }
        String tmpFilePath =  "c://event_user_info//"+ SecurityUtils.getUsername() + "//";
        //没有路径就创建路径
        File tmp = new File(tmpFilePath);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        String resourcesPath = tmpFilePath + "//avatar." +imgType;
        File upFile = new File(resourcesPath);
        try {
            fileUpload.transferTo(upFile);
        } catch (IOException e) {
            return AjaxResult.error("上传失败,请重新上传");
        }
        return AjaxResult.success("上传成功");
    }

    /**
     * 登录方法
     *
     * @param eventInfoVO 登录信息
     * @return 结果
     */
    @PreAuthorize("hasAuthority('event')")
    @PostMapping("/eventInfoRegis")
    public AjaxResult eventInfoRegis(@RequestBody EventInfoVO eventInfoVO) {
        AjaxResult ajax = AjaxResult.success();
        eventService.eventInfoRegis(eventInfoVO);
        return ajax;
    }
}
