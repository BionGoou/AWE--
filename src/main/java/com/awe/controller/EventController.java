package com.awe.controller;

import com.awe.exception.ServiceException;
import com.awe.model.entity.EventInfoDO;
import com.awe.model.other.AjaxResult;
import com.awe.model.vo.EventInfoVO;
import com.awe.service.EventService;
import com.awe.utils.SecurityUtils;
import com.awe.utils.StringUtils;
import com.awe.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

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
        String UUID = IdUtils.fastUUID();
        String tmpFilePath =  "c://event_avatars//";
        //没有路径就创建路径
        File tmp = new File(tmpFilePath);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        String resourcesPath = tmpFilePath + UUID + '.' + imgType;
        File upFile = new File(resourcesPath);
        try {
            fileUpload.transferTo(upFile);
        } catch (IOException e) {
            return AjaxResult.error("上传失败,请重新上传");
        }
        AjaxResult ajax = AjaxResult.success("上传成功");
        ajax.put("imgUrl",UUID+"."+imgType);
        return ajax;
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

    @PreAuthorize("hasAuthority('event')")
    @GetMapping("/getEventInfo")
    public AjaxResult getEventInfo() {
        AjaxResult ajax = AjaxResult.success();
        List<EventInfoDO> eventInfoDOS = eventService.loadEventInfo();
        ajax.put("data",eventInfoDOS);
        return ajax;
    }

    @GetMapping("/getAvatar")
    public ResponseEntity<byte[]> getAvatar(@RequestParam("imgUrl") String imgUrl) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream("c://event_avatars//" + imgUrl);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //通过自己写的http工具类获取到图片输入流
        //将输入流转为byte[]
        byte[] bytesByStream = getBytesByStream(inputStream);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(bytesByStream,headers, HttpStatus.OK);
    }


    public byte[]  getBytesByStream(InputStream inputStream){
        byte[] bytes = new byte[1024];

        int b;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            while((b = inputStream.read(bytes)) != -1){

                byteArrayOutputStream.write(bytes,0,b);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
