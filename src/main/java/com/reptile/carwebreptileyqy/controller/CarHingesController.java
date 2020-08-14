package com.reptile.carwebreptileyqy.controller;

import com.reptile.carwebreptileyqy.dto.CarHingesDTO;
import com.reptile.carwebreptileyqy.entity.CarHingesInfoEntity;
import com.reptile.carwebreptileyqy.service.CarHingesService;
import com.reptile.carwebreptileyqy.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CarHingesController {

    @Autowired
    CarHingesService carHingesService;

    /**
     * 导入数据
     * @param file
     * @return
     */
    @PostMapping(value = "/carHinges/importCarHingesInfo")
    @ResponseBody
    public String importCarHingesInfo(@RequestParam("file") MultipartFile file) {
        try {
            Object result = carHingesService.importCarHingesInfo(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败!";
        }
        return "上传成功!";
    }

    /**
     * 合页列表查询
     * @param carHingesDTO
     * @return
     */
    @PostMapping(value = "/carHinges/list",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse listAll(
            @RequestBody CarHingesDTO carHingesDTO){
        BaseResponse baseResponse = new BaseResponse();
        Map map = new LinkedHashMap();
        List<CarHingesInfoEntity> list = carHingesService.list(carHingesDTO);
        int total = carHingesService.total(carHingesDTO);
        map.put("total",total);
        map.put("currentPage",carHingesDTO.getCurrentPage());
        map.put("carHingesList",list);
        baseResponse.setData(map);
        return baseResponse;
    }
}
